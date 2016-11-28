package controllers

import javax.inject._

import models.{Domicilio, Usuario}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import services.DomicilioService

/**
  * Created by acamilo.barrera on 27/11/16.
  */
@Singleton
class DomicilioController @Inject()(domicilioService: DomicilioService) extends Controller {
  implicit val domicilioWrites: Writes[Domicilio] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "usuario_email").write[String] and
      (JsPath \ "menu_id").write[String] and
      (JsPath \ "fecha").write[String] and
      (JsPath \ "hora").write[String] and
      (JsPath \ "estado_domicilio_id").write[Int] and
      (JsPath \ "estado_pago_id").write[Int]

    ) (unlift(Domicilio.unapply))

  implicit val domicilioReads: Reads[Domicilio] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "usuario_email").read[String] and
      (JsPath \ "menu_id").read[String] and
      (JsPath \ "fecha").read[String] and
      (JsPath \ "hora").read[String] and
      (JsPath \ "estado_domicilio_id").read[Int] and
      (JsPath \ "estado_pago_id").read[Int]

    ) (Domicilio.apply _)

    def consultaDomicilio(id:Int) = Action{
    val domicilio=domicilioService.consultarDomicilio(id)
    if(domicilio!=null)
      Ok(Json.toJson(domicilio))
    else
      NotFound(Json.obj("status" ->"NotFound", "message" -> ("El domicilio '" + domicilio.id + "' no existe.")))
  }

  def crearDomicilio=Action(BodyParsers.parse.json) { request =>
    val placeResult = request.body.validate[Domicilio]
    placeResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toJson(errors)))
      },
      domicilio => {
        domicilioService.crearDomicilio(domicilio)
        Ok(Json.obj("status" -> "OK", "message" -> ("El domicilio '" + domicilio.id + "' ha sido guardado.")))
      }
    )
  }
    def consultaDomicilios = Action {
      val domicilios = domicilioService.traerTodo
      val json = Json.toJson(domicilios)
      Ok(json)
    }


}
