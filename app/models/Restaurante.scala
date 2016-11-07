package models

import javax.inject._
import play.api.Application
import play.api.db._
import play.api.Play.current
import scala.concurrent.Future

/**
  * Created by ubuntu on 17/10/16.
  */
case class Restaurante(var nit: String, var nombre: String, var ciudad: String, var barrio: String, var direccion: String, var email: String, var telefono: String)


class Restaurantes {


  def traerTodo: Seq[Restaurante] = {
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT nit,nombre,ciudad,barrio,direccion,email,telefono FROM restaurante")

      var restaurantes: Seq[Restaurante] = Seq.empty

      while (rs.next) {
        val restaurante = new Restaurante(rs.getString("nit"),
          rs.getString("nombre"),
          rs.getString("ciudad"),
          rs.getString("barrio"),
          rs.getString("direccion"),
          rs.getString("email"),
          rs.getString("telefono")
        )
        restaurantes = restaurantes :+ restaurante
      }
      restaurantes
    } finally {
      conn.close()
    }

  }
}