package tn.esprit.autoloc.service;

import java.util.List;

import tn.esprit.autoloc.domain.Client;

public interface IClientService {

    List<Client> findAll();

    Client findById(Long id);

    Client create(Client client);

    Client update(Long id, Client client);

    void deleteById(Long id);
}
