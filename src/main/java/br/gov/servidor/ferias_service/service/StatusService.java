package br.gov.servidor.ferias_service.service;

import br.gov.servidor.ferias_service.dto.StatusSolicitacaoDTO;
import br.gov.servidor.ferias_service.model.StatusSolicitacao;
import br.gov.servidor.ferias_service.repository.StatusSolicitacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusService {

    private final StatusSolicitacaoRepository repository;

    public StatusService(StatusSolicitacaoRepository repository) {
        this.repository = repository;
    }

    public List<StatusSolicitacaoDTO> listAll() {
        return repository.findAll()
                .stream()
                .map(s -> new StatusSolicitacaoDTO(s.getId(), s.getNome()))
                .collect(Collectors.toList());
    }

    public StatusSolicitacao fromName(String name) {
        return repository.findByNome(name)
                .orElseThrow(() -> new IllegalArgumentException("Status não encontrado: " + name));
    }

    public StatusSolicitacao fromId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Status inválido: " + id));
    }
}
