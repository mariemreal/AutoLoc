package tn.esprit.autoloc.service;

import java.util.List;

import tn.esprit.autoloc.domain.Reservation;

public interface IReservationService {

    List<Reservation> findAll();

    Reservation findById(Long id);

    Reservation create(Reservation reservation);

    Reservation update(Long id, Reservation reservation);

    void deleteById(Long id);
}
