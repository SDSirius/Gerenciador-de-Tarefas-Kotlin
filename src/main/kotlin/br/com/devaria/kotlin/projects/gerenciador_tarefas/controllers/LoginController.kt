package br.com.devaria.kotlin.projects.gerenciador_tarefas.controllers

import br.com.devaria.kotlin.projects.gerenciador_tarefas.dtos.ErroDto
import br.com.devaria.kotlin.projects.gerenciador_tarefas.dtos.LoginDTO
import br.com.devaria.kotlin.projects.gerenciador_tarefas.dtos.LoginResppostaDTO
import br.com.devaria.kotlin.projects.gerenciador_tarefas.utils.JWTUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/login")
class LoginController {

    private val LOGIN_TESTE ="admin@admin.com"
    private val SENHA_TESTE ="Admin1234@"
    private val idUsuario =1

    @PostMapping
    fun fazerLogin (@RequestBody dto : LoginDTO ) : ResponseEntity<Any> {
        try {
            if (dto == null || dto.login.isBlank() || dto.login.isEmpty()
                || dto.senha.isBlank() || dto.senha.isEmpty()
                || dto.login !=LOGIN_TESTE || dto.senha != SENHA_TESTE){
                return ResponseEntity(ErroDto(HttpStatus.BAD_REQUEST.value(),
                    "Parâmetro de entrada invalidos, verifique as informações e tente novamente"),
                    HttpStatus.BAD_REQUEST)
            }

            val token = JWTUtils().gerarToken(idUsuario.toString())

            val usuarioTeste = LoginResppostaDTO("UsuarioTeste", LOGIN_TESTE, token)
            return ResponseEntity(usuarioTeste, HttpStatus.OK)

        }catch (e: Exception){
            return ResponseEntity(ErroDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possivel efetuar o login, tente novamente"),
                HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}