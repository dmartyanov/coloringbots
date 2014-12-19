import akka.actor.{ActorLogging, ReceiveTimeout, Actor, ActorRef}
import akka.event.LoggingReceive

import scala.concurrent.duration._

/**
 * Created by d.a.martyanov on 19.12.14.
 */
class Arena(bs: Map[ActorRef, Int], n: Int, m: Int, iterations: Int) extends Actor with ActorLogging{

  context.setReceiveTimeout(1 seconds)
  val actions = scala.collection.mutable.Map[ActorRef, BotMove]()
  var field: Field = FieldProcessor.make(n, m)
  var round = 0

  // здесь должна быть логика расположения ботов начальная
  bs foreach { case (_, color) => field = FieldProcessor.setColor(field, color, Cord(color, color)) }

  nextRoundBroadcast(bs.keys.toList)
  
  override def receive: Receive = LoggingReceive {
    case bm: BotMove if bm.round == round =>
      actions.put(sender, bm)
    case ReceiveTimeout =>
      field = FieldProcessor.update(field, actions.values)

      if(gameFinished) {
        bs.keys.foreach( _ ! GameFinished)
        context.parent ! Finish(5, List(1, 3))
        context.stop(self)
      }
      else
        nextRoundBroadcast(bs.keys.toList)
  }

  def nextRoundBroadcast(ars: List[ActorRef]) = {
    actions.clear()
    round += 1
    println(s"Round $round started, initial field")
    FieldProcessor printField field
    println("\n")
    ars.foreach( _ ! NextTurn(round, field))
  }

  def gameFinished = round > iterations
}
