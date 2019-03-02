package io.skul.nezam.core.model

import scala.concurrent.Future

/**
  * @author S.Hosein Ayat
  */
trait IssueEventStore {
  def saveEvent(issueEvent: IssueEvent): Future[Unit]

  def getEvents(issueId: String): Future[List[IssueEvent]]
}
