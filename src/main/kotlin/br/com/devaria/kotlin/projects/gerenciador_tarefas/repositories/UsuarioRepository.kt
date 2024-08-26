package br.com.devaria.kotlin.projects.gerenciador_tarefas.repositories

import br.com.devaria.kotlin.projects.gerenciador_tarefas.models.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository : JpaRepository<Usuario, Long>{
    fun findByEmail(email:String) : Usuario?
}