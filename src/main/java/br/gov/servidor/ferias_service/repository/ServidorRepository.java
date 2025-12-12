package br.gov.servidor.ferias_service.repository;

import br.gov.servidor.ferias_service.model.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServidorRepository extends JpaRepository<Servidor, Long> {

    Optional<Servidor> findByEmail(String email);

    boolean existsByEmail(String email);
}
