package models

import java.sql.{Connection, PreparedStatement, ResultSet}

import play.api.db.DB
import play.api.Play.current

/**
  * Created by abarrera on 07/11/2016.
  */
case class Mesa(var numero: Int, var nit: String, var capacidad: Int)

object MesaRepository {
  val Insert = "INSERT INTO mesa(numero, restaurante_nit, capacidad) VALUES (?, ?, ?)"
  val Update = "UPDATE mesa SET  capacidad=? WHERE numero=? AND restaurante_nit=?"
  val SelectByNit = "SELECT numero, restaurante_nit, capacidad FROM mesa WHERE restaurante_nit = ? ORDER BY numero ASC"
  val SelectOne = "SELECT numero, restaurante_nit, capacidad FROM mesa WHERE numero = ? AND restaurante_nit = ?"
}

class Mesas {

  import MesaRepository._


  def algunos(nit: String): Seq[Mesa] = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = connection.prepareStatement(SelectByNit)
      prepareStatement.setString(1, nit)
      val resultSet = prepareStatement.executeQuery()

      var mesas: Seq[Mesa] = Seq.empty
      while (resultSet.next()) {
        val mesa = resultSetAMesa(resultSet)
        mesas = mesas :+ mesa
      }
      mesas
    }
  }

  def alguno(numero: Int, nit: String): Mesa = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = connection.prepareStatement(SelectOne)
      prepareStatement.setInt(1, numero)
      prepareStatement.setString(2, nit)

      val resultSet = prepareStatement.executeQuery()
      var mesa: Mesa = null

      while (resultSet.next()) {
        mesa = resultSetAMesa(resultSet)
      }
      mesa
    }
  }

  def guardar(mesa: Mesa): Boolean = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = crearPrepareStatementGuardar(connection, mesa)
      prepareStatement.execute()
    }
  }


  def modificar(mesa: Mesa): Boolean = {
    DB.withConnection() { connection =>
      val prepareStatement: PreparedStatement = crearPrepareStatementModificar(connection, mesa)
      prepareStatement.execute()
    }
  }

  private def resultSetAMesa(fila: ResultSet): Mesa = {
    new Mesa(fila.getInt("numero"),
      fila.getString("restaurante_nit"),
      fila.getInt("capacidad")
    )
  }

  private def crearPrepareStatementGuardar(connection: Connection, mesa: Mesa): PreparedStatement = {
    val preparedStatement = connection.prepareStatement(Insert)

    preparedStatement.setInt(1, mesa.numero)
    preparedStatement.setString(2, mesa.nit)
    preparedStatement.setInt(3, mesa.capacidad)

    preparedStatement
  }

  def crearPrepareStatementModificar(connection: Connection, mesa: Mesa): PreparedStatement = {
    val preparedStatement = connection.prepareStatement(Update)

    preparedStatement.setInt(1, mesa.capacidad)
    preparedStatement.setInt(2, mesa.numero)
    preparedStatement.setString(3, mesa.nit)

    preparedStatement
  }
}

