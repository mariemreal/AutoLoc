package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;

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

    @Override
    public Vehicule affecterVehicule(Long idVehicule, Long idAgence) {
        Vehicule vehicule = findById(idVehicule);
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable : id = " + idAgence));
        if (vehicule.getAgence() != null) {
            vehicule.getAgence().getVehicules().remove(vehicule); // quitte l'ancienne agence
        }
        vehicule.setAgence(agence);                // côté propriétaire : écrit agence_id
        agence.getVehicules().add(vehicule);       // côté inverse
        return vehicule;                           // UPDATE vehicule SET agence_id = ? au commit
    }

    @Override
    public Vehicule affecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = findById(idVehicule);
        Equipement equipement = findEquipement(idEquipement);
        if (!vehicule.getEquipements().add(equipement)) {   // Set : add() renvoie false si déjà présent
            throw new IllegalStateException("L'équipement " + idEquipement
                    + " est déjà installé sur le véhicule " + idVehicule);
        }
        equipement.getVehicules().add(vehicule);           // côté inverse (mappedBy)
        return vehicule;                                   // INSERT INTO vehicule_equipement au commit
    }

    @Override
    public Vehicule desaffecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = findById(idVehicule);
        Equipement equipement = findEquipement(idEquipement);
        if (!vehicule.getEquipements().remove(equipement)) {
            throw new IllegalStateException("L'équipement " + idEquipement
                    + " n'est pas installé sur le véhicule " + idVehicule);
        }
        equipement.getVehicules().remove(vehicule);
        return vehicule;                                   // DELETE FROM vehicule_equipement au commit
    }

    private Equipement findEquipement(Long idEquipement) {
        return equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new EntityNotFoundException("Equipement introuvable : id = " + idEquipement));
    }
}
