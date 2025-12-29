package com.ex.gestion_conteneurs_agents.model;

import com.ex.gestion_conteneurs_agents.enums.TransactionType;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe Transaction utilisant le Pattern Builder.
 * Une transaction est définie par son id, sa date, son montant et son type.
 * 
 * Design Pattern utilisé: BUILDER
 * - Permet de construire des objets complexes étape par étape
 * - Facilite la création d'objets avec de nombreux paramètres
 * - Rend le code plus lisible et maintenable
 */
@Getter
@ToString
public class Transaction {
    private final String id;
    private final LocalDateTime date;
    private final double montant;
    private final TransactionType type;

    // Constructeur privé - seul le Builder peut créer une Transaction
    private Transaction(TransactionBuilder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.montant = builder.montant;
        this.type = builder.type;
    }

    /**
     * Méthode statique pour obtenir une instance du Builder.
     * @return une nouvelle instance de TransactionBuilder
     */
    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    /**
     * Affiche les détails de la transaction de manière formatée.
     */
    public void afficher() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.println("│ Transaction ID: " + id);
        System.out.println("│ Date: " + date.format(formatter));
        System.out.println("│ Montant: " + String.format("%.2f", montant) + " €");
        System.out.println("│ Type: " + type.getLabel());
        System.out.println("└─────────────────────────────────────────────────┘");
    }

    /**
     * Classe Builder interne pour construire des objets Transaction.
     * Implémente le pattern Builder avec une interface fluide.
     */
    public static class TransactionBuilder {
        private String id;
        private LocalDateTime date;
        private double montant;
        private TransactionType type;

        public TransactionBuilder id(String id) {
            this.id = id;
            return this;
        }

        public TransactionBuilder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public TransactionBuilder montant(double montant) {
            this.montant = montant;
            return this;
        }

        public TransactionBuilder type(TransactionType type) {
            this.type = type;
            return this;
        }

        /**
         * Construit l'objet Transaction avec validation.
         * @return l'instance de Transaction créée
         * @throws IllegalStateException si des champs obligatoires sont manquants
         */
        public Transaction build() {
            // Validation des champs obligatoires
            if (id == null || id.isEmpty()) {
                throw new IllegalStateException("L'ID de la transaction est obligatoire");
            }
            if (date == null) {
                this.date = LocalDateTime.now(); // Date par défaut
            }
            if (type == null) {
                throw new IllegalStateException("Le type de transaction est obligatoire");
            }
            if (montant < 0) {
                throw new IllegalStateException("Le montant ne peut pas être négatif");
            }
            return new Transaction(this);
        }
    }
}
