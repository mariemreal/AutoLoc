package tn.esprit.autoloc.service;

import java.util.List;

import tn.esprit.autoloc.domain.Paiement;

/**
 * Paiement est l'enfant d'une composition : il est créé et supprimé
 * uniquement via IContratService (addPaiement / removePaiement).
 */
public interface IPaiementService {

    List<Paiement> findAll();

    Paiement findById(Long id);

    Paiement update(Long id, Paiement paiement);
}
