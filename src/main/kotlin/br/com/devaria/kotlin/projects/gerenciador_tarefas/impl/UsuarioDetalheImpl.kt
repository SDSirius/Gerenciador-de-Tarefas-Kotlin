package br.com.devaria.kotlin.projects.gerenciador_tarefas.impl

import br.com.devaria.kotlin.projects.gerenciador_tarefas.models.Usuario
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UsuarioDetalheImpl(private val usuario : Usuario) : UserDetails {
    override fun getAuthorities() = mutableListOf<GrantedAuthority>()

    override fun getPassword() = usuario.senha

    override fun getUsername() = usuario.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}