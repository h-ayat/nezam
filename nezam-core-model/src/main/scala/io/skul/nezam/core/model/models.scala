package io.skul.nezam.core.model

import akka.actor.Actor

import scala.concurrent.Future

/**
  * @author S.Hosein Ayat
  */

trait EnumHelper[T] {
  def all: List[T]

  lazy val names: Set[String] = all.map(_.toString).toSet

  lazy val nameMap: Map[String, T] = all.map(t => t.toString -> t).toMap
}

sealed trait TaskPriority {
  def value: Int
}

object TaskPriority extends EnumHelper[TaskPriority] {

  case object Low extends TaskPriority {
    override val value = 0
  }

  case object Medium extends TaskPriority {
    override val value = 1
  }

  case object High extends TaskPriority {
    override val value = 2
  }

  case object Critical extends TaskPriority {
    override val value = 3
  }

  val all: List[TaskPriority] = Low :: Medium :: High :: Critical :: Nil

}

sealed trait TaskState

object TaskState extends EnumHelper[TaskState] {

  case object BackLog extends TaskState

  case object Ready extends TaskState

  case object Dev extends TaskState

  case object Review extends TaskState

  case object Test extends TaskState

  case object Deploy extends TaskState

  case object Finished extends TaskState

  val all: List[TaskState] = BackLog :: Ready :: Dev :: Review :: Test :: Deploy :: Finished :: Nil
}


case class Issue(id: String, title: String, priority: TaskPriority = TaskPriority.Medium, tasks: List[Task] = Nil, issuerId: String, date: Long)

case class Task(id: String, assigneeId: Option[String], title: String, description: String, tags: List[String] = Nil, reviewerId: Option[String],
                state: TaskState)


trait IssueEvent {
  def issueId: String

  def date: Long
}

case class IssueCreatedEvent(issueId: String, title: String, issuerId: String, date: Long) extends IssueEvent

case class IssueGroomedEvent(issueId: String, groomerId: String, date: Long) extends IssueEvent

case class ChertEvent(issueId: String, date: Long) extends IssueEvent

case class Chert2Event(issueId: String, title: String, date: Long) extends IssueEvent


trait Command {
  def userId: String
}

trait IssueCommand extends Command {
  def issueId: String
}

case class CreateIssueCommand(title: String, userId: String) extends Command

case class GroomIssueCommand(issueId: String, userId: String) extends IssueCommand


class IssueCommandEndpoint extends Actor {

  def uuid: String = ???

  override def receive: Receive = {
    case CreateIssueCommand(title, userId) =>
      val now = System.currentTimeMillis()
      val id = uuid
      val event = IssueCreatedEvent(id, title, userId, now)
      context.system.eventStream.publish(event)

  }
}