package io.skul.nezam.core.model

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import io.skul.nezam.core.model.IssueService.{GET, Get}

object IssueFactory {
  def createIssue(event: IssueCreatedEvent): Issue = {
    val task = Task(event.issueId, None, event.title, "", Nil, None, TaskState.BackLog)
    val issue = Issue(event.issueId, event.title, TaskPriority.Medium, task :: Nil,
      event.issuerId, event.date)
    issue
  }
}


class IssueAggregateActor(events: List[IssueEvent]) extends Actor {

  private var issue: Issue = {
    val createdEvent = events.head.asInstanceOf[IssueCreatedEvent]
    val issue = IssueFactory.createIssue(createdEvent)
    events.tail.foldLeft(issue)(mutate)
  }

  private def mutate(old: Issue, event: IssueEvent): Issue = {
    event match {
      case IssueGroomedEvent(_, _, _) =>
        val tasks = old.tasks.map { task =>
          if (task.state == TaskState.BackLog)
            task.copy(state = TaskState.Ready)
          else
            task
        }
        old.copy(tasks = tasks)

      case ChertEvent(_, _) =>
        old

      case Chert2Event(_, title, _) =>
        old.copy(title = title)
    }
  }

  override def receive: Receive = {
    case GroomIssueCommand(_, userId) =>
      if (issue.tasks.exists(_.state == TaskState.BackLog)) {
        sender().tell(true, self)
        val event = IssueGroomedEvent(issue.id, userId, System.currentTimeMillis())
        this.issue = mutate(this.issue, event)
        context.system.eventStream.publish(event)
      } else {
        sender() ! false
      }

    case GET =>
      sender() ! this.issue
  }
}

class IssueServiceActor extends Actor {

  private val children = scala.collection.mutable.HashMap[String, ActorRef]()

  def uuid(): String = UUID.randomUUID().toString

  def getChild(id: String) : ActorRef = {
    children.get(id) match {
      case Some(actor) => actor
      case _ =>
        // read events from event store
        val events: List[IssueEvent] = Nil
        val child = context.actorOf(Props(new IssueAggregateActor(events)), s"childIssue-$id")
        children.put(id, child)
        child
    }
  }

  override def receive: Receive = {
    case CreateIssueCommand(title, userId) =>
      val id = uuid()
      val issueCreatedEvent = IssueCreatedEvent(id, title, userId, System.currentTimeMillis())
      context.system.eventStream.publish(issueCreatedEvent)
      getChild(id)
      sender() ! true

    case command: IssueCommand =>
      getChild(command.issueId).forward(command)

    case Get(id) =>
      getChild(id).forward(GET)
  }
}

object IssueService {

  case object GET

  case class Get(id: String)

}