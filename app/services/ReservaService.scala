package services

import javax.inject._

import models.{Reserva, Reservas}

/**
  * Created by ubuntu on 17/10/16.
  */
@Singleton
class ReservaService @Inject()(reservas: Reservas) {

  def traerTodo: Seq[Reserva] = {
    reservas.traerTodo
  }

  def guardar(reserva: Reserva): Boolean = {
    reservas.guardar(reserva)
  }

  def ultimaReservaUsuario(email: String): Int = {
    reservas.ultimaReservaUsuario(email)
  }
}
