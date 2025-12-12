package br.gov.servidor.ferias_service.repository;

import br.gov.servidor.ferias_service.model.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusSolicitacaoRepository extends JpaRepository<StatusSolicitacao, Long> {
    Optional<StatusSolicitacao> findByNome(String nome);
}
