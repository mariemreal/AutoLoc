package tn.esprit.autoloc.service;

import java.util.List;

import tn.esprit.autoloc.domain.Agence;

public interface IAgenceService {

    List<Agence> findAll();

    Agence findById(Long id);

    Agence create(Agence agence);

    Agence update(Long id, Agence agence);

    void deleteById(Long id);
}
