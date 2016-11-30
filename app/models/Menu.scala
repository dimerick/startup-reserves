package models

import java.sql.{Connection, PreparedStatement, ResultSet}
import play.api.db._
import play.api.Play.current


/**
  * Created by ubuntu on 17/10/16.
  */
case class Menu(var id: Int, var restaurante_nit: String, var nombre: String, var descripcion: String, var precio: Int, var url_img: String, var disponible: Boolean )

object MenuRepository {
  val Insert = "INSERT INTO menu(id,restaurante_nit,nombre,descripcion,precio,url_img,disponible) values(?,?,?,?,?,?,?)"
  val Update = "UPDATE menu SET restaurante_nit=?,nombre=?,descripcion=?,precio=?,url_img=?,disponible=? WHERE id=?"
  val Select = "SELECT id,restaurante_nit,nombre,descripcion,precio,url_img,disponible FROM menu ORDER BY nombre ASC"
  val SelectOne = "SELECT id,restaurante_nit,nombre,descripcion,precio,url_img,disponible FROM menu WHERE id=?"
}


class Menus {

  import MenuRepository._

  def consultarMenu(id: Int): Menu = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = connection.prepareStatement(SelectOne)
      prepareStatement.setInt(1, id)
      val resultSet = prepareStatement.executeQuery()
      var menu: Menu = null
      while (resultSet.next()) {
        menu = resultSetAMenu(resultSet)
      }
      menu
    }
  }

  def crearMenu(menu: Menu): Boolean = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = crearPrepareStatementCrear(connection, menu)
      prepareStatement.execute()
    }
  }

  def modificar(menu: Menu): Boolean = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = crearPrepareStatementModificar(connection, menu)
      prepareStatement.execute()
    }
  }


  def traerTodo: Seq[Menu] = {
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT id,restaurante_nit,nombre,descripcion,precio,url_img,disponible FROM menu")

      var menus: Seq[Menu] = Seq.empty

      while (rs.next) {
        val menu = new Menu(rs.getInt("id"),
          rs.getString("restaurante_nit"),
          rs.getString("nombre"),
          rs.getString("descripcion"),
          rs.getInt("precio"),
          rs.getString("url_img"),
          rs.getBoolean("disponible")

        )
        menus = menus :+ menu
      }
      menus
    } finally {
      conn.close()
    }

  }

  private def resultSetAMenu(fila: ResultSet): Menu = {
    new Menu(fila.getInt("id"),
      fila.getString("restaurante_nit"),
      fila.getString("nombre"),
      fila.getString("descripcion"),
      fila.getInt("precio"),
      fila.getString("url_img"),
      fila.getBoolean("disponible")
    )
  }

  private def crearPrepareStatementModificar(conexion: Connection, menu: Menu): PreparedStatement = {
    val preparedStatement = conexion.prepareStatement(Update)
    preparedStatement.setInt(1, menu.id)
    preparedStatement.setString(2, menu.restaurante_nit)
    preparedStatement.setString(3, menu.nombre)
    preparedStatement.setString(4, menu.descripcion)
    preparedStatement.setInt(5, menu.precio).toString
    preparedStatement.setString(6, menu.url_img)
    preparedStatement.setBoolean(7, menu.disponible)
    preparedStatement
  }

  private def crearPrepareStatementCrear(conexion: Connection, menu: Menu): PreparedStatement = {
    val preparedStatement = conexion.prepareStatement(Insert)
    preparedStatement.setInt(1, menu.id)
    preparedStatement.setString(2, menu.restaurante_nit)
    preparedStatement.setString(3, menu.nombre)
    preparedStatement.setString(4, menu.descripcion)
    preparedStatement.setInt(5,menu.precio).toString
    preparedStatement.setString(6, menu.url_img)
    preparedStatement.setBoolean(7, menu.disponible)
    preparedStatement
  }
}