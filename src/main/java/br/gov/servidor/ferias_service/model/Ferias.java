package br.gov.servidor.ferias_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ferias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ferias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "servidor_id", nullable = false)
    private Servidor servidor;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusSolicitacao status;

    @Column(nullable = false)
    private Integer dias;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pagFerias;

    @Column(nullable = false)
    private String observacao;
}
