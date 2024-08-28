package br.com.devaria.kotlin.projects.gerenciador_tarefas.repositories

import br.com.devaria.kotlin.projects.gerenciador_tarefas.models.Tarefa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TarefaRepository : JpaRepository<Tarefa, Long> {

    @Query(
        "SELECT t FROM Tarefa t " +
        "WHERE t.usuario.id = :idUsuario " +
        "AND (:periodoDe IS NULL OR t.dataPrevisao >= :periodoDe) " +
        "AND (:periodoAte IS NULL OR t.dataConclusao <= :periodoAte) " +
        "AND (:status = 0 OR (:status = 1 AND t.dataConclusao IS NULL) " +
        "OR (:status = 2 AND t.dataConclusao IS NOT NULL))"
    )
    fun findByUsuarioWithFilter(idUsuario: Long, periodoDe: LocalDate?, periodoAte: LocalDate?, status: Int): List<Tarefa>?

}