package appAluno.demo.repository;

import appAluno.demo.model.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvisoRepository extends JpaRepository<Aviso, Integer> {

    List<Aviso> findAllByOrderByDataPublicacaoDesc();
}