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
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @GetMapping
    public List<Vehicule> findAll() {
        return vehiculeService.findAll();
    }

    @GetMapping("/{id}")
    public Vehicule findById(@PathVariable("id") Long id) {
        return vehiculeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicule create(@RequestBody Vehicule vehicule) {
        return vehiculeService.create(vehicule);
    }

    @PutMapping("/{id}")
    public Vehicule update(@PathVariable("id") Long id, @RequestBody Vehicule vehicule) {
        return vehiculeService.update(id, vehicule);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id) {
        vehiculeService.deleteById(id);
    }

    // PUT /api/vehicules/5/agence/2
    @PutMapping("/{id}/agence/{idAgence}")
    public Vehicule affecterVehicule(@PathVariable("id") Long idVehicule,
                                     @PathVariable("idAgence") Long idAgence) {
        return vehiculeService.affecterVehicule(idVehicule, idAgence);
    }

    // PUT /api/vehicules/5/equipements/1
    @PutMapping("/{id}/equipements/{idEquipement}")
    public Vehicule affecterEquipement(@PathVariable("id") Long idVehicule,
                                       @PathVariable("idEquipement") Long idEquipement) {
        return vehiculeService.affecterEquipement(idVehicule, idEquipement);
    }

    // DELETE /api/vehicules/5/equipements/1
    @DeleteMapping("/{id}/equipements/{idEquipement}")
    public Vehicule desaffecterEquipement(@PathVariable("id") Long idVehicule,
                                          @PathVariable("idEquipement") Long idEquipement) {
        return vehiculeService.desaffecterEquipement(idVehicule, idEquipement);
    }
}
