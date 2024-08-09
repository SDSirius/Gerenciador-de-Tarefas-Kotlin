package br.com.devaria.kotlin.projects.gerenciador_tarefas.controllers

import br.com.devaria.kotlin.projects.gerenciador_tarefas.dtos.ErroDto
import br.com.devaria.kotlin.projects.gerenciador_tarefas.dtos.LoginDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class LoginController {

    @PostMapping("/login")
    fun fazerLogin (@RequestBody dto : LoginDTO ) : ResponseEntity<Any> {
        try {
            if (dto == null || dto.login.isNullOrBlank() || dto.login.isBlank()
                || dto.senha.isNullOrBlank() || dto.senha.isBlank()){
                return ResponseEntity(ErroDto(HttpStatus.BAD_REQUEST.value(),
                    "Parâmetro de entrada invalidos, verifique as informações e tente novamente"),
                    HttpStatus.BAD_REQUEST)
            }
            if (dto.login != "Sergiod398@gmail.com" || dto.senha != "SDMA4ever"){
                return ResponseEntity(ErroDto(HttpStatus.METHOD_NOT_ALLOWED.value(),
                    "Usuário não cadastrado"),
                    HttpStatus.METHOD_NOT_ALLOWED)
            }else{
                return ResponseEntity("Usuario Autenticado com sucesso", HttpStatus.OK)
            }

        }catch (e: Exception){
            return ResponseEntity(ErroDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possivel efetuar o login, tente novamente"),
                HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}