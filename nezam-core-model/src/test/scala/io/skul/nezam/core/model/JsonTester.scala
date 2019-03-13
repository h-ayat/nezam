package io.skul.nezam.core.model

import org.scalatest.{FlatSpec, Matchers}
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.json4s.jackson.Serialization

/**
  * @author S.Hosein Ayat
  */
class JsonTester extends FlatSpec with Matchers {


  "json" should "parse base case classes" in {
    implicit val formats: AnyRef with Formats = Serialization.formats(ShortTypeHints(List(classOf[Cat], classOf[Dog])))
    val cat = Cat("Iraj", "Tayebi")
    val dog = Dog("Arjang")
    val list = List[Animal](cat, dog)
    val jsString = Serialization.write(list)
    val outputList = JsonMethods.parse(jsString).extract[List[Animal]]
    outputList shouldBe list
  }

  it should "parse enum objects" in {
    implicit val formats: Formats = TaskJsonFormats.taskFormat
    val t = Task("id" , None, "title" , "Desc" , Nil, None, TaskState.BackLog)
    val json = Serialization.write(t)
    println(json)
    val ob = JsonMethods.parse(json).extract[Task]
    ob shouldBe t
  }
}

sealed trait Animal

case class Cat(name: String, lastName: String) extends Animal

case class Dog(name: String) extends Animal