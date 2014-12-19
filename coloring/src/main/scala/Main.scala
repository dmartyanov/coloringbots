import akka.actor.{Props, ActorSystem}
import com.typesafe.scalalogging.StrictLogging

/**
 * Created by d.a.martyanov on 19.12.14.
 */
object Main extends App with StrictLogging{
  logger.info("Game started")
  val as = ActorSystem("game")

  //акторы
  val a1 = as.actorOf(Props(new RandomDemoBot(3, 10, 10)))
  val a2 = as.actorOf(Props(new RandomDemoBot(5, 10, 10)))
  val a3 = as.actorOf(Props(new RandomDemoBot(1, 10, 10)))

  as.actorOf(Props[Manager], "manager") ! Start(Map(a1 -> 3, a2 -> 5, a3 -> 1), 10, 10)
}
