package services

import javax.inject._

import models.{Mesa, Mesas}

/**
  * Created by abarrera on 07/11/2016.
  */
@Singleton
class MesaService @Inject()(mesas: Mesas) {
  def alguno(numero:Int,nit: String): Mesa = {
    mesas.alguno(numero,nit)
  }

  def consultarPorNit(nit:String): Seq[Mesa] = {
    mesas.algunos(nit)
  }

  def guardar(mesa: Mesa): Boolean = {
    if (mesas.alguno(mesa.numero, mesa.nit) != null)
      mesas.modificar(mesa)
    else
      mesas.guardar(mesa)
  }
}
