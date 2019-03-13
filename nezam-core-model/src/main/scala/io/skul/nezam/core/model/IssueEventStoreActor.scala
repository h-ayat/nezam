package io.skul.nezam.core.model

import java.io.{File, FileWriter}

import akka.actor.Actor
import org.json4s.Formats
import org.json4s.jackson.{JsonMethods, Serialization}

/**
  * @author S.Hosein Ayat
  */
class IssueEventStoreActor extends Actor{

  val path = "./data/issues/"

  override def preStart(): Unit = {
    context.system.eventStream.subscribe(self, classOf[IssueEvent])
  }


  override def receive: Receive = {
    case e: IssueEvent =>
      val file = new File(path + e.issueId)
      val fileWriter = new FileWriter(file, true)
      implicit val format: Formats = TaskJsonFormats.eventFormat
      val str = Serialization.write(e)
      fileWriter.write(str + "\n")
      fileWriter.close()
  }
}
