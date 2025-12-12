package br.gov.servidor.ferias_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "status") // Usando a tabela existente no banco
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusSolicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome; // PENDENTE, APROVADO, REPROVADO
}
