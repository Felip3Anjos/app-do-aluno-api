package appAluno.demo.controller;

import appAluno.demo.model.Evento;
import appAluno.demo.model.Usuario;
import appAluno.demo.repository.EventoRepository;
import appAluno.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    //criar um novo evento
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> criarEvento(@PathVariable Long usuarioId, @RequestBody Map<String, String> payload) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>("Utilizador não encontrado", HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOpt.get();
        String titulo = payload.get("titulo");
        LocalDate data = LocalDate.parse(payload.get("data")); //Espera uma data no formato "AAAA-MM-DD"

        Evento novoEvento = new Evento();
        novoEvento.setTitulo(titulo);
        novoEvento.setDataEvento(data);
        novoEvento.setUsuario(usuario);

        Evento eventoSalvo = eventoRepository.save(novoEvento);
        return new ResponseEntity<>(eventoSalvo, HttpStatus.CREATED);
    }

    //buscar todos os eventos de um user
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Evento>> getEventosPorUsuario(@PathVariable Long usuarioId) {
        List<Evento> eventos = eventoRepository.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(eventos);
    }
}
