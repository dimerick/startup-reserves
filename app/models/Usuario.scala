package models

import java.sql.{Connection, PreparedStatement, ResultSet}
import play.api.db._
import play.api.Play.current

/**
  * Created by ubuntu on 17/10/16.
  */
case class Usuario(var nombre: String, var apellido: String, var email: String, var password: String, var token: String)

object UsuarioRepository {
  val Insert = "INSERT INTO usuario(nombre,apellido,email,password,remember_token) values(?,?,?,?,?)"
  val Update = "UPDATE usuario SET nombre=?,apellido=?,remember_token=? WHERE email=?"
  val Select = "SELECT nombre,apellido,email,password,remember_token FROM usuario ORDER BY apellido ASC"
  val SelectOne = "SELECT nombre,apellido,email,password,remember_token FROM usuario WHERE email=?"
}


class Usuarios {

  import UsuarioRepository._

  def consultarUsuario(email: String): Usuario = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = connection.prepareStatement(SelectOne)
      prepareStatement.setString(1, email)
      val resultSet = prepareStatement.executeQuery()
      var usuario: Usuario = null
      while (resultSet.next()) {
        usuario = resultSetAUsuario(resultSet)
      }
      usuario
    }
  }

  def crearUsuario(usuario: Usuario): Boolean = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = crearPrepareStatementCrear(connection, usuario)
      prepareStatement.execute()
    }
  }

  def modificar(usuario: Usuario): Boolean = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = crearPrepareStatementModificar(connection, usuario)
      prepareStatement.execute()
    }
  }


  def traerTodo: Seq[Usuario] = {
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT nombre,apellido,email,password,remember_token FROM usuario")

      var usuarios: Seq[Usuario] = Seq.empty

      while (rs.next) {
        val usuario = new Usuario(rs.getString("nombre"),
          rs.getString("apellido"),
          rs.getString("email"),
          rs.getString("password"),
          rs.getString("remember_token")

        )
        usuarios = usuarios :+ usuario
      }
      usuarios
    } finally {
      conn.close()
    }

  }

  private def resultSetAUsuario(fila: ResultSet): Usuario = {
    new Usuario(fila.getString("email"),
      fila.getString("nombre"),
      fila.getString("apellido"),
      fila.getString("password"),
      fila.getString("remember_token")
    )
  }

  private def crearPrepareStatementModificar(conexion: Connection, usuario: Usuario): PreparedStatement = {
    val preparedStatement = conexion.prepareStatement(Update)
    preparedStatement.setString(1, usuario.nombre)
    preparedStatement.setString(2, usuario.apellido)
    preparedStatement.setString(4, usuario.email)
    preparedStatement.setString(3, usuario.token)
    preparedStatement
  }

  private def crearPrepareStatementCrear(conexion: Connection, usuario: Usuario): PreparedStatement = {
    val preparedStatement = conexion.prepareStatement(Insert)
    preparedStatement.setString(1, usuario.nombre)
    preparedStatement.setString(2, usuario.apellido)
    preparedStatement.setString(3, usuario.email)
    preparedStatement.setString(4, usuario.password)
    preparedStatement.setString(5, usuario.token)
    preparedStatement
  }
}