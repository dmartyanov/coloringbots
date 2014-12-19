import akka.actor.{ActorLogging, Actor}
import akka.event.LoggingReceive

/**
 * Created by d.a.martyanov on 19.12.14.
 */
abstract class BotBase(val color: Int) extends Actor with ActorLogging{
  override def receive: Receive = LoggingReceive {
    case NextTurn(round, field) => sender ! BotMove(color, round, action(field))
    case GameFinished => context.stop(self)
  }

  def action(field: Field): Cord
}

case class NextTurn(roung: Int, field: Field)

case class BotMove(color: Int, round: Int, cord: Cord)

case object GameFinished

class RandomDemoBot(override val color: Int, n: Int, m: Int) extends BotBase(color) {
  override def action(field: Field): Cord =
    Cord( scala.util.Random.nextInt(n) , scala.util.Random.nextInt(m))

}