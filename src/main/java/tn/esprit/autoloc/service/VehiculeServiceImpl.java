package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicule introuvable : id = " + id));
    }

    @Override
    public Vehicule create(Vehicule vehicule) {
        vehicule.setIdVehicule(null); // id null -> save() fait un persist (INSERT), jamais un merge
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        Vehicule existing = findById(id);
        // Seuls les champs simples sont copiés : les associations ne sont pas écrasées
        // (un merge de l'objet reçu viderait par exemple les listes non renseignées).
        existing.setImmatriculation(vehicule.getImmatriculation());
        existing.setMarque(vehicule.getMarque());
        existing.setModele(vehicule.getModele());
        existing.setCategorie(vehicule.getCategorie());
        existing.setTarifJournalier(vehicule.getTarifJournalier());
        existing.setStatut(vehicule.getStatut());
        return existing; // entité gérée : l'UPDATE part au commit (dirty checking)
    }

    @Override
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new EntityNotFoundException("Vehicule introuvable : id = " + id);
        }
        vehiculeRepository.deleteById(id);
    }
}
