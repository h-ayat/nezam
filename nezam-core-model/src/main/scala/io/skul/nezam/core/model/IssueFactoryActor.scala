package io.skul.nezam.core.model

import akka.actor.Actor
import io.skul.nezam.core.model.IssueFactoryActor.GetIssueQuery

/**
  * @author S.Hosein Ayat
  */
class IssueFactoryActor(eventStore: IssueEventStore) extends Actor {

  override def preStart(): Unit = {
    super.preStart()
    context.system.eventStream.subscribe(self, classOf[IssueEvent])
  }

  override def receive: Receive = {
    case e: IssueCreatedEvent =>
      eventStore.saveEvent(e)


    case GetIssueQuery(id) =>
      import context.dispatcher

      eventStore.getEvents(id).map { events =>
        val createdEvent = events.head.asInstanceOf[IssueCreatedEvent]
        val task = Task(createdEvent.issueId, None, createdEvent.title, "", Nil, None, TaskState.BackLog)
        val issue = Issue(createdEvent.issueId, createdEvent.title, TaskPriority.Medium, task :: Nil,
          createdEvent.issuerId, createdEvent.date)

        val newIssue = events.tail.foldLeft(issue) { (acc, event) =>
          event match {
            case IssueGroomedEvent(_, _, _) =>
              val tasks = acc.tasks.map { task =>
                if (task.state == TaskState.BackLog)
                  task.copy(state = TaskState.Ready)
                else
                  task
              }
              acc.copy(tasks = tasks)
          }
        }
      }
  }
}


object IssueFactoryActor {

  trait IssueQuery

  case class GetIssueQuery(id: String) extends IssueQuery

}
