package br.com.devaria.kotlin.projects.gerenciador_tarefas.models

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class Tarefa(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var nome : String = "",
    var dataPrevisao : LocalDate = LocalDate.MIN,
    var dataConclusao : LocalDate? = null,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario")
    val usuario: Usuario? = null
)