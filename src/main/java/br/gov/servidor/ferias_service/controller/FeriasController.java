package br.gov.servidor.ferias_service.controller;

import br.gov.servidor.ferias_service.dto.FeriasCreateDTO;
import br.gov.servidor.ferias_service.dto.FeriasDTO;
import br.gov.servidor.ferias_service.service.FeriasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ferias")
@RequiredArgsConstructor
public class FeriasController {

    private final FeriasService feriasService;

    @PostMapping
    public ResponseEntity<FeriasDTO> create(@RequestBody FeriasCreateDTO dto) {
        return ResponseEntity.ok(feriasService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeriasDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(feriasService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<FeriasDTO>> getAll() {
        return ResponseEntity.ok(feriasService.findAll());
    }

    @GetMapping("/servidor/{servidorId}")
    public ResponseEntity<List<FeriasDTO>> getByServidorId(@PathVariable Long servidorId) {
        return ResponseEntity.ok(feriasService.findByServidorId(servidorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeriasDTO> update(@PathVariable Long id, @RequestBody FeriasCreateDTO dto) {
        return ResponseEntity.ok(feriasService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        feriasService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
