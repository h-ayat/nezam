package io.skul.nezam.core.model

/**
  * @author S.Hosein Ayat
  */

sealed trait Priority {
  def value: Int
}

object Priority {

  case object Low extends Priority {
    override val value = 0
  }

  case object Medium extends Priority {
    override val value = 1
  }

  case object High extends Priority {
    override val value = 2
  }

  case object Critical extends Priority {
    override val value = 3
  }

}

sealed trait TaskState

object TaskState {

  case object BackLog extends TaskState

  case object Ready extends TaskState

  case object Dev extends TaskState

  case object Review extends TaskState

  case object Test extends TaskState

  case object Deploy extends TaskState

  case object Finished extends TaskState

}


case class Issue(id: String, title: String, priority: Priority = Priority.Medium, tasks: List[Task] = Nil, issuerId: String, date: Long)

case class Task(id: String, assigneeId: Option[String], title: String, description: String, tags: List[String] = Nil, reviewerId: Option[String],
                state: TaskState)


trait IssueEvent {
  def issueId: String

  def date: Long
}

case class IssueCreatedEvent(issueId: String, title: String, issuerId: String, date: Long) extends IssueEvent

case class IssueGroomedEvent(issueId: String, groomerId: String, date: Long) extends IssueEvent


trait Command {
  def userId: String
}

case class CreateIssueCommand(title: String, userId: String) extends Command

case class GroomIssueCommand(issueId: String, userId: String) extends Command


object IssueAggregate {

  import scala.collection.mutable

  val repo: mutable.HashMap[String, Issue] = mutable.HashMap[String, Issue]()

  def processCommand(command: Command): List[IssueEvent] = {
    val now = System.currentTimeMillis()
    command match {
      case CreateIssueCommand(title, userId) =>

        val task = Task(uuid, None, title, "", Nil, None, TaskState.BackLog)

        val issue = Issue(uuid, title, Priority.Medium, task :: Nil, userId, now)
        repo.put(issue.id, issue)
        IssueCreatedEvent(issue.id, title, userId, now) :: Nil

      case GroomIssueCommand(issueId, userId) =>
        val issue = repo(issueId)
        val copyIssue = issue.copy(tasks = issue.tasks.map(_.copy(state = TaskState.Ready)))
        repo.put(issueId, copyIssue)
        IssueGroomedEvent(issueId, userId, now) :: Nil
    }
  }


  def uuid: String = ???
}