package appAluno.demo.controller;

import appAluno.demo.model.Tarefa;
import appAluno.demo.model.Usuario;
import appAluno.demo.repository.SubtarefaRepository;
import appAluno.demo.repository.TarefaRepository;
import appAluno.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private SubtarefaRepository subtarefaRepository; // Repositório de subtarefas

    @Autowired
    private UsuarioRepository usuarioRepository;

    //(os outros métodos como criarTarefa, getTarefasPendentes, etc.)
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> criarTarefa(@PathVariable Long usuarioId, @RequestBody Map<String, String> payload) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>("Utilizador não encontrado", HttpStatus.NOT_FOUND);
        }
        Tarefa novaTarefa = new Tarefa();
        novaTarefa.setTitulo(payload.get("titulo"));
        novaTarefa.setUsuario(usuarioOpt.get());
        novaTarefa.setConcluida("false");
        return new ResponseEntity<>(tarefaRepository.save(novaTarefa), HttpStatus.CREATED);
    }

    @GetMapping("/pendentes/usuario/{usuarioId}")
    public List<Tarefa> getTarefasPendentes(@PathVariable Long usuarioId) {
        return tarefaRepository.findByUsuarioIdAndConcluida(usuarioId, "false");
    }

    @GetMapping("/concluidas/usuario/{usuarioId}")
    public List<Tarefa> getTarefasConcluidas(@PathVariable Long usuarioId) {
        return tarefaRepository.findByUsuarioIdAndConcluida(usuarioId, "true");
    }

    @PutMapping("/{tarefaId}/concluir")
    public ResponseEntity<Tarefa> concluirTarefa(@PathVariable Long tarefaId) {
        return tarefaRepository.findById(tarefaId).map(tarefa -> {
            tarefa.setConcluida("true");
            if (tarefa.getSubtarefas() != null) {
                tarefa.getSubtarefas().forEach(sub -> sub.setConcluida("true"));
            }
            tarefaRepository.save(tarefa);
            return ResponseEntity.ok(tarefa);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{tarefaId}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long tarefaId) {
        if (tarefaRepository.existsById(tarefaId)) {
            tarefaRepository.deleteById(tarefaId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    //APAGAR TODAS AS TAREFAS
    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Void> apagarTodasAsTarefasDoUsuario(@PathVariable Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            return ResponseEntity.notFound().build();
        }

        //Apaga primeiro todas as subtarefas para evitar erro
        subtarefaRepository.deleteAllByTarefa_UsuarioId(usuarioId);

        //apaga as tarefas
        tarefaRepository.deleteAllByUsuarioId(usuarioId);

        return ResponseEntity.noContent().build();
    }
}

