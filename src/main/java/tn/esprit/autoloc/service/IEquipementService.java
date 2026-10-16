package tn.esprit.autoloc.service;

import java.util.List;

import tn.esprit.autoloc.domain.Equipement;

public interface IEquipementService {

    List<Equipement> findAll();

    Equipement findById(Long id);

    Equipement create(Equipement equipement);

    Equipement update(Long id, Equipement equipement);

    void deleteById(Long id);
}
