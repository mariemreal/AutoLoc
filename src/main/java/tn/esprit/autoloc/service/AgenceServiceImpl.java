package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Agence findById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable : id = " + id));
    }

    @Override
    public Agence create(Agence agence) {
        agence.setIdAgence(null); // id null -> save() fait un persist (INSERT), jamais un merge
        return agenceRepository.save(agence);
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence existing = findById(id);
        // Seuls les champs simples sont copiés : les associations ne sont pas écrasées
        // (un merge de l'objet reçu viderait par exemple les listes non renseignées).
        existing.setNom(agence.getNom());
        existing.setVille(agence.getVille());
        existing.setAdresse(agence.getAdresse());
        existing.setTelephone(agence.getTelephone());
        return existing; // entité gérée : l'UPDATE part au commit (dirty checking)
    }

    @Override
    public void deleteById(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new EntityNotFoundException("Agence introuvable : id = " + id);
        }
        agenceRepository.deleteById(id);
    }
}
