package br.com.devaria.kotlin.projects.gerenciador_tarefas.models

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Entity
import jakarta.persistence.*

@Entity
data class Usuario (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,
    val nome: String = "",
    val email :String = "",
    var senha: String= "",

    @JsonBackReference
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    val tarefas: List<Tarefa> = emptyList()
)