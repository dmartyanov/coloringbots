import akka.actor.{Props, ActorRef, Actor}
import akka.event.LoggingReceive

/**
 * Created by d.a.martyanov on 19.12.14.
 */
class Manager extends Actor {

  val iterationNo = 50

  override def receive: Receive = LoggingReceive {
    case Start(bs, n, m) =>
      context.actorOf(Props(new SerialArena(bs, n, m, iterationNo)))
    case Finish(wn, ls) =>
      println(s"Winner's color [${wn._1}] with time [${wn._2}]")
      println(s"[${ls.map(_._1).mkString(" ")}] loseb with times [${ls.map(_._2).mkString(" ")}]")
  }
}

case class Start(bots: Map[ActorRef, Int], n: Int, m: Int)
case class Finish(winner: (Int, Long), loosers: List[(Int, Long)])