package services

import javax.inject._

import models.{Domicilio, Domicilios}

/**
  * Created by acamilo.barrera on 27/11/16.
  */
@Singleton
class DomicilioService @Inject()(domicilios: Domicilios) {

  def consultarDomicilio(id: Int): Domicilio = {
    domicilios.consultarDomicilio(id)
  }

  def traerTodo: Seq[Domicilio] = {
    domicilios.traerTodo
  }

  def crearDomicilio(domicilio: Domicilio): Boolean = {
    if (domicilios.consultarDomicilio(domicilio.id) != null)
      domicilios.modificar(domicilio)
    else
      domicilios.crearDomicilio(domicilio)
  }
}
