import akka.actor.{Props, ActorRef, Actor}
import akka.event.LoggingReceive

/**
 * Created by d.a.martyanov on 19.12.14.
 */
class Manager extends Actor {

  val iterationNo = 50

  override def receive: Receive = LoggingReceive {
    case Start(bs, n, m) =>
      context.actorOf(Props(new Arena(bs, n, m, iterationNo)))
    case Finish(wn, ls) =>
      println(s"Winner's color [${wn}}]")
      println(s"[${ls.mkString(" ")}] lose")
  }
}

case class Start(bots: Map[ActorRef, Int], n: Int, m: Int)
case class Finish(winner: Int, loosers: List[Int])