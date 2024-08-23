package br.com.devaria.kotlin.projects.gerenciador_tarefas.controllers

import br.com.devaria.kotlin.projects.gerenciador_tarefas.dtos.ErroDto
import br.com.devaria.kotlin.projects.gerenciador_tarefas.models.Usuario
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/usuario")
class UsuarioController {

    @PostMapping
    fun criarUsuario(@RequestBody usuario:Usuario) : ResponseEntity<Any>  {
        try {
            val erros = mutableListOf<String>()

            if (usuario == null){
                return ResponseEntity(ErroDto(HttpStatus.BAD_REQUEST.value(),
                    "Parametros de entrados não enviados"),
                    HttpStatus.BAD_REQUEST)
            }

            if (usuario.nome.isNullOrEmpty() || usuario.nome.isBlank() || usuario.nome.length < 3){
                erros.add("Nome inválido")
            }
            if (usuario.email.isNullOrEmpty() || usuario.email.isBlank() || usuario.email.length < 5){
                erros.add("Email inválido")
            }

            if (usuario.senha.isNullOrEmpty() || usuario.senha.isBlank() || usuario.senha.length < 8){
                erros.add("Senha inválida")
            }

            if (erros.size > 0 ){
                return ResponseEntity(ErroDto(HttpStatus.BAD_REQUEST.value(),
                    null, erros),
                    HttpStatus.BAD_REQUEST)
            }

            return ResponseEntity(usuario, HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity(ErroDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possivel cadastrar usuário, tente novamente"),
                HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}