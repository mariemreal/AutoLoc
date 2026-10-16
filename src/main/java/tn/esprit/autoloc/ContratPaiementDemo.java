package tn.esprit.autoloc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.ModePaiement;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Programme de démonstration de l'Atelier 3 (fil rouge Contrat <-> Paiement).
 * Ne s'exécute qu'avec le profil "demo" : spring.profiles.active=demo
 * Chaque étape est à rapprocher des requêtes SQL affichées dans les logs.
 */
@Slf4j
@Component
@Profile("demo")
@RequiredArgsConstructor
public class ContratPaiementDemo implements CommandLineRunner {

    private final IContratRepository contratRepository;
    private final IPaiementRepository paiementRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void run(String... args) {
        // 1. save() sur une entité nouvelle (id null) -> persist : INSERT contrat + INSERT paiement (cascade)
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("450.00"));
        contrat.setValide(true);
        ajouterPaiement(contrat, new BigDecimal("200.00"));
        ajouterPaiement(contrat, new BigDecimal("250.00"));

        Contrat enregistre = contratRepository.save(contrat);
        Long id = enregistre.getIdContrat();
        log.info("[1] save (INSERT) -> id attribué par MySQL = {}, paiements en base = {}",
                id, paiementRepository.count());

        // 2. findById() -> Optional : présent puis absent
        contratRepository.findById(id).ifPresentOrElse(
                c -> log.info("[2] findById({}) -> trouvé, montant = {}", id, c.getMontantTotal()),
                () -> log.info("[2] findById({}) -> absent", id));
        log.info("[2] findById(-1) -> présent ? {}", contratRepository.findById(-1L).isPresent());

        // 3. save() sur une entité existante (id renseigné) -> merge : SELECT puis UPDATE
        enregistre.setMontantTotal(new BigDecimal("500.00"));
        enregistre = contratRepository.save(enregistre);
        log.info("[3] save (merge) -> nouveau montant = {}", enregistre.getMontantTotal());

        // 4. orphanRemoval : retirer un paiement de la liste -> DELETE paiement au commit
        transactionTemplate.executeWithoutResult(status -> {
            Contrat c = contratRepository.findById(id).orElseThrow();
            c.getPaiements().remove(0);
        });
        log.info("[4] orphanRemoval -> paiements en base = {}", paiementRepository.count());

        // 5. Tri + pagination (disponibles grâce à JpaRepository)
        Page<Contrat> page = contratRepository.findAll(
                PageRequest.of(0, 2, Sort.by("montantTotal").descending()));
        log.info("[5] page 0 : {} contrat(s) sur {} au total, {} page(s)",
                page.getNumberOfElements(), page.getTotalElements(), page.getTotalPages());

        // 6. deleteById() -> cascade REMOVE : DELETE paiement puis DELETE contrat
        contratRepository.deleteById(id);
        log.info("[6] deleteById({}) -> existe encore ? {}, paiements en base = {}",
                id, contratRepository.existsById(id), paiementRepository.count());

        // 7. deleteById() sur un id inexistant : aucune exception depuis Spring Data 3
        contratRepository.deleteById(999_999L);
        log.info("[7] deleteById(999999) -> aucune exception levée");
    }

    private static void ajouterPaiement(Contrat contrat, BigDecimal montant) {
        Paiement paiement = new Paiement();
        paiement.setMontant(montant);
        paiement.setDatePaiement(LocalDate.now());
        paiement.setModePaiement(ModePaiement.CARTE);
        paiement.setContrat(contrat);          // côté propriétaire de la relation
        contrat.getPaiements().add(paiement);  // côté inverse, pour la cascade
    }
}
