package br.com.devaria.kotlin.projects.gerenciador_tarefas.filters

import br.com.devaria.kotlin.projects.gerenciador_tarefas.authorization
import br.com.devaria.kotlin.projects.gerenciador_tarefas.bearer
import br.com.devaria.kotlin.projects.gerenciador_tarefas.impl.UsuarioDetalheImpl
import br.com.devaria.kotlin.projects.gerenciador_tarefas.models.Usuario
import br.com.devaria.kotlin.projects.gerenciador_tarefas.repositories.UsuarioRepository
import br.com.devaria.kotlin.projects.gerenciador_tarefas.utils.JWTUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class JWTAuthorizationFilter(authenticationManager: AuthenticationManager, val jwtUtils: JWTUtils,val usuarioRepository: UsuarioRepository)
    : BasicAuthenticationFilter(authenticationManager) {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader = request.getHeader(authorization)
        if(authorizationHeader != null && authorizationHeader.startsWith(bearer)){
            val autorizado = getAuthentication(authorizationHeader)
            SecurityContextHolder.getContext().authentication = autorizado
        }

        chain.doFilter(request, response)
    }

    private fun getAuthentication(authorization: String): UsernamePasswordAuthenticationToken {
        val token = authorization.substring(7)
        if (jwtUtils.isTokenValid(token)){
            val idString = jwtUtils.getUsuarioId(token)
            if (!idString.isNullOrEmpty() && !idString.isBlank()){
                val usuario = usuarioRepository.findByIdOrNull(idString.toLong()) ?: throw UsernameNotFoundException("Usuário não encontrado!")
                val usuarioImpl = UsuarioDetalheImpl(usuario)
                return UsernamePasswordAuthenticationToken(usuarioImpl, null, usuarioImpl.authorities)
            }
        }
        throw UsernameNotFoundException(
            "Token informado não válido ou não contém userInfo"
        )
    }
}