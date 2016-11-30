package controllers

import java.sql.Date

import models.ConsultaReserva
import javax.inject._

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import services.ConsultaReservaService

/**
  * Created by acamilo.barrera on 28/11/16.
  */
@Singleton
class ConsultaReservaController @Inject()(consultaReservaService: ConsultaReservaService) extends Controller {
  implicit val reservaWrites: Writes[ConsultaReserva] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "fecha").write[String] and
      (JsPath \ "hora").write[String] and
      (JsPath \ "usuario_email").write[String] and
      (JsPath \ "mesa_numero").write[Int] and
      (JsPath \ "restaurante_nit").write[String] and
      (JsPath \ "estado").write[Int]
    ) (unlift(ConsultaReserva.unapply))

  implicit val reservaReads: Reads[ConsultaReserva] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "fecha").read[String] and
      (JsPath \ "hora").read[String] and
      (JsPath \ "usuario_email").read[String] and
      (JsPath \ "mesa_numero").read[Int] and
      (JsPath \ "restaurante_nit").read[String] and
      (JsPath \ "estado").read[Int]
    ) (ConsultaReserva.apply _)


  def consultarReservas(date_ini: Long, date_end :Long) = Action {
    val reservas = consultaReservaService.traerTodo1(date_ini, date_end )
    val json = Json.toJson(reservas)
    Ok(json)
  }
}
