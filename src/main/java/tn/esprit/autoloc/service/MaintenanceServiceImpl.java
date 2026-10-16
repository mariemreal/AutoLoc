package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Maintenance findById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Maintenance introuvable : id = " + id));
    }

    @Override
    public Maintenance create(Maintenance maintenance) {
        maintenance.setIdMaintenance(null); // id null -> save() fait un persist (INSERT), jamais un merge
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance update(Long id, Maintenance maintenance) {
        Maintenance existing = findById(id);
        // Seuls les champs simples sont copiés : les associations ne sont pas écrasées
        // (un merge de l'objet reçu viderait par exemple les listes non renseignées).
        existing.setDateDebut(maintenance.getDateDebut());
        existing.setDateFin(maintenance.getDateFin());
        existing.setDescription(maintenance.getDescription());
        return existing; // entité gérée : l'UPDATE part au commit (dirty checking)
    }

    @Override
    public void deleteById(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new EntityNotFoundException("Maintenance introuvable : id = " + id);
        }
        maintenanceRepository.deleteById(id);
    }
}
