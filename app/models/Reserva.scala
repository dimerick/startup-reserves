package models

import java.sql.{Connection, PreparedStatement, ResultSet}
import play.api.db._
import play.api.Play.current
import java.sql.Date
import java.sql.Time

/**
  * Created by ubuntu on 17/10/16.
  */
case class  Reserva(var id: Int, var fecha: String, var hora: String, var usuario_email: String, var mesa_numero: Int, var restaurante_nit: String, var estado: Int)


object ReservaRepository {
  val Insert = "INSERT INTO reserva(fecha,hora,usuario_email,mesa_numero,restaurante_nit) values(?,?,?,?,?)"

}


class Reservas {

  import ReservaRepository._

  /*
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
*/
  def guardar(reserva: Reserva): Boolean = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = crearPrepareStatementGuardar(connection, reserva)
        prepareStatement.execute()
    }
  }

  def ultimaReservaUsuario(email: String): Int = {
    val con = DB.getConnection()
    val sta = con.createStatement()
    val result = sta.executeQuery("SELECT id FROM reserva WHERE usuario_email='"+email+"' ORDER BY id DESC LIMIT 1")
    result.next()
    val id = result.getInt("id")
    con.close()
    return id
  }

  def traerTodo: Seq[Reserva] = {
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT id, fecha, hora, usuario_email, mesa_numero, restaurante_nit, estado FROM reserva ORDER BY id DESC")

      var reservas: Seq[Reserva] = Seq.empty

      while (rs.next) {
        val reserva = new Reserva(rs.getInt("id"),
          rs.getString("fecha"),
          rs.getString("hora"),
          rs.getString("usuario_email"),
          rs.getInt("mesa_numero"),
          rs.getString("restaurante_nit"),
          rs.getInt("estado")
        )
        reservas = reservas :+ reserva
      }
      reservas
    } finally {
      conn.close()
    }

  }

  private def crearPrepareStatementGuardar(conexion: Connection, reserva: Reserva): PreparedStatement = {
    val preparedStatement = conexion.prepareStatement(Insert)
    var date: Date = Date.valueOf(reserva.fecha)
    var time: Time = Time.valueOf(reserva.hora)
    preparedStatement.setDate(1, date)
    preparedStatement.setTime(2, time)
    preparedStatement.setString(3, reserva.usuario_email)
    preparedStatement.setInt(4, reserva.mesa_numero)
    preparedStatement.setString(5, reserva.restaurante_nit)
    preparedStatement
  }
}