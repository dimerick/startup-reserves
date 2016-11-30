package services

import javax.inject._
import java.sql.Date
import models.{ConsultaReserva, ConsultaReservas}

/**
  * Created by acamilo.barrera on 28/11/16.
  */
@Singleton
class ConsultaReservaService @Inject()(consultaReservas: ConsultaReservas) {

  def traerTodo1(date_ini: Long, date_end : Long): Seq[ConsultaReserva] = {
    consultaReservas.traerTodo1(date_ini, date_end)
  }

}
