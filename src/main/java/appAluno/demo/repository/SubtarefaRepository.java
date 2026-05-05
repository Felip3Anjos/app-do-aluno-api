package appAluno.demo.repository;

import appAluno.demo.model.Subtarefa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubtarefaRepository extends JpaRepository<Subtarefa, Long> {

    @Transactional
    @Modifying
    void deleteAllByTarefa_UsuarioId(Long usuarioId);

    @Query("SELECT s FROM Subtarefa s WHERE s.tarefa.usuario.id = ?1 AND s.concluida = 'true' AND s.tarefa.concluida = 'false'")
    List<Subtarefa> findConcluidasByUsuarioId(Long usuarioId);
}