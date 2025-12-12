package br.gov.servidor.ferias_service.controller;
import br.gov.servidor.ferias_service.dto.auth.AuthRequestDTO;
import br.gov.servidor.ferias_service.dto.auth.AuthResponseDTO;
import br.gov.servidor.ferias_service.dto.ServidorCreateDTO;
import br.gov.servidor.ferias_service.dto.ServidorDTO;
import br.gov.servidor.ferias_service.service.ServidorService;
import br.gov.servidor.ferias_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ServidorService servidorService;

    // Registrar servidor
    @PostMapping("/register")
    public ResponseEntity<ServidorDTO> register(@RequestBody ServidorCreateDTO dto) {
        ServidorDTO servidorDTO = servidorService.create(dto);
        return ResponseEntity.ok(servidorDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO dto) {
        AuthResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
