package models

import java.sql.{Connection, PreparedStatement, ResultSet}
import play.api.db._
import play.api.Play.current

/**
  * Created by ubuntu on 17/10/16.
  */
case class Restaurante(var nit: String, var nombre: String, var ciudad: String, var barrio: String, var direccion: String, var email: String, var telefono: String)

object RestauranteRepository {
  val Insert = "INSERT INTO restaurante(nit,nombre,ciudad,barrio,direccion,email,telefono) values(?,?,?,?,?,?,?)"
  val Update = "UPDATE restaurante SET nombre=?,ciudad=?,barrio=?,direccion=?,email=?,telefono=? WHERE nit= ?"
  val Select = "SELECT nit,nombre,ciudad,barrio,direccion,email,telefono FROM restaurante ORDER BY nombre ASC"
  val SelectOne = "SELECT nit,nombre,ciudad,barrio,direccion,email,telefono FROM restaurante WHERE nit= ?"
}


class Restaurantes {

  import RestauranteRepository._

  def alguno(nit: String): Restaurante = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = connection.prepareStatement(SelectOne)
      prepareStatement.setString(1, nit)
      val resultSet = prepareStatement.executeQuery()
      var restaurante: Restaurante = null
      while (resultSet.next()) {
        restaurante = resultSetARestarurante(resultSet)
      }
      restaurante
    }
  }

  def guardar(restaurante: Restaurante): Boolean = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = crearPrepareStatementGuardar(connection, restaurante)
      prepareStatement.execute()
    }
  }

  def modificar(restaurante: Restaurante): Boolean = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = crearPrepareStatementModificar(connection, restaurante)
      prepareStatement.execute()
    }
  }


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

  private def resultSetARestarurante(fila: ResultSet): Restaurante = {
    new Restaurante(fila.getString("nit"),
      fila.getString("nombre"),
      fila.getString("ciudad"),
      fila.getString("barrio"),
      fila.getString("direccion"),
      fila.getString("email"),
      fila.getString("telefono")
    )
  }

  private def crearPrepareStatementModificar(conexion: Connection, restaurante: Restaurante): PreparedStatement = {
    val preparedStatement = conexion.prepareStatement(Update)
    preparedStatement.setString(1, restaurante.nombre)
    preparedStatement.setString(2, restaurante.ciudad)
    preparedStatement.setString(3, restaurante.barrio)
    preparedStatement.setString(4, restaurante.direccion)
    preparedStatement.setString(5, restaurante.email)
    preparedStatement.setString(6, restaurante.telefono)
    preparedStatement.setString(7, restaurante.nit)
    preparedStatement
  }

  private def crearPrepareStatementGuardar(conexion: Connection, restaurante: Restaurante): PreparedStatement = {
    val preparedStatement = conexion.prepareStatement(Insert)
    preparedStatement.setString(1, restaurante.nit)
    preparedStatement.setString(2, restaurante.nombre)
    preparedStatement.setString(3, restaurante.ciudad)
    preparedStatement.setString(4, restaurante.barrio)
    preparedStatement.setString(5, restaurante.direccion)
    preparedStatement.setString(6, restaurante.email)
    preparedStatement.setString(7, restaurante.telefono)
    preparedStatement
  }
}