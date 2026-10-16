package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable : id = " + id));
    }

    @Override
    public Client create(Client client) {
        client.setIdClient(null); // id null -> save() fait un persist (INSERT), jamais un merge
        return clientRepository.save(client);
    }

    @Override
    public Client update(Long id, Client client) {
        Client existing = findById(id);
        // Seuls les champs simples sont copiés : les associations ne sont pas écrasées
        // (un merge de l'objet reçu viderait par exemple les listes non renseignées).
        existing.setNom(client.getNom());
        existing.setPrenom(client.getPrenom());
        existing.setEmail(client.getEmail());
        existing.setTelephone(client.getTelephone());
        existing.setNumPermis(client.getNumPermis());
        existing.setDateInscription(client.getDateInscription());
        return existing; // entité gérée : l'UPDATE part au commit (dirty checking)
    }

    @Override
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client introuvable : id = " + id);
        }
        clientRepository.deleteById(id);
    }
}
