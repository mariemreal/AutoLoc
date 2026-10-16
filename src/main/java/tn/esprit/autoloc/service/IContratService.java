package tn.esprit.autoloc.service;

import java.util.List;

import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;

public interface IContratService {

    List<Contrat> findAll();

    Contrat findById(Long id);

    Contrat create(Contrat contrat);

    Contrat update(Long id, Contrat contrat);

    void deleteById(Long id);

    // Composition Contrat -> Paiement : un paiement se crée et se retire via son contrat
    List<Paiement> findPaiements(Long idContrat);

    Paiement addPaiement(Long idContrat, Paiement paiement);

    void removePaiement(Long idContrat, Long idPaiement);
}
