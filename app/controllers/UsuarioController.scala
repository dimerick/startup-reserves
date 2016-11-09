package controllers

import javax.inject._

import models.Usuario
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import services.UsuarioService

/**
  * Created by ubuntu on 17/10/16.
  */
@Singleton
class UsuarioController @Inject()(usuarioService: UsuarioService) extends Controller {
  implicit val usuarioWrites: Writes[Usuario] = (
    (JsPath \ "nombre").write[String] and
      (JsPath \ "apellido").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \ "password").write[String] and
      (JsPath \ "token").write[String]

    ) (unlift(Usuario.unapply))

  implicit val usuarioReads: Reads[Usuario] = (
    (JsPath \ "nombre").read[String] and
      (JsPath \ "apellido").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "token").read[String]

    ) (Usuario.apply _)

    def consultaUsuario(email:String) = Action{
    val usuario=usuarioService.consultarUsuario(email)
    if(usuario!=null)
      Ok(Json.toJson(usuario))
    else
      NotFound(Json.obj("status" ->"NotFound", "message" -> ("No se encontro el usuario "+email)))
  }

  def crearUsuario=Action(BodyParsers.parse.json) { request =>
    val placeResult = request.body.validate[Usuario]
    placeResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      usuario => {
        usuarioService.crearUsuario(usuario)
        Ok(Json.obj("status" -> "OK", "message" -> ("El usuario '" + usuario.email + "' ha sido guardado.")))
      }
    )
  }
    def consultaUsuarios = Action {
      val usuarios = usuarioService.traerTodo
      val json = Json.toJson(usuarios)
      Ok(json)
    }


}
