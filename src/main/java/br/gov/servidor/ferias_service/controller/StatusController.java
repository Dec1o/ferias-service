package br.gov.servidor.ferias_service.controller;

import br.gov.servidor.ferias_service.dto.StatusSolicitacaoDTO;
import br.gov.servidor.ferias_service.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<List<StatusSolicitacaoDTO>> getAll() {
        return ResponseEntity.ok(statusService.listAll());
    }
}
