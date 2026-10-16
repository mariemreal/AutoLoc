# Atelier 3 — Couche Repository (Spring Data JPA)

## 1. Choix d'interface

| Interface | Étend | Justification |
|---|---|---|
| IContratRepository | JpaRepository<Contrat, Long> | Créée d'abord en `CrudRepository` (étape 1, CRUD de base + observation de la cascade et de l'orphanRemoval), puis passée en `JpaRepository` : CRUD complet, `findAll` renvoie une `List`, tri/pagination (`findAll(Pageable)`), `saveAndFlush` disponible. |
| IPaiementRepository | JpaRepository<Paiement, Long> | Paiement est l'enfant d'une composition : il sert surtout à **lire** les paiements. Création et retrait passent par le Contrat (cascade ALL + orphanRemoval). |
| IAgenceRepository | JpaRepository<Agence, Long> | Entité indépendante gérée en CRUD complet ; listes et tri utiles (ex. agences par ville). |
| IEmployeRepository | JpaRepository<Employe, Long> | CRUD complet ; listes paginées d'employés par agence à prévoir. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | Catalogue de véhicules : pagination et tri (par tarif, catégorie) indispensables. |
| IEquipementRepository | JpaRepository<Equipement, Long> | Référentiel simple, CRUD complet, `findAllById` en `List` pratique pour associer des équipements à un véhicule. |
| IClientRepository | JpaRepository<Client, Long> | CRUD complet ; recherche et pagination des clients. |
| IReservationRepository | JpaRepository<Reservation, Long> | CRUD complet ; historique paginé et trié par date. |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | CRUD complet ; historique trié des maintenances d'un véhicule. |

Les neuf interfaces utilisent la clé `Long`, conforme au type des champs `@Id` (`idContrat`, `idPaiement`…).
Aucune n'a l'annotation `@Repository` : Spring Data détecte les interfaces et génère le proxy au démarrage.

## 2. Points de compréhension

- **Iterable vs List** : `CrudRepository.findAll()` renvoie un `Iterable` (pas de `size()`, `get(i)`, `stream()` direct). `ListCrudRepository` et `JpaRepository` renvoient une `List`.
- **Spring Data 3** : `PagingAndSortingRepository` n'étend plus `CrudRepository`. Pour CRUD + pagination, il faut les deux, ou `JpaRepository`.
- **save()** :
  - id `null` → `persist` → `INSERT`, l'id IDENTITY est renseigné dans l'objet.
  - id renseigné → `merge` → `SELECT` puis `UPDATE` si un champ a changé. Il faut utiliser **l'objet retourné**.
- **findById()** renvoie un `Optional<T>` : on le traite avec `ifPresentOrElse`, `orElseThrow`… jamais `get()` sans vérification.
- **deleteById()** : passe par le contexte de persistance (`SELECT` puis `DELETE`), donc la cascade REMOVE et l'orphanRemoval s'appliquent. Aucune exception si l'id n'existe pas (Spring Data 3).
- **Suppressions en lot** (`deleteAllInBatch`, `deleteAllByIdInBatch`) : une seule requête `DELETE`, **sans** contexte de persistance → ni cascade ni orphanRemoval. Sur `contrat`, la requête échouerait sur la clé étrangère de `paiement` ou laisserait des paiements orphelins.
- **getReferenceById()** : proxy paresseux sans requête ; `EntityNotFoundException` seulement à l'accès si la ligne n'existe pas.

## 3. Observations (programme ContratPaiementDemo, profil `demo`)

Lancement : `spring.profiles.active=demo` dans `application.properties` (à retirer ensuite).
La colonne « SQL attendu » décrit ce que Hibernate doit produire ; la colonne « Constat » est à remplir avec les logs réels.

| Étape | Appel | SQL attendu | Constat (logs réels) |
|---|---|---|---|
| 1 | `save(contrat neuf)` | `insert into contrat`, puis 2 × `insert into paiement` (cascade PERSIST) | à compléter |
| 2 | `findById(id)` / `findById(-1)` | `select … from contrat where id_contrat=?` (×2) ; le 2e renvoie `Optional.empty()` | à compléter |
| 3 | `save(contrat modifié)` | `select` (merge) puis `update contrat set … montant_total=?` | à compléter |
| 4 | retrait d'un paiement de la liste | `select` contrat + `select` paiements, puis `delete from paiement where id_paiement=?` au commit (orphanRemoval) | à compléter |
| 5 | `findAll(PageRequest.of(0, 2, tri))` | `select … order by montant_total desc limit ?` + `select count(*)` | à compléter |
| 6 | `deleteById(id)` | `select` contrat, `select` paiements, `delete from paiement`, `delete from contrat` (cascade REMOVE) | à compléter |
| 7 | `deleteById(999999)` | un seul `select`, aucun `delete`, aucune exception | à compléter |

## 4. Anomalies SonarQube for IDE

| Anomalie SonarQube for IDE | Règle / explication | Correction apportée |
|---|---|---|
| `AutolocApiApplicationTests.contextLoads()` : méthode vide | java:S1186 — une méthode vide doit être justifiée par un commentaire, sinon on ne sait pas si c'est un oubli | Commentaire expliquant que le test vérifie seulement le démarrage du contexte Spring |
| à relever dans IntelliJ | à compléter | à compléter |
| à relever dans IntelliJ | à compléter | à compléter |
