package tn.esprit.autoloc.service;

import java.util.List;

import tn.esprit.autoloc.domain.Employe;

public interface IEmployeService {

    List<Employe> findAll();

    Employe findById(Long id);

    Employe create(Employe employe);

    Employe update(Long id, Employe employe);

    void deleteById(Long id);

    // ----- @ManyToOne Employe -> Agence -----

    /** Retire l'employé de son agence (agence_id = NULL). */
    Employe desaffecterEmploye(Long idEmploye);

    /** Rattache l'employé à une agence ; refusé s'il appartient déjà à une agence. */
    Employe affecterEmploye(Long idEmploye, Long idAgence);
}
