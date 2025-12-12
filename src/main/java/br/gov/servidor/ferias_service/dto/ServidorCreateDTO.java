package br.gov.servidor.ferias_service.dto;

import java.math.BigDecimal;

public class ServidorCreateDTO {

    private String nome;
    private String email;
    private String senha;
    private BigDecimal pagamento; // Alterado para BigDecimal

    public ServidorCreateDTO() {}

    public ServidorCreateDTO(String nome, String email, String senha, BigDecimal pagamento) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.pagamento = pagamento;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public BigDecimal getPagamento() { return pagamento; }
    public void setPagamento(BigDecimal pagamento) { this.pagamento = pagamento; }
}
