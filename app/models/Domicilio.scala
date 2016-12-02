package models

import java.sql.{Connection, PreparedStatement, ResultSet}
import play.api.db._
import play.api.Play.current
import java.sql.Date
import java.sql.Time

/**
  * Created by acamilo.barrera on 27/11/16.
  */
case class Domicilio(var id: Int, var usuario_email: String, var menu_id: Int, var fecha: String, var hora: String, var estado_domicilio_id: Int, var estado_pago_id: Int)

object DomicilioRepository {
  val Insert = "INSERT INTO domicilio(id,usuario_email,menu_id,fecha,hora,estado_domicilio_id,estado_pago_id) values(?,?,?,?,?,?,?)"
  val Update = "UPDATE domicilio SET id=?,usuario_email=?,menu_id=?,fecha,hora=?,estado_domicilio_id=?,estado_pago_id=?"
  val Select = "SELECT id,usuario_email,menu_id,fecha,hora,estado_domicilio_id,estado_pago_id FROM domicilio ORDER BY id ASC"
  val SelectOne = "SELECT id,usuario_email,menu_id,fecha,hora,estado_domicilio_id,estado_pago_id FROM domicilio WHERE id=?"
}


class Domicilios {

  import DomicilioRepository._

  def consultarDomicilio(id: Int): Domicilio = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = connection.prepareStatement(SelectOne)
      prepareStatement.setInt(1, id)
      val resultSet = prepareStatement.executeQuery()
      var domicilio: Domicilio = null
      while (resultSet.next()) {
        domicilio = resultSetADomicilio(resultSet)
      }
      domicilio
    }
  }

  def crearDomicilio(domicilio: Domicilio): Boolean = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = crearPrepareStatementCrear(connection, domicilio)
      prepareStatement.execute()
    }
  }

  def modificar(domicilio: Domicilio): Boolean = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = crearPrepareStatementModificar(connection, domicilio)
      prepareStatement.execute()
    }
  }


  def traerTodo: Seq[Domicilio] = {
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT id,usuario_email,menu_id,fecha,hora,estado_domicilio_id,estado_pago_id FROM domicilio")

      var domicilios: Seq[Domicilio] = Seq.empty

      while (rs.next) {
        val domicilio = new Domicilio(rs.getInt("id"),
          rs.getString("usuario_email"),
          rs.getInt("menu_id"),
          rs.getString("fecha"),
          rs.getString("hora"),
          rs.getInt("estado_domicilio_id"),
          rs.getInt("estado_pago_id")

        )
        domicilios = domicilios :+ domicilio
      }
      domicilios
    } finally {
      conn.close()
    }

  }

  private def resultSetADomicilio(fila: ResultSet): Domicilio = {
    new Domicilio(fila.getInt("id"),
      fila.getString("usuario_email"),
      fila.getInt("menu_id"),
      fila.getString("fecha"),
      fila.getString("hora"),
      fila.getInt("estado_domicilio_id"),
      fila.getInt("estado_pago_id")
    )
  }

  private def crearPrepareStatementModificar(conexion: Connection, domicilio: Domicilio): PreparedStatement = {
    val preparedStatement = conexion.prepareStatement(Update)
    preparedStatement.setInt(1, domicilio.id)
    preparedStatement.setString(2, domicilio.usuario_email)
    preparedStatement.setInt(3, domicilio.menu_id)
    preparedStatement.setString(4, domicilio.fecha)
    preparedStatement.setString(5, domicilio.hora)
    preparedStatement.setInt(6, domicilio.estado_domicilio_id)
    preparedStatement.setInt(7, domicilio.estado_pago_id)
    preparedStatement
  }

  private def crearPrepareStatementCrear(conexion: Connection, domicilio: Domicilio): PreparedStatement = {
    val preparedStatement = conexion.prepareStatement(Insert)
    var date: Date = Date.valueOf(domicilio.fecha)
    var time: Time = Time.valueOf(domicilio.hora)
    preparedStatement.setInt(1, domicilio.id)
    preparedStatement.setString(2, domicilio.usuario_email)
    preparedStatement.setInt(3, domicilio.menu_id)
    preparedStatement.setDate(4, date)
    preparedStatement.setTime(5, time)
    preparedStatement.setInt(6, domicilio.estado_domicilio_id)
    preparedStatement.setInt(7, domicilio.estado_pago_id)
    preparedStatement
  }
}