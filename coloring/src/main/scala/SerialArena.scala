import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.event.LoggingReceive

/**
 * Created by d.a.martyanov on 22.12.14.
 */
class SerialArena(bs: Map[ActorRef, Int], n: Int, m: Int, iterations: Int) extends Actor with ActorLogging {

  var field: Field = FieldProcessor.make(n, m)
  var round = 0
  val playersOrder = scala.util.Random.shuffle(bs.keys) toList
  var playersTimings = bs map { case (af, _) => af -> 0l}
  var lastTurn = new java.util.Date().getTime

  // здесь должна быть логика расположения ботов начальная
  bs foreach { case (_, color) => field = FieldProcessor.setColor(field, color, Cord(color, color)) }

  nextRound(None)

  override def receive: Receive = LoggingReceive {
    case bm: BotMove if bm.round == round =>
      val s = sender()
      countLatency(s)
      field = FieldProcessor.setColor(field, bm.color, bm.cord)
      println(s"Field after color [${bs.getOrElse(s, 0)}] turn")
      FieldProcessor printField field
      println("\n")
      val nextPlayer = playersOrder.dropWhile(_ != sender).drop(1).headOption
      nextRound(nextPlayer)
  }

  def nextRound(nextPlayer: Option[ActorRef]) = nextPlayer match {
    case Some(af) => sendFieldtoPlayer(af)

    case None if !gameFinished =>
      round += 1
      println(s"Round $round started, initial field")
      FieldProcessor printField field
      println("\n")
      lastTurn = new java.util.Date().getTime
      sendFieldtoPlayer(playersOrder.head)
    case _ =>
      bs.keys.foreach(_ ! GameFinished)
      context.parent ! finishResults
      context.stop(self)
  }

  def sendFieldtoPlayer(pl: ActorRef) = {
    lastTurn = new java.util.Date().getTime
    pl ! NextTurn(round, field)
  }

  def countLatency(sender: ActorRef) = {
    val current = new java.util.Date().getTime
    val diff = current - lastTurn
    playersTimings += sender -> (playersTimings.getOrElse(sender, 0l) + diff)
  }

  def gameFinished = round >= iterations

  def finishResults: Finish = {
    val playersResults = playersTimings.toList.sortBy(_._2) map {case (af, lat) => bs.getOrElse(af, 0) -> lat }
    Finish(playersResults.head, playersResults.tail)
  }
}
