package br.gov.servidor.ferias_service.dto;

import java.math.BigDecimal;

public class ServidorDTO {

    private Long id;
    private String nome;
    private String email;
    private BigDecimal pagamento; // Alterado para BigDecimal

    public ServidorDTO() {}

    public ServidorDTO(Long id, String nome, String email, BigDecimal pagamento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.pagamento = pagamento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public BigDecimal getPagamento() { return pagamento; }
    public void setPagamento(BigDecimal pagamento) { this.pagamento = pagamento; }
}
