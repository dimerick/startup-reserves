package controllers

import javax.inject.{Inject, Singleton}

import models.Mesa
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{Action, BodyParsers, Controller}
import services.MesaService

/**
  * Created by abarrera on 07/11/2016.
  */
@Singleton
class MesaController @Inject()(mesaService: MesaService) extends Controller {
  implicit val mesaWrites: Writes[Mesa] = (
    (JsPath \ "numero").write[Int] and
      (JsPath \ "nit").write[String] and
      (JsPath \ "capacidad").write[Int]
    ) (unlift(Mesa.unapply))

  implicit val mesaReads: Reads[Mesa] = (
    (JsPath \ "numero").read[Int] and
      (JsPath \ "nit").read[String] and
      (JsPath \ "capacidad").read[Int]
    ) (Mesa.apply _)

  def consultarMesasPorNit(nit: String) = Action {
    val mesas = mesaService.consultarPorNit(nit)
    val json = Json.toJson(mesas)
    Ok(json)
  }

  def consultarMesa(numero: Int, nit: String) = Action {
    val mesa = mesaService.alguno(numero, nit)
    if (mesa != null)
      Ok(Json.toJson(mesa))
    else
      NotFound(Json.obj("status" -> "NotFound", "message" -> ("No se encontro la mesa " + nit + "(" + numero + ")")))
  }

  def guardar = Action(BodyParsers.parse.json) { request =>
    val placeResult = request.body.validate[Mesa]
    placeResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toJson(errors)))
      },
      mesa => {
        mesaService.guardar(mesa)
        Ok(Json.obj("status" -> "OK", "message" -> ("Mesa " + mesa.nit + "(" + mesa.numero + ") guardado.")))
      }
    )
  }
}
