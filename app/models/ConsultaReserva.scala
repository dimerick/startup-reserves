package models

import java.sql.{Connection, PreparedStatement, ResultSet}
import play.api.db._
import play.api.Play.current
import java.sql.Date
import java.sql.Time

/**
  * Created by acamilo.barrera on 28/11/16.
  */
case class ConsultaReserva(var id: Int, var fecha: String, var hora: String, var usuario_email: String, var mesa_numero: Int, var restaurante_nit: String, var estado: Int)


object ConsultaReservaRepository {
  val Insert = "INSERT INTO reserva(fecha,hora,usuario_email,mesa_numero,restaurante_nit) values(?,?,?,?,?)"

}


class ConsultaReservas {

  import ConsultaReservaRepository._

  def traerTodo1(date_ini : Long, date_end : Long): Seq[ConsultaReserva] = {
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT id, fecha, hora, usuario_email, mesa_numero, restaurante_nit, estado FROM reserva WHERE fecha BETWEEN '"+date_ini+"' AND '"+date_end+"' ORDER BY id DESC")

      var ConsultaReservas: Seq[ConsultaReserva] = Seq.empty

      while (rs.next) {
        val consultaReserva = new ConsultaReserva(rs.getInt("id"),
          rs.getString("fecha"),
          rs.getString("hora"),
          rs.getString("usuario_email"),
          rs.getInt("mesa_numero"),
          rs.getString("restaurante_nit"),
          rs.getInt("estado")
        )
        ConsultaReservas = ConsultaReservas :+ consultaReserva
      }
      ConsultaReservas
    } finally {
      conn.close()
    }

  }

  private def crearPrepareStatementGuardar(conexion: Connection, consultaReserva: ConsultaReserva): PreparedStatement = {
    val preparedStatement = conexion.prepareStatement(Insert)
    var date: Date = Date.valueOf(consultaReserva.fecha)
    var time: Time = Time.valueOf(consultaReserva.hora)
    preparedStatement.setDate(1, date)
    preparedStatement.setTime(2, time)
    preparedStatement.setString(3, consultaReserva.usuario_email)
    preparedStatement.setInt(4, consultaReserva.mesa_numero)
    preparedStatement.setString(5, consultaReserva.restaurante_nit)
    preparedStatement
  }
}