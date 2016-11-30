package controllers

import javax.inject._

import models.Menu
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import services.MenuService


/**
  * Created by ubuntu on 17/10/16.
  */
@Singleton
class MenuController @Inject()(menuService: MenuService) extends Controller {
  implicit val menuWrites: Writes[Menu] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "restaurante_nit").write[String] and
      (JsPath \ "nombre").write[String] and
      (JsPath \ "descripcion").write[String] and
      (JsPath \ "precio").write[Int] and
        (JsPath \ "url_img").write[String] and
        (JsPath \ "disponible").write[Boolean]

    ) (unlift(Menu.unapply))

  implicit val menuReads: Reads[Menu] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "restaurante_nit").read[String] and
      (JsPath \ "nombre").read[String] and
      (JsPath \ "descripcion").read[String] and
      (JsPath \ "precio").read[Int] and
      (JsPath \ "url_img").read[String] and
      (JsPath \ "disponible").read[Boolean]

    ) (Menu.apply _)

    def consultaMenu(id:Int) = Action{
    val menu=menuService.consultarMenu(id)
    if(menu!=null)
      Ok(Json.toJson(menu))
    else
      NotFound(Json.obj("status" ->"NotFound", "message" -> ("No se encontro el menu "+id)))
  }

  def crearMenu=Action(BodyParsers.parse.json) { request =>
    val placeResult = request.body.validate[Menu]
    placeResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toJson(errors)))
      },
      menu => {
        menuService.crearMenu(menu)
        Ok(Json.obj("status" -> "OK", "message" -> ("El Menu '" + menu.id + "' ha sido guardado.")))
      }
    )
  }
    def consultaMenus = Action {
      val menus = menuService.traerTodo
      val json = Json.toJson(menus)
      Ok(json)
    }


}
