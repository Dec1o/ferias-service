package br.gov.servidor.ferias_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FeriasDTO {

    private Long id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Long servidorId;
    private Long statusId;
    private Integer dias;
    private BigDecimal pagFerias;
    private String observacao;

    public FeriasDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public Long getServidorId() { return servidorId; }
    public void setServidorId(Long servidorId) { this.servidorId = servidorId; }
    public Long getStatusId() { return statusId; }
    public void setStatusId(Long statusId) { this.statusId = statusId; }
    public Integer getDias() { return dias; }
    public void setDias(Integer dias) { this.dias = dias; }
    public BigDecimal getPagFerias() { return pagFerias; }
    public void setPagFerias(BigDecimal pagFerias) { this.pagFerias = pagFerias; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
