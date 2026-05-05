package appAluno.demo.controller;

import appAluno.demo.model.Subtarefa;
import appAluno.demo.model.Tarefa;
import appAluno.demo.repository.SubtarefaRepository;
import appAluno.demo.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Import necessário
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/subtarefas")
public class SubtarefaController {

    @Autowired
    private SubtarefaRepository subtarefaRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @PostMapping("/tarefa/{tarefaId}")
    public ResponseEntity<?> criarSubtarefa(@PathVariable Long tarefaId, @RequestBody Map<String, String> payload) {
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(tarefaId);
        if (tarefaOpt.isEmpty()) {
            return new ResponseEntity<>("Tarefa principal não encontrada", HttpStatus.NOT_FOUND);
        }

        Tarefa tarefa = tarefaOpt.get();
        String titulo = payload.get("titulo");

        Subtarefa novaSubtarefa = new Subtarefa();
        novaSubtarefa.setTitulo(titulo);
        novaSubtarefa.setTarefa(tarefa);
        novaSubtarefa.setConcluida("false");
        Subtarefa subtarefaSalva = subtarefaRepository.save(novaSubtarefa);

        return new ResponseEntity<>(subtarefaSalva, HttpStatus.CREATED);
    }

    @PutMapping("/{subtarefaId}/concluir")
    public ResponseEntity<Subtarefa> concluirSubtarefa(@PathVariable Long subtarefaId) {
        return subtarefaRepository.findById(subtarefaId).map(subtarefa -> {
            subtarefa.setConcluida("true");
            subtarefaRepository.save(subtarefa);
            return ResponseEntity.ok(subtarefa);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{subtarefaId}")
    public ResponseEntity<Void> deletarSubtarefa(@PathVariable Long subtarefaId) {
        if (subtarefaRepository.existsById(subtarefaId)) {
            subtarefaRepository.deleteById(subtarefaId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- NOVO ENDPOINT ADICIONADO ---
    // Este endpoint devolve todas as subtarefas concluídas de um utilizador
    // (cuja tarefa-mãe ainda não esteja concluída)
    @GetMapping("/concluidas/usuario/{usuarioId}")
    public ResponseEntity<List<Subtarefa>> getSubtarefasConcluidas(@PathVariable Long usuarioId) {
        List<Subtarefa> subtarefas = subtarefaRepository.findConcluidasByUsuarioId(usuarioId);
        return ResponseEntity.ok(subtarefas);
    }
}