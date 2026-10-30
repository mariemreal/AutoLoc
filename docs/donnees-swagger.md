# Remplir la base avec Swagger

Swagger UI : `http://localhost:PORT/swagger-ui.html` (PORT = 8089 ou 8080, voir `Tomcat started on port …`).

Pour chaque requête : ouvrir l'endpoint → **Try it out** → coller le JSON → **Execute**.
La réponse doit être **201** (création) ou **200** (affectation).

> Les ids ci-dessous (1, 2, 3…) supposent des tables vides. Si ta base contient déjà des lignes,
> lis l'id renvoyé dans la réponse de chaque POST et utilise-le à la place.

## 1. Agences — `POST /api/agences`

```json
{ "nom": "AutoLoc Tunis", "ville": "Tunis", "adresse": "Avenue Habib Bourguiba", "telephone": "71100100" }
```
```json
{ "nom": "AutoLoc Sfax", "ville": "Sfax", "adresse": "Route de Tunis km 2", "telephone": "74200200" }
```
```json
{ "nom": "AutoLoc Sousse", "ville": "Sousse", "adresse": "Boulevard du 14 Janvier", "telephone": "73300300" }
```
→ Tunis = 1, Sfax = 2, Sousse = 3

## 2. Employés — `POST /api/employes` (role : `AGENT` ou `MANAGER`)

```json
{ "nom": "Ben Ali", "prenom": "Karim", "role": "AGENT" }
```
```json
{ "nom": "Gharbi", "prenom": "Ines", "role": "MANAGER" }
```
```json
{ "nom": "Haddad", "prenom": "Sami", "role": "AGENT" }
```

## 3. Véhicules — `POST /api/vehicules`
(categorie : `CITADINE`, `BERLINE`, `SUV`, `UTILITAIRE` — statut : `DISPONIBLE`, `LOUE`, `MAINTENANCE`)

```json
{ "immatriculation": "210 TU 1001", "marque": "Citroen", "modele": "C3", "categorie": "CITADINE", "tarifJournalier": 85.00, "statut": "DISPONIBLE" }
```
```json
{ "immatriculation": "215 TU 2002", "marque": "Peugeot", "modele": "508", "categorie": "BERLINE", "tarifJournalier": 140.00, "statut": "DISPONIBLE" }
```
```json
{ "immatriculation": "220 TU 3003", "marque": "Kia", "modele": "Sportage", "categorie": "SUV", "tarifJournalier": 180.00, "statut": "DISPONIBLE" }
```
```json
{ "immatriculation": "225 TU 4004", "marque": "Renault", "modele": "Kangoo", "categorie": "UTILITAIRE", "tarifJournalier": 110.00, "statut": "DISPONIBLE" }
```

## 4. Équipements — `POST /api/equipements`

```json
{ "libelle": "Siège bébé" }
```
```json
{ "libelle": "GPS" }
```
```json
{ "libelle": "Galerie de toit" }
```

## 5. Clients — `POST /api/clients`

```json
{ "nom": "Mansour", "prenom": "Leila", "email": "leila.mansour@mail.tn", "telephone": "98111222", "numPermis": "P-100001", "dateInscription": "2026-10-01" }
```
```json
{ "nom": "Jebali", "prenom": "Omar", "email": "omar.jebali@mail.tn", "telephone": "22333444", "numPermis": "P-100002", "dateInscription": "2026-10-15" }
```

## 6. Réservations — `POST /api/reservations`
(statut : `EN_ATTENTE`, `CONFIRMEE`, `ANNULEE`, `TERMINEE`)

```json
{ "dateDebut": "2026-11-02", "dateFin": "2026-11-06", "statut": "CONFIRMEE" }
```
```json
{ "dateDebut": "2026-11-10", "dateFin": "2026-11-12", "statut": "EN_ATTENTE" }
```

## 7. Contrats — `POST /api/contrats`, puis leurs paiements

```json
{ "dateSignature": "2026-11-01", "montantTotal": 340.00, "valide": true }
```
`POST /api/contrats/1/paiements` (modePaiement : `CARTE`, `ESPECES`, `VIREMENT`)
```json
{ "montant": 200.00, "datePaiement": "2026-11-01", "modePaiement": "CARTE" }
```
```json
{ "montant": 140.00, "datePaiement": "2026-11-06", "modePaiement": "ESPECES" }
```

## 8. Maintenance — `POST /api/maintenances`

```json
{ "dateDebut": "2026-10-25", "dateFin": "2026-10-27", "description": "Vidange et plaquettes de frein" }
```

## 9. Les scénarios d'affectation (vehicule-controller / employe-controller)

| Scénario | Endpoint Swagger | Effet en base |
|---|---|---|
| Karim (1) rattaché à Sousse (3) | `PUT /api/employes/1/agence/3` | `employe.agence_id = 3` |
| Mutation vers Tunis — sans désaffecter | `PUT /api/employes/1/agence/1` | **409** : déjà dans une agence |
| 1) désaffecter | `PUT /api/employes/1/desaffecter` | `agence_id = NULL` |
| 2) affecter à Tunis | `PUT /api/employes/1/agence/1` | `agence_id = 1` |
| Ines (2) et Sami (3) à Tunis | `PUT /api/employes/2/agence/1`, `PUT /api/employes/3/agence/1` | |
| Véhicule neuf rattaché à Sfax | `PUT /api/vehicules/1/agence/2` | `vehicule.agence_id = 2` |
| Autres véhicules | `PUT /api/vehicules/2/agence/1`, `3/agence/1`, `4/agence/3` | |
| Siège bébé (1) installé sur la C3 (1) | `PUT /api/vehicules/1/equipements/1` | ligne `(1, 1)` dans `vehicule_equipement` |
| GPS (2) sur la 508 et le Sportage | `PUT /api/vehicules/2/equipements/2`, `PUT /api/vehicules/3/equipements/2` | 2 lignes |
| Retrait du siège bébé au retour | `DELETE /api/vehicules/1/equipements/1` | ligne `(1, 1)` supprimée |

Pour la capture finale, réinstalle le siège bébé (`PUT /api/vehicules/1/equipements/1`) afin que
`vehicule_equipement` ne soit pas vide, ou fais une capture avant et une après le retrait.

## 10. Captures dans phpMyAdmin

- `agence`, `employe` (colonne `agence_id` remplie), `vehicule` (colonne `agence_id`),
  `equipement`, `vehicule_equipement`, `client`, `reservation`, `contrat`, `paiement` (colonne `contrat_id`).
