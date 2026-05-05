package appAluno.demo.controller;

import appAluno.demo.model.Usuario;
import appAluno.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario novoUsuario) {
        if (usuarioRepository.findByNomeUsuario(novoUsuario.getNomeUsuario()).isPresent()) {
            return new ResponseEntity<>("Nome de utilizador já existe.", HttpStatus.BAD_REQUEST);
        }
        if (usuarioRepository.findByEmail(novoUsuario.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email já registado.", HttpStatus.BAD_REQUEST);
        }
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        return new ResponseEntity<>(usuarioSalvo, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciais) {
        String nomeUsuario = credenciais.get("nomeUsuario");
        String senha = credenciais.get("senha");
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNomeUsuario(nomeUsuario);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getSenha().equals(senha)) {
            return new ResponseEntity<>(usuarioOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Nome de utilizador ou senha inválidos.", HttpStatus.UNAUTHORIZED);
    }

    //ATUALIZAR PERFIL
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPerfil(@PathVariable Long id, @RequestBody Usuario dadosAtualizados) {
        // Procura o utilizador para editar.
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>("Utilizador não encontrado.", HttpStatus.NOT_FOUND);
        }

        //LÓGICA DE VALIDAÇÃO
        // Verifica se o novo NOME DE UTILIZADOR já está a ser usado por OUTRO
        Optional<Usuario> checkNomeUsuario = usuarioRepository.findByNomeUsuario(dadosAtualizados.getNomeUsuario());
        if (checkNomeUsuario.isPresent() && !checkNomeUsuario.get().getId().equals(id)) {
            return new ResponseEntity<>("Este nome de utilizador já está em uso.", HttpStatus.BAD_REQUEST);
        }

        //Verifica se o novo EMAIL já está a ser usado por OUTRO
        Optional<Usuario> checkEmail = usuarioRepository.findByEmail(dadosAtualizados.getEmail());
        if (checkEmail.isPresent() && !checkEmail.get().getId().equals(id)) {
            return new ResponseEntity<>("Este email já está em uso.", HttpStatus.BAD_REQUEST);
        }

        // Se estiver tudo OK, atualiza td.
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
        if (!usuario.getSenha().equals(senhaAntiga)) {
            return new ResponseEntity<>("Senha antiga incorreta.", HttpStatus.BAD_REQUEST);
        }
        usuario.setSenha(senhaNova);
        usuarioRepository.save(usuario);
        return new ResponseEntity<>("Senha alterada com sucesso!", HttpStatus.OK);
    }
}

