package tn.esprit.autoloc.service;

import java.util.List;

import tn.esprit.autoloc.domain.Vehicule;

public interface IVehiculeService {

    List<Vehicule> findAll();

    Vehicule findById(Long id);

    Vehicule create(Vehicule vehicule);

    Vehicule update(Long id, Vehicule vehicule);

    void deleteById(Long id);

    // ----- @ManyToOne Vehicule -> Agence -----

    /** Rattache un véhicule à une agence (remplace l'agence précédente s'il en avait une). */
    Vehicule affecterVehicule(Long idVehicule, Long idAgence);

    // ----- @ManyToMany Vehicule <-> Equipement -----

    /** Installe un équipement sur le véhicule (ligne ajoutée dans vehicule_equipement). */
    Vehicule affecterEquipement(Long idVehicule, Long idEquipement);

    /** Retire l'équipement du véhicule (ligne supprimée de vehicule_equipement). */
    Vehicule desaffecterEquipement(Long idVehicule, Long idEquipement);
}
