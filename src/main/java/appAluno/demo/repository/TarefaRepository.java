package appAluno.demo.repository;

import appAluno.demo.model.Tarefa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByUsuarioIdAndConcluida(Long usuarioId, String concluida);

    // A anotação @Transactional é necessária para operações de modificação
    @Transactional
    @Modifying // Indica que esta query vai apagar dados
    void deleteAllByUsuarioId(Long usuarioId);
}

