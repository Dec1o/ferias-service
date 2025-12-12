package br.gov.servidor.ferias_service.repository;

import br.gov.servidor.ferias_service.model.Ferias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeriasRepository extends JpaRepository<Ferias, Long> {
    List<Ferias> findByServidorId(Long servidorId);
}
