package services

import javax.inject._
import models.{Restaurante, Restaurantes}

/**
  * Created by ubuntu on 17/10/16.
  */
@Singleton
class RestauranteService @Inject()(restaurantes: Restaurantes) {

  def alguno(nit: String): Restaurante = {
    restaurantes.alguno(nit)
  }

  def traerTodo: Seq[Restaurante] = {
    restaurantes.traerTodo
  }

  def guardar(restaurante: Restaurante): Boolean = {
    if (restaurantes.alguno(restaurante.nit) != null)
      restaurantes.modificar(restaurante)
    else
      restaurantes.guardar(restaurante)
  }
}
