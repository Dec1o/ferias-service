package br.gov.servidor.ferias_service.service;

import br.gov.servidor.ferias_service.dto.ServidorCreateDTO;
import br.gov.servidor.ferias_service.dto.ServidorDTO;
import br.gov.servidor.ferias_service.exception.BusinessException;
import br.gov.servidor.ferias_service.exception.ResourceNotFoundException;
import br.gov.servidor.ferias_service.model.Servidor;
import br.gov.servidor.ferias_service.repository.ServidorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServidorService {

    private final ServidorRepository servidorRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ServidorDTO create(ServidorCreateDTO dto) {
        if (servidorRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

        Servidor s = new Servidor();
        s.setNome(dto.getNome());
        s.setEmail(dto.getEmail());
        s.setSenha(passwordEncoder.encode(dto.getSenha()));
        s.setPagamento(dto.getPagamento()); // novo campo

        Servidor saved = servidorRepository.save(s);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public ServidorDTO findByIdDto(Long id) {
        Servidor s = servidorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servidor não encontrado com id: " + id));
        return toDTO(s);
    }

    @Transactional(readOnly = true)
    public Servidor findById(Long id) {
        return servidorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servidor não encontrado com id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ServidorDTO> findAll() {
        return servidorRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServidorDTO update(Long id, ServidorCreateDTO dto) {
        Servidor s = findById(id);
        s.setNome(dto.getNome());

        if (!s.getEmail().equals(dto.getEmail())) {
            if (servidorRepository.existsByEmail(dto.getEmail())) {
                throw new BusinessException("Email já cadastrado por outro usuário");
            }
            s.setEmail(dto.getEmail());
        }

        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            s.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        s.setPagamento(dto.getPagamento()); // atualiza pagamento

        Servidor updated = servidorRepository.save(s);
        return toDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        Servidor s = findById(id);
        servidorRepository.delete(s);
    }

    private ServidorDTO toDTO(Servidor s) {
        ServidorDTO dto = new ServidorDTO();
        dto.setId(s.getId());
        dto.setNome(s.getNome());
        dto.setEmail(s.getEmail());
        dto.setPagamento(s.getPagamento()); // novo campo
        return dto;
    }
}
