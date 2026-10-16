package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contrat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrat;

    @Column(nullable = false)
    private LocalDate dateSignature;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montantTotal;

    private boolean valide;

    // 1 Contrat <-> 1 Reservation (cote proprietaire : colonne reservation_id unique)
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "reservation_id", unique = true)
    private Reservation reservation;

    // 1 Contrat -> N Paiement (composition) : supprimer un contrat supprime ses paiements,
    // retirer un paiement de la liste le supprime de la base (orphanRemoval)
    @JsonIgnore
    @OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Paiement> paiements = new ArrayList<>();
}
