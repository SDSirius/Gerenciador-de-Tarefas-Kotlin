package br.com.devaria.kotlin.projects.gerenciador_tarefas.models

import jakarta.persistence.Entity
import jakarta.persistence.*

@Entity
data class Usuario (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,
    val nome: String = "",
    val email :String = "",
    val senha: String= ""
)