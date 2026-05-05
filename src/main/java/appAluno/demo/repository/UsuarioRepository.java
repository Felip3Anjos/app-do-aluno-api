package appAluno.demo.repository;

import appAluno.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Este método é ótimo para uma barra de pesquisa, onde o utilizador digita parte do nome.
    @Query("select u from Usuario u where u.nomeUsuario like ?1%")
    List<Usuario> findByNomeUsuarioparte(String nomeUsuario);

    // Ele procura por um nome de utilizador EXATO,e ve se ja tem, o que é essencial para o login

    Optional<Usuario> findByNomeUsuario(String nomeUsuario);

    // A busca por email também deve ser única, então é Optional
    Optional<Usuario> findByEmail(String email);
}

