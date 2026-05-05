package appAluno.demo.controller;

import appAluno.demo.model.Usuario;
import appAluno.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario novoUsuario) {
        if (usuarioRepository.findByNomeUsuario(novoUsuario.getNomeUsuario()).isPresent()) {
            return new ResponseEntity<>("Nome de utilizador já existe.", HttpStatus.BAD_REQUEST);
        }
        if (usuarioRepository.findByEmail(novoUsuario.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email já registado.", HttpStatus.BAD_REQUEST);
        }
        // Criptografa a senha antes de salvar
        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        return new ResponseEntity<>(usuarioSalvo, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciais) {
        String nomeUsuario = credenciais.get("nomeUsuario");
        String senha = credenciais.get("senha");
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNomeUsuario(nomeUsuario);
        if (usuarioOpt.isPresent() && passwordEncoder.matches(senha, usuarioOpt.get().getSenha())) {
            return new ResponseEntity<>(usuarioOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Nome de utilizador ou senha inválidos.", HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPerfil(@PathVariable Long id, @RequestBody Usuario dadosAtualizados) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>("Utilizador não encontrado.", HttpStatus.NOT_FOUND);
        }
        Optional<Usuario> checkNomeUsuario = usuarioRepository.findByNomeUsuario(dadosAtualizados.getNomeUsuario());
        if (checkNomeUsuario.isPresent() && !checkNomeUsuario.get().getId().equals(id)) {
            return new ResponseEntity<>("Este nome de utilizador já está em uso.", HttpStatus.BAD_REQUEST);
        }
        Optional<Usuario> checkEmail = usuarioRepository.findByEmail(dadosAtualizados.getEmail());
        if (checkEmail.isPresent() && !checkEmail.get().getId().equals(id)) {
            return new ResponseEntity<>("Este email já está em uso.", HttpStatus.BAD_REQUEST);
        }
        Usuario usuarioExistente = usuarioOpt.get();
        usuarioExistente.setNomeUsuario(dadosAtualizados.getNomeUsuario());
        usuarioExistente.setEmail(dadosAtualizados.getEmail());
        usuarioExistente.setNomeCompleto(dadosAtualizados.getNomeCompleto());
        usuarioExistente.setTelefone(dadosAtualizados.getTelefone());
        Usuario usuarioSalvo = usuarioRepository.save(usuarioExistente);
        return ResponseEntity.ok(usuarioSalvo);
    }

    @PutMapping("/mudar-senha")
    public ResponseEntity<String> mudarSenha(@RequestBody Map<String, String> dados) {
        String nomeUsuario = dados.get("nomeUsuario");
        String senhaAntiga = dados.get("senhaAntiga");
        String senhaNova = dados.get("senhaNova");
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNomeUsuario(nomeUsuario);
        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>("Utilizador não encontrado.", HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioOpt.get();
        if (!passwordEncoder.matches(senhaAntiga, usuario.getSenha())) {
            return new ResponseEntity<>("Senha antiga incorreta.", HttpStatus.BAD_REQUEST);
        }
        // Criptografa a nova senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(senhaNova));
        usuarioRepository.save(usuario);
        return new ResponseEntity<>("Senha alterada com sucesso!", HttpStatus.OK);
    }
}