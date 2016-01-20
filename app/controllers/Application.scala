package controllers

import models.Task
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._

/*
  Action is a function that handles a request and generates a result (to be sent to client).
  Result value is representing the HTTP response to send to the web client.
    In example 'Ok' constructs a 200 OK response containing text/plain response body.
  There are several methods to create an Action value.
*/

// A Controller is nothing more than a singleton object that generates Action values.
class Application extends Controller {

  // The simplest use case for defining an action generator is a method with no parameters that returns an Action value.
  def index = Action {
    Redirect(routes.Application.tasks)
    //Ok("Hello in the World!")
    //This is the simplest way to create an Action, but we don’t get a reference to the incoming request. It is often useful to access the HTTP request calling this Action.
  }

  def tasks = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        Task.create(label)
        Redirect(routes.Application.tasks)
      }
    )
  }

  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

  val taskForm = Form("label" -> nonEmptyText)

}

