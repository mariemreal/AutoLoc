package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paiement introuvable : id = " + id));
    }

    @Override
    public Paiement update(Long id, Paiement paiement) {
        Paiement existing = findById(id);
        // Seuls les champs simples sont copiés : les associations ne sont pas écrasées
        // (un merge de l'objet reçu viderait par exemple les listes non renseignées).
        existing.setMontant(paiement.getMontant());
        existing.setDatePaiement(paiement.getDatePaiement());
        existing.setModePaiement(paiement.getModePaiement());
        return existing; // entité gérée : l'UPDATE part au commit (dirty checking)
    }
}
