package io.skul.nezam.core.model

import io.skul.nezam.core.model.TaskState.BackLog
import org.json4s.{CustomSerializer, DefaultFormats, Formats, ShortTypeHints}
import org.json4s.JsonAST.{JField, JInt, JObject, JString}
import org.json4s.jackson.Serialization

/**
  * @author S.Hosein Ayat
  */
object TaskJsonFormats {
  val taskFormat: Formats = DefaultFormats + new TaskStateSerializer
  val eventFormat: Formats = Serialization.formats(ShortTypeHints(List(classOf[IssueGroomedEvent], classOf[IssueCreatedEvent])))

}


class TaskStateSerializer extends CustomSerializer[TaskState](_ => ( {
  case JString(s) if TaskState.names.contains(s) =>
    TaskState.nameMap(s)
}, {
  case x: TaskState =>
    JString(x.toString)
}
))