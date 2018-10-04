package io.skul.nezam.gen.models

import io.skul.proximo.model._

/**
  * @author S.Hosein Ayat
  */
object Models {
  val packName = "io.skul.nezam.models"

  object UserModel extends Entity with ComplexHelper {

    val id: PKey = _id()
    val username: SimpleAttr = attr("username", StrTyp)
    val password: SimpleAttr = attr("username", StrTyp)
    val enabled: SimpleAttr = attr("enabled", StrTyp)

    override def name: String = "User"

    override def pack: String = packName

    override def attrs: List[Attr] = id :: username :: password :: enabled :: Nil
  }

  object IssueModel extends PolymorphEntity with ComplexHelper {

    val id: PKey = _id()

    val title: SimpleAttr = attr("title", StrTyp)

    val description: SimpleAttr = attr("description", StrTyp)

    val assignee: FKey = fk(UserModel.id, "assignee").?

    val storyPoints: SimpleAttr = attr("storyPoints", IntTyp).?

    val originalEstimate: SimpleAttr = attr("originalEstimate", IntTyp).?

    override def name: String = "Issue"

    override def pack: String = packName

    override def attrs: List[Attr] = id :: title :: description :: assignee :: Nil
  }

  object TaskModel extends SubtypeEntity with ComplexHelper {
    override def superTyp: PolymorphEntity = IssueModel

    override def name: String = "Task"

    override def pack: String = packName

    override def attrs: List[Attr] = Nil
  }

}

