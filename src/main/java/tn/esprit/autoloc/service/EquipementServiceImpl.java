package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repository.IEquipementRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Equipement findById(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipement introuvable : id = " + id));
    }

    @Override
    public Equipement create(Equipement equipement) {
        equipement.setIdEquipement(null); // id null -> save() fait un persist (INSERT), jamais un merge
        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement update(Long id, Equipement equipement) {
        Equipement existing = findById(id);
        // Seuls les champs simples sont copiés : les associations ne sont pas écrasées
        // (un merge de l'objet reçu viderait par exemple les listes non renseignées).
        existing.setLibelle(equipement.getLibelle());
        return existing; // entité gérée : l'UPDATE part au commit (dirty checking)
    }

    @Override
    public void deleteById(Long id) {
        if (!equipementRepository.existsById(id)) {
            throw new EntityNotFoundException("Equipement introuvable : id = " + id);
        }
        equipementRepository.deleteById(id);
    }
}
