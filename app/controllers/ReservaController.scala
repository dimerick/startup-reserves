package controllers

import models.Reserva
import javax.inject._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import services.ReservaService

/**
  * Created by ubuntu on 17/10/16.
  */
@Singleton
class ReservaController @Inject()(reservaService: ReservaService) extends Controller {
  implicit val reservaWrites: Writes[Reserva] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "fecha").write[String] and
      (JsPath \ "hora").write[String] and
      (JsPath \ "usuario_email").write[String] and
      (JsPath \ "mesa_numero").write[Int] and
      (JsPath \ "restaurante_nit").write[String] and
      (JsPath \ "estado").write[Int]
    ) (unlift(Reserva.unapply))

  implicit val reservaReads: Reads[Reserva] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "fecha").read[String] and
      (JsPath \ "hora").read[String] and
      (JsPath \ "usuario_email").read[String] and
      (JsPath \ "mesa_numero").read[Int] and
      (JsPath \ "restaurante_nit").read[String] and
      (JsPath \ "estado").read[Int]
    ) (Reserva.apply _)


  def consultarReservas = Action {
    val reservas = reservaService.traerTodo
    val json = Json.toJson(reservas)
    Ok(json)
  }

  def guardar=Action(BodyParsers.parse.json){ request=>
    val placeResult=request.body.validate[Reserva]
    placeResult.fold(
      errors=>{
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
      },
      reserva =>{
        reservaService.guardar(reserva)
        val id = reservaService.ultimaReservaUsuario(reserva.usuario_email)
        Ok(Json.obj("status" ->"OK", "message" -> ("Se ha registrado la reserva exitosamente con el id: "+id)))
      }
    )
  }
}
