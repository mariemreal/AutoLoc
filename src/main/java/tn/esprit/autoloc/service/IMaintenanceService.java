package tn.esprit.autoloc.service;

import java.util.List;

import tn.esprit.autoloc.domain.Maintenance;

public interface IMaintenanceService {

    List<Maintenance> findAll();

    Maintenance findById(Long id);

    Maintenance create(Maintenance maintenance);

    Maintenance update(Long id, Maintenance maintenance);

    void deleteById(Long id);
}
