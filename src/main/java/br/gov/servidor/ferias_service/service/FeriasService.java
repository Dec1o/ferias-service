package br.gov.servidor.ferias_service.service;

import br.gov.servidor.ferias_service.dto.FeriasCreateDTO;
import br.gov.servidor.ferias_service.dto.FeriasDTO;
import br.gov.servidor.ferias_service.exception.BusinessException;
import br.gov.servidor.ferias_service.exception.ResourceNotFoundException;
import br.gov.servidor.ferias_service.model.Ferias;
import br.gov.servidor.ferias_service.model.Servidor;
import br.gov.servidor.ferias_service.model.StatusSolicitacao;
import br.gov.servidor.ferias_service.repository.FeriasRepository;
import br.gov.servidor.ferias_service.repository.ServidorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeriasService {

    private final FeriasRepository feriasRepository;
    private final ServidorRepository servidorRepository;
    private final StatusService statusService;

    @Transactional
    public FeriasDTO create(FeriasCreateDTO dto) {
        Servidor servidor = servidorRepository.findById(dto.getServidorId())
                .orElseThrow(() -> new ResourceNotFoundException("Servidor não encontrado"));

        if (dto.getDataInicio() == null || dto.getDataFim() == null) {
            throw new BusinessException("Datas inválidas");
        }

        if (dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new BusinessException("dataFim deve ser igual ou posterior a dataInicio");
        }

        boolean overlap = feriasRepository.findByServidorId(servidor.getId()).stream()
                .anyMatch(f -> !(dto.getDataFim().isBefore(f.getDataInicio()) || dto.getDataInicio().isAfter(f.getDataFim())));
        if (overlap) {
            throw new BusinessException("Período conflita com férias já existentes");
        }

        // Calcula pagFerias = pagamento do servidor + 1/3
        BigDecimal pagamento = servidor.getPagamento();
        BigDecimal tercio = pagamento.divide(BigDecimal.valueOf(3), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal pagFerias = pagamento.add(tercio);

        Ferias f = new Ferias();
        f.setServidor(servidor);
        f.setDataInicio(dto.getDataInicio());
        f.setDataFim(dto.getDataFim());
        f.setStatus(statusService.fromName("PENDENTE"));
        f.setDias((int) ChronoUnit.DAYS.between(dto.getDataInicio(), dto.getDataFim()) + 1);
        f.setPagFerias(pagFerias);
        f.setObservacao("Pagamento efetuado 48h antes do início das férias");

        return toDTO(feriasRepository.save(f));
    }

    @Transactional(readOnly = true)
    public FeriasDTO findById(Long id) {
        return toDTO(feriasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Férias não encontrada com id: " + id)));
    }

    @Transactional(readOnly = true)
    public List<FeriasDTO> findByServidorId(Long servidorId) {
        return feriasRepository.findByServidorId(servidorId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FeriasDTO> findAll() {
        return feriasRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FeriasDTO update(Long id, FeriasCreateDTO dto) {
        Ferias f = feriasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Férias não encontrada"));

        if (!f.getStatus().getNome().equalsIgnoreCase("PENDENTE")) {
            throw new BusinessException("Somente solicitações pendentes podem ser alteradas");
        }

        if (dto.getDataInicio() != null && dto.getDataFim() != null) {
            if (dto.getDataFim().isBefore(dto.getDataInicio())) {
                throw new BusinessException("dataFim deve ser igual ou posterior a dataInicio");
            }
            f.setDataInicio(dto.getDataInicio());
            f.setDataFim(dto.getDataFim());
            f.setDias((int) ChronoUnit.DAYS.between(dto.getDataInicio(), dto.getDataFim()) + 1);

            // Recalcula pagFerias
            BigDecimal pagamento = f.getServidor().getPagamento();
            BigDecimal tercio = pagamento.divide(BigDecimal.valueOf(3), 2, BigDecimal.ROUND_HALF_UP);
            f.setPagFerias(pagamento.add(tercio));
        }

        if (dto.getStatusId() != null) {
            StatusSolicitacao status = statusService.fromId(dto.getStatusId());
            f.setStatus(status);
        }

        return toDTO(feriasRepository.save(f));
    }

    @Transactional
    public void delete(Long id) {
        Ferias f = feriasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Férias não encontrada"));

        if (f.getStatus().getNome().equalsIgnoreCase("APROVADO")) {
            throw new BusinessException("Não é possível deletar férias já aprovadas");
        }

        feriasRepository.delete(f);
    }

    private FeriasDTO toDTO(Ferias f) {
        FeriasDTO dto = new FeriasDTO();
        dto.setId(f.getId());
        dto.setDataInicio(f.getDataInicio());
        dto.setDataFim(f.getDataFim());
        dto.setServidorId(f.getServidor() != null ? f.getServidor().getId() : null);
        dto.setStatusId(f.getStatus() != null ? f.getStatus().getId() : null);
        dto.setDias(f.getDias());
        dto.setPagFerias(f.getPagFerias());
        dto.setObservacao(f.getObservacao());
        return dto;
    }
}
