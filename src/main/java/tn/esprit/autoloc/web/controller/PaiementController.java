package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.service.IPaiementService;

import java.util.List;

/**
 * Lecture et modification des paiements. La création et la suppression passent par
 * ContratController (/api/contrats/{id}/paiements) : Paiement est l'enfant d'une composition.
 */
@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final IPaiementService paiementService;

    @GetMapping
    public List<Paiement> findAll() {
        return paiementService.findAll();
    }

    @GetMapping("/{id}")
    public Paiement findById(@PathVariable("id") Long id) {
        return paiementService.findById(id);
    }

    @PutMapping("/{id}")
    public Paiement update(@PathVariable("id") Long id, @RequestBody Paiement paiement) {
        return paiementService.update(id, paiement);
    }
}
