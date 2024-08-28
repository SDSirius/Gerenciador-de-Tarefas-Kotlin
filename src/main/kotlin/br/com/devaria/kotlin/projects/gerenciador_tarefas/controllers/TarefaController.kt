package br.com.devaria.kotlin.projects.gerenciador_tarefas.controllers

import br.com.devaria.kotlin.projects.gerenciador_tarefas.dtos.ErroDto
import br.com.devaria.kotlin.projects.gerenciador_tarefas.dtos.SucessoDto
import br.com.devaria.kotlin.projects.gerenciador_tarefas.models.Tarefa
import br.com.devaria.kotlin.projects.gerenciador_tarefas.repositories.TarefaRepository
import br.com.devaria.kotlin.projects.gerenciador_tarefas.repositories.UsuarioRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("api/tarefa")
class TarefaController(usuarioRepository: UsuarioRepository, val tarefaRepository: TarefaRepository) : BaseController(usuarioRepository) {

    @GetMapping
    fun ListarTarefas (@RequestHeader("Authorization") authorization : String,
        @RequestParam periodoDe: Optional<String>,
        @RequestParam periodoAte:Optional<String>,
        @RequestParam status: Optional<Int>): ResponseEntity<Any> {

        try {
            val usuario = lerToken(authorization)
            var periodoDeDt = if(periodoDe.isPresent && periodoDe.get().isNotEmpty()){
                LocalDate.parse(periodoDe.get())
            }else{
                null
            }
            var periodoAteDt = if(periodoAte.isPresent && periodoAte.get().isNotEmpty()){
                LocalDate.parse(periodoAte.get())
            }else{
                null
            }
            var statusInt = if (status.isPresent){
                status.get()
            }else{
                0
            }

            val resultado = tarefaRepository.findByUsuarioWithFilter(usuario.id, periodoDeDt, periodoAteDt, statusInt)
            return ResponseEntity(resultado, HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity(ErroDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Não foi possivel listar suas tarefas"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    @PostMapping
    fun AdicionarTarefa(@RequestBody req: Tarefa, @RequestHeader("Authorization") authorization : String): ResponseEntity<Any>{
        try {
            var usuario = lerToken(authorization)
            var erros = mutableListOf<String>()
            if (req == null ){
                erros.add("Tarefa Não encontrada")
            }else{
                if (req.nome.isNullOrEmpty() || req.nome.isBlank() || req.nome.length < 4){
                    erros.add("Nome de tarefa Inválido")

                }
                if (req.dataPrevisao.isBefore( LocalDate.now() )){
                    erros.add("Data de previsão não pode ser menor que hoje")
                }
            }

            if (erros.size > 0 ){
                return ResponseEntity(ErroDto(HttpStatus.BAD_REQUEST.value(), erros = erros), HttpStatus.BAD_REQUEST)
            }

            val tarefa = Tarefa(
                nome = req.nome,
                dataPrevisao = req.dataPrevisao,
                usuario = usuario
            )

            tarefaRepository.save(tarefa)

            return ResponseEntity(SucessoDto("Tarefa cadastrada com sucesso!"), HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity(ErroDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Não foi possivel adicionar a tarefa, tente novamente"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    fun DeletarTarefa(@PathVariable id: Long, @RequestHeader("Authorization")authorization:String ) : ResponseEntity<Any>{
        try {
            val usuario = lerToken((authorization))
            val tarefa = tarefaRepository.findByIdOrNull(id)
            if (tarefa == null|| tarefa.usuario?.id != usuario.id){
                return ResponseEntity(ErroDto(HttpStatus.BAD_REQUEST.value(),"Tarefa Informada não existe"),HttpStatus.BAD_REQUEST)
            }
            tarefaRepository.delete(tarefa)

            return ResponseEntity(SucessoDto("Tarefa removida com sucesso!"), HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity(ErroDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Não foi possivel remover a tarefa, tente novamente"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/{id}")
    fun AtualizarTarefa(
        @PathVariable id: Long,
        @RequestHeader("Authorization")authorization:String,
        @RequestBody updateModel: Tarefa ) : ResponseEntity<Any>{

        try {
            var erros = mutableListOf<String>()
            val usuario = lerToken((authorization))
            val tarefa = tarefaRepository.findByIdOrNull(id)

            if (usuario == null || tarefa == null){
                return ResponseEntity(ErroDto(HttpStatus.BAD_REQUEST.value(),"Tarefa Informada não existe"),HttpStatus.BAD_REQUEST)
            }

            if (updateModel == null){
                erros.add("Favor enviar os dados que deseja atualizar!")
            }else{
                if (!updateModel.nome.isNullOrEmpty() && !updateModel.nome.isBlank()
                    && updateModel.nome.length < 4){
                    erros.add("Nome inválido")
                }

                if (updateModel.dataConclusao != null && updateModel.dataConclusao == LocalDate.MIN){
                    erros.add("Data de conclusão inválida")
                }
            }
            if (erros.size > 0 ){
                return ResponseEntity(ErroDto(HttpStatus.BAD_REQUEST.value(), erros = erros), HttpStatus.BAD_REQUEST)
            }

            if (!updateModel.nome.isNullOrEmpty() && !updateModel.nome.isBlank()){
                tarefa.nome = updateModel.nome
            }

            if (!updateModel.dataPrevisao.isBefore(LocalDate.now())){
                tarefa.dataPrevisao = updateModel.dataPrevisao
            }

            if (updateModel.dataConclusao != null && updateModel.dataConclusao!= LocalDate.MIN ){
                tarefa.dataConclusao = updateModel.dataConclusao
            }

            tarefaRepository.save(tarefa)

            return ResponseEntity(SucessoDto("Tarefa atualizada com sucesso!"), HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity(ErroDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Não foi possivel atualizar tarefa, tente novamente"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}