package services

import javax.inject._

import models.{Menu, Menus}

/**
  * Created by ubuntu on 17/10/16.
  */
@Singleton
class MenuService @Inject()(menus: Menus) {

  def consultarMenu(id: Int): Menu = {
    menus.consultarMenu(id)
  }

  def traerTodo: Seq[Menu] = {
    menus.traerTodo
  }

  def crearMenu(menu: Menu): Boolean = {
    if (menus.consultarMenu(menu.id) != null)
      menus.modificar(menu)
    else
      menus.crearMenu(menu)
  }
}
