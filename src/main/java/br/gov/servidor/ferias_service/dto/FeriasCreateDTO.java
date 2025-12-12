package br.gov.servidor.ferias_service.dto;

import java.time.LocalDate;

public class FeriasCreateDTO {

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Long servidorId;
    private Long statusId; // opcional

    public FeriasCreateDTO() {}

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public Long getServidorId() { return servidorId; }
    public void setServidorId(Long servidorId) { this.servidorId = servidorId; }
    public Long getStatusId() { return statusId; }
    public void setStatusId(Long statusId) { this.statusId = statusId; }
}
