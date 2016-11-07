package controllers

import javax.inject._

import models.Restaurante
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import services.RestauranteService

/**
  * Created by ubuntu on 17/10/16.
  */
@Singleton
class RestauranteController @Inject()(restauranteService: RestauranteService) extends Controller {
  implicit val restauranteWrites: Writes[Restaurante] = (
    (JsPath \ "nit").write[String] and
      (JsPath \ "nombre").write[String] and
      (JsPath \ "ciudad").write[String] and
      (JsPath \ "barrio").write[String] and
      (JsPath \ "direccion").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \ "telefono").write[String]
    ) (unlift(Restaurante.unapply))

  implicit val restauranteReads: Reads[Restaurante] = (
    (JsPath \ "nit").read[String] and
      (JsPath \ "nombre").read[String] and
      (JsPath \ "ciudad").read[String] and
      (JsPath \ "barrio").read[String] and
      (JsPath \ "direccion").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "telefono").read[String]
    ) (Restaurante.apply _)

  def consultarRestaurantes = Action {
    val restaurantes = restauranteService.traerTodo
    val json = Json.toJson(restaurantes)
    Ok(json)
  }

  def consultarRestaurante(nit:String) = Action{
    val restaurante=restauranteService.alguno(nit)
    if(restaurante!=null)
      Ok(Json.toJson(restaurante))
    else
      NotFound(Json.obj("status" ->"NotFound", "message" -> ("No se encontro el restaurante "+nit)))
  }

  def guardar=Action(BodyParsers.parse.json){ request=>
    val placeResult=request.body.validate[Restaurante]
    placeResult.fold(
      errors=>{
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
      },
      restaurante =>{
        restauranteService.guardar(restaurante)
        Ok(Json.obj("status" ->"OK", "message" -> ("Restaurante '"+restaurante.nit+"' guardado.") ))
      }
    )
  }
}
