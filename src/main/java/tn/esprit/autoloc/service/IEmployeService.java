package tn.esprit.autoloc.service;

import java.util.List;

import tn.esprit.autoloc.domain.Employe;

public interface IEmployeService {

    List<Employe> findAll();

    Employe findById(Long id);

    Employe create(Employe employe);

    Employe update(Long id, Employe employe);

    void deleteById(Long id);
}
