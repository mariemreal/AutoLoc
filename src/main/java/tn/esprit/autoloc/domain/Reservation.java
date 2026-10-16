package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutReservation statut;

    // N Reservation -> 1 Client
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    // N Reservation -> 1 Vehicule
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    // 1 Reservation <-> 1 Contrat (cote inverse : la cle etrangere est dans contrat)
    @OneToOne(mappedBy = "reservation")
    private Contrat contrat;
}
