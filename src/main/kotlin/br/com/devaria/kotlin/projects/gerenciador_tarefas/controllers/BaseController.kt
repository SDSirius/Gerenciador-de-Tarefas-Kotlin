package br.com.devaria.kotlin.projects.gerenciador_tarefas.controllers

import br.com.devaria.kotlin.projects.gerenciador_tarefas.models.Usuario
import br.com.devaria.kotlin.projects.gerenciador_tarefas.repositories.UsuarioRepository
import br.com.devaria.kotlin.projects.gerenciador_tarefas.utils.JWTUtils
import org.springframework.data.repository.findByIdOrNull

open class BaseController(val usuarioRepository: UsuarioRepository)  {
    fun lerToken(authorization:String) :Usuario{
        val token = authorization.substring(7)
        var userIdStr = JWTUtils().getUsuarioId(token)

        if(userIdStr == null || userIdStr.isNullOrEmpty() || userIdStr.isBlank()){
            throw IllegalArgumentException("Você não tem acesso a este recurso!")
        }

        var usuario = usuarioRepository.findByIdOrNull(userIdStr.toLong())

        if (usuario== null){
            throw IllegalArgumentException("Usuario não encontrado!")

        }

        return usuario
    }
}