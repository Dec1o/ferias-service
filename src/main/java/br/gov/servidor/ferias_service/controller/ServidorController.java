package br.gov.servidor.ferias_service.controller;

import br.gov.servidor.ferias_service.dto.ServidorDTO;
import br.gov.servidor.ferias_service.dto.ServidorCreateDTO;
import br.gov.servidor.ferias_service.service.ServidorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servidores")
@RequiredArgsConstructor
public class ServidorController {

    private final ServidorService servidorService;

    @PostMapping
    public ResponseEntity<ServidorDTO> create(@RequestBody ServidorCreateDTO dto) {
        return ResponseEntity.ok(servidorService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServidorDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(servidorService.findByIdDto(id));
    }

    @GetMapping
    public ResponseEntity<List<ServidorDTO>> getAll() {
        return ResponseEntity.ok(servidorService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServidorDTO> update(@PathVariable Long id, @RequestBody ServidorCreateDTO dto) {
        return ResponseEntity.ok(servidorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servidorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
