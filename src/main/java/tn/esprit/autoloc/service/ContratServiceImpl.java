package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IPaiementRepository paiementRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat introuvable : id = " + id));
    }

    @Override
    public Contrat create(Contrat contrat) {
        contrat.setIdContrat(null); // id null -> save() fait un persist (INSERT), jamais un merge
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat update(Long id, Contrat contrat) {
        Contrat existing = findById(id);
        // Seuls les champs simples sont copiés : les associations ne sont pas écrasées
        // (un merge de l'objet reçu viderait par exemple les listes non renseignées).
        existing.setDateSignature(contrat.getDateSignature());
        existing.setMontantTotal(contrat.getMontantTotal());
        existing.setValide(contrat.isValide());
        return existing; // entité gérée : l'UPDATE part au commit (dirty checking)
    }

    @Override
    public void deleteById(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new EntityNotFoundException("Contrat introuvable : id = " + id);
        }
        contratRepository.deleteById(id);
    }

    @Override
    public Paiement addPaiement(Long idContrat, Paiement paiement) {
        Contrat contrat = findById(idContrat);
        paiement.setIdPaiement(null);
        paiement.setContrat(contrat);          // côté propriétaire (clé étrangère contrat_id)
        contrat.getPaiements().add(paiement);  // côté inverse : cascade PERSIST au commit
        return paiementRepository.save(paiement);
    }

    @Override
    public void removePaiement(Long idContrat, Long idPaiement) {
        Contrat contrat = findById(idContrat);
        boolean retire = contrat.getPaiements()
                .removeIf(p -> p.getIdPaiement().equals(idPaiement)); // orphanRemoval -> DELETE
        if (!retire) {
            throw new EntityNotFoundException(
                    "Paiement " + idPaiement + " introuvable dans le contrat " + idContrat);
        }
    }
}
