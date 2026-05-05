package appAluno.demo.repository;

import appAluno.demo.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    // encontra todos os eventos de um user específico
    List<Evento> findByUsuarioId(Long usuarioId);
}
