package br.com.devaria.kotlin.projects.gerenciador_tarefas.controllers

import br.com.devaria.kotlin.projects.gerenciador_tarefas.dtos.ErroDto
import br.com.devaria.kotlin.projects.gerenciador_tarefas.dtos.LoginDTO
import br.com.devaria.kotlin.projects.gerenciador_tarefas.dtos.LoginResppostaDTO
import br.com.devaria.kotlin.projects.gerenciador_tarefas.extensions.md5
import br.com.devaria.kotlin.projects.gerenciador_tarefas.extensions.toHex
import br.com.devaria.kotlin.projects.gerenciador_tarefas.repositories.UsuarioRepository
import br.com.devaria.kotlin.projects.gerenciador_tarefas.utils.JWTUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/login")
class LoginController(val usuarioRepository: UsuarioRepository) {

    @PostMapping
    fun fazerLogin (@RequestBody dto : LoginDTO ) : ResponseEntity<Any> {
        try {
            if (dto == null || dto.login.isBlank() || dto.login.isEmpty()
                || dto.senha.isBlank() || dto.senha.isEmpty()){
                return ResponseEntity(ErroDto(HttpStatus.BAD_REQUEST.value(),
                    "Parâmetro de entrada invalidos, verifique as informações e tente novamente"),
                    HttpStatus.BAD_REQUEST)
            }
            var usuario = usuarioRepository.findByEmail(dto.login)

            if (usuario == null || usuario.senha != md5(dto.senha).toHex()){
                return ResponseEntity(ErroDto(HttpStatus.BAD_REQUEST.value(),
                    "Usuario ou senha invalidos"),
                    HttpStatus.BAD_REQUEST)
            }

            val token = JWTUtils().gerarToken(usuario.id.toString())

            val usuarioAutenticado = LoginResppostaDTO(usuario.nome, usuario.email, token)
            return ResponseEntity(usuarioAutenticado, HttpStatus.OK)

        }catch (e: Exception){
            return ResponseEntity(ErroDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possivel efetuar o login, tente novamente"),
                HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}