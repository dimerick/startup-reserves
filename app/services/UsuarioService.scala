package services

import javax.inject._

import models.{Usuario, Usuarios}

/**
  * Created by ubuntu on 17/10/16.
  */
@Singleton
class UsuarioService @Inject()(usuarios: Usuarios) {

  def consultarUsuario(email: String): Usuario = {
    usuarios.consultarUsuario(email)
  }

  def traerTodo: Seq[Usuario] = {
    usuarios.traerTodo
  }

  def crearUsuario(usuario: Usuario): Boolean = {
    if (usuarios.consultarUsuario(usuario.email) != null)
      usuarios.modificar(usuario)
    else
      usuarios.crearUsuario(usuario)
  }
}
