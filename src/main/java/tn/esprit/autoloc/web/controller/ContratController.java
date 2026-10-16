package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.service.IContratService;

import java.util.List;

@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
public class ContratController {

    private final IContratService contratService;

    @GetMapping
    public List<Contrat> findAll() {
        return contratService.findAll();
    }

    @GetMapping("/{id}")
    public Contrat findById(@PathVariable("id") Long id) {
        return contratService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contrat create(@RequestBody Contrat contrat) {
        return contratService.create(contrat);
    }

    @PutMapping("/{id}")
    public Contrat update(@PathVariable("id") Long id, @RequestBody Contrat contrat) {
        return contratService.update(id, contrat);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id) {
        contratService.deleteById(id);
    }

    // ----- Paiements du contrat (composition : on passe toujours par le contrat) -----

    @GetMapping("/{id}/paiements")
    public List<Paiement> findPaiements(@PathVariable("id") Long id) {
        return contratService.findPaiements(id);
    }

    @PostMapping("/{id}/paiements")
    @ResponseStatus(HttpStatus.CREATED)
    public Paiement addPaiement(@PathVariable("id") Long id, @RequestBody Paiement paiement) {
        return contratService.addPaiement(id, paiement);
    }

    @DeleteMapping("/{id}/paiements/{idPaiement}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePaiement(@PathVariable("id") Long id, @PathVariable("idPaiement") Long idPaiement) {
        contratService.removePaiement(id, idPaiement);
    }
}
