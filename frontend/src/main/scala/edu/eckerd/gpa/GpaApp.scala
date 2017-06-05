package edu.eckerd.gpa

import fs2._
import fs2.util.Async
import org.scalajs.dom.document

import scala.concurrent.duration._
import scala.scalajs.js.JSApp
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

object GpaApp extends JSApp {

  def main(): Unit = {
    implicit val scheduler = Scheduler.default
    implicit val strategy = Strategy.default
    stream.run.unsafeRunAsync(_ => ())
  }

  def stream(implicit strategy: Strategy, scheduler: Scheduler, f: Async[Task]): Stream[Task, Unit] = {
    Stream.eval(setTitle("Gpa App")) >>
    Stream.eval(gradeInputBox(scalatags.JsDom.all.div().render)).map(document.body.appendChild(_))
  }

  def setTitle(title: String) : Task[Unit] = {
    Task.delay(document.title = title)
  }

  def gradeInputBox(appendTarget: dom.html.Div) = {
    import scalatags.JsDom.all._

    val listings = Seq(
      "A",
      "A-",
      "B+",
      "B-",
      "C+",
      "C",
      "C-",
      "D+",
      "D",
      "D-",
      "F"
    )

    Task.delay {

      val box = input(
        `type` := "text",
        placeholder := "Grade",
        list := "grades"
      ).render

      def renderListings = {
        import scalatags.JsDom.all._
        datalist(
          id := "grades",
          for {
            fruit <- listings
            if fruit.toLowerCase.startsWith(
              box.value.toLowerCase
            )
          } yield option(value := fruit)
        ).render
      }

      val output = div(renderListings).render

      box.onkeyup = (e: dom.Event) => {
        output.innerHTML = ""
        output.appendChild(renderListings)
      }


      appendTarget.appendChild(
        div(
          div(box),
          div(output)
        ).render
      )
    }
  }



}








