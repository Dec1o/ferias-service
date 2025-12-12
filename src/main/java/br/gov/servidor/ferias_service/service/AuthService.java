package br.gov.servidor.ferias_service.service;

import br.gov.servidor.ferias_service.dto.auth.AuthRequestDTO;
import br.gov.servidor.ferias_service.dto.auth.AuthResponseDTO;
import br.gov.servidor.ferias_service.model.Servidor;
import br.gov.servidor.ferias_service.repository.ServidorRepository;
import br.gov.servidor.ferias_service.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ServidorRepository servidorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDTO login(AuthRequestDTO dto) {
        Servidor servidor = servidorRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.getSenha(), servidor.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        String token = jwtTokenProvider.generateToken(servidor.getEmail());
        return new AuthResponseDTO(token);
    }
}
