package appAluno.demo.controller;

import appAluno.demo.model.Aviso;
import appAluno.demo.repository.AvisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/avisos")
public class AvisoController {

    @Autowired
    private AvisoRepository avisoRepository;

    @GetMapping
    public ResponseEntity<List<Aviso>> getTodosAvisos() {
        // --- MUDANÇA AQUI ---
        // Pedi ao repositório para ordenar pela coluna nova
        List<Aviso> avisos = avisoRepository.findAllByOrderByDataPublicacaoDesc();
        return ResponseEntity.ok(avisos);
    }

    @PostMapping
    public ResponseEntity<Aviso> criarAviso(@RequestBody Aviso aviso) {
        // --- MUDANÇA AQUI ---
        // Atualiza o campo 'dataPublicacao' ao invés de 'dataPostagem'
        aviso.setDataPublicacao(LocalDate.now());

        Aviso novoAviso = avisoRepository.save(aviso);
        return new ResponseEntity<>(novoAviso, HttpStatus.CREATED);
    }
}