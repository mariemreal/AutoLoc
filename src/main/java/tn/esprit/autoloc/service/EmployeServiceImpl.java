package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employe introuvable : id = " + id));
    }

    @Override
    public Employe create(Employe employe) {
        employe.setIdEmploye(null); // id null -> save() fait un persist (INSERT), jamais un merge
        return employeRepository.save(employe);
    }

    @Override
    public Employe update(Long id, Employe employe) {
        Employe existing = findById(id);
        // Seuls les champs simples sont copiés : les associations ne sont pas écrasées
        // (un merge de l'objet reçu viderait par exemple les listes non renseignées).
        existing.setNom(employe.getNom());
        existing.setPrenom(employe.getPrenom());
        existing.setRole(employe.getRole());
        return existing; // entité gérée : l'UPDATE part au commit (dirty checking)
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employe introuvable : id = " + id);
        }
        employeRepository.deleteById(id);
    }

    @Override
    public Employe desaffecterEmploye(Long idEmploye) {
        Employe employe = findById(idEmploye);
        Agence ancienne = employe.getAgence();
        if (ancienne != null) {
            ancienne.getEmployes().remove(employe); // côté inverse, pour garder les deux côtés cohérents
        }
        employe.setAgence(null);                    // côté propriétaire : c'est lui qui écrit agence_id
        return employe;                             // UPDATE employe SET agence_id = NULL au commit
    }

    @Override
    public Employe affecterEmploye(Long idEmploye, Long idAgence) {
        Employe employe = findById(idEmploye);
        if (employe.getAgence() != null) {
            // Règle métier : un employé n'appartient qu'à une agence -> un transfert passe par une désaffectation
            throw new IllegalStateException("L'employé " + idEmploye + " appartient déjà à l'agence "
                    + employe.getAgence().getIdAgence() + " : désaffectez-le d'abord");
        }
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable : id = " + idAgence));
        employe.setAgence(agence);
        agence.getEmployes().add(employe);
        return employe;                             // UPDATE employe SET agence_id = ? au commit
    }
}
