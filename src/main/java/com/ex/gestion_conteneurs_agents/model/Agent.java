package com.ex.gestion_conteneurs_agents.model;

import com.ex.gestion_conteneurs_agents.aspects.annotations.Cachable;
import com.ex.gestion_conteneurs_agents.aspects.annotations.Log;
import com.ex.gestion_conteneurs_agents.observer.NotificationEvent;
import com.ex.gestion_conteneurs_agents.observer.Observer;
import com.ex.gestion_conteneurs_agents.observer.Subject;
import com.ex.gestion_conteneurs_agents.strategy.DefaultStrategy;
import com.ex.gestion_conteneurs_agents.strategy.NotificationStrategy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Classe Agent implémentant les patterns Observer et Strategy.
 * 
 * Design Patterns utilisés:
 * - OBSERVER (Subject + Observer): Permet aux agents de s'observer mutuellement
 * - STRATEGY: Permet de changer dynamiquement le comportement de traitement des notifications
 * 
 * Un agent:
 * - Gère ses propres transactions
 * - Peut souscrire aux notifications d'autres agents (Observer)
 * - Peut notifier ses observateurs lors de l'ajout d'une transaction (Subject)
 * - Peut changer sa stratégie de traitement des notifications (Strategy)
 */
@Component
@Getter
public class Agent implements Subject, Observer {

    private final String nom;
    private final List<Transaction> transactions;
    private final List<Observer> observers;
    
    @Setter
    private NotificationStrategy strategy;

    /**
     * Constructeur par défaut avec nom générique.
     */
    public Agent() {
        this("Agent-" + System.currentTimeMillis());
    }

    /**
     * Constructeur avec nom spécifié.
     * @param nom le nom de l'agent
     */
    public Agent(String nom) {
        this.nom = nom;
        this.transactions = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.strategy = new DefaultStrategy(); // Stratégie par défaut
    }

    // ==================== GESTION DES TRANSACTIONS ====================

    /**
     * Ajoute une transaction et notifie tous les observateurs.
     * @param transaction la transaction à ajouter
     */
    @Log
    public void ajouterTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("La transaction ne peut pas être null");
        }
        transactions.add(transaction);
        System.out.println("\n✅ Transaction ajoutée à l'agent [" + nom + "]: " + transaction.getId());
        
        // Notification des observateurs
        NotificationEvent event = new NotificationEvent(this.nom, transaction);
        notifyObservers(event);
    }

    /**
     * Supprime une transaction par son ID.
     * @param transactionId l'ID de la transaction à supprimer
     * @return true si la transaction a été supprimée, false sinon
     */
    @Log
    public boolean supprimerTransaction(String transactionId) {
        return transactions.removeIf(t -> t.getId().equals(transactionId));
    }

    /**
     * Recherche une transaction par son ID.
     * @param transactionId l'ID de la transaction
     * @return Optional contenant la transaction si trouvée
     */
    public Optional<Transaction> rechercherTransaction(String transactionId) {
        return transactions.stream()
                .filter(t -> t.getId().equals(transactionId))
                .findFirst();
    }

    /**
     * Retourne la transaction avec le montant le plus grand.
     * Méthode annotée pour le cache (Aspect @Cachable).
     * @return Optional contenant la transaction avec le plus grand montant
     */
    @Cachable
    @Log
    public Optional<Transaction> getTransactionMaxMontant() {
        return transactions.stream()
                .max(Comparator.comparingDouble(Transaction::getMontant));
    }

    /**
     * Calcule le solde total des transactions.
     * @return le solde (ventes - achats)
     */
    @Log
    public double calculerSolde() {
        return transactions.stream()
                .mapToDouble(t -> {
                    switch (t.getType()) {
                        case VENTE: return t.getMontant();
                        case ACHAT: return -t.getMontant();
                        default: return 0;
                    }
                })
                .sum();
    }

    // ==================== PATTERN OBSERVER - SUBJECT ====================

    /**
     * Enregistre un observateur (souscription d'un agent).
     */
    @Override
    @Log
    public void subscribe(Observer observer) {
        if (observer != null && !observers.contains(observer) && observer != this) {
            observers.add(observer);
            System.out.println("📌 [" + ((Agent) observer).getNom() + "] s'est abonné à [" + nom + "]");
        }
    }

    /**
     * Désinscrit un observateur.
     */
    @Override
    public void unsubscribe(Observer observer) {
        if (observers.remove(observer)) {
            System.out.println("📌 [" + ((Agent) observer).getNom() + "] s'est désabonné de [" + nom + "]");
        }
    }

    /**
     * Notifie tous les observateurs enregistrés.
     */
    @Override
    public void notifyObservers(NotificationEvent event) {
        System.out.println("🔔 Notification de " + observers.size() + " observateur(s) par [" + nom + "]");
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    // ==================== PATTERN OBSERVER - OBSERVER ====================

    /**
     * Méthode appelée lors de la réception d'une notification.
     * Utilise la stratégie configurée pour traiter la notification.
     */
    @Override
    @Log
    public void update(NotificationEvent event) {
        System.out.println("\n📨 [" + nom + "] reçoit une notification de [" + event.getAgentName() + "]");
        strategy.handleNotification(event);
    }

    // ==================== PATTERN STRATEGY ====================

    /**
     * Change la stratégie de traitement des notifications.
     * @param newStrategy la nouvelle stratégie à utiliser
     */
    @Log
    public void changerStrategie(NotificationStrategy newStrategy) {
        if (newStrategy == null) {
            throw new IllegalArgumentException("La stratégie ne peut pas être null");
        }
        System.out.println("🔄 [" + nom + "] change de stratégie: " + 
                strategy.getStrategyName() + " → " + newStrategy.getStrategyName());
        this.strategy = newStrategy;
    }

    // ==================== AFFICHAGE ====================

    /**
     * Affiche les informations de l'agent et ses transactions.
     */
    @Log
    public void afficher() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                        AGENT: " + nom);
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Stratégie active: " + strategy.getStrategyName());
        System.out.println("║ Nombre de transactions: " + transactions.size());
        System.out.println("║ Nombre d'observateurs: " + observers.size());
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║                      TRANSACTIONS                            ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        
        if (transactions.isEmpty()) {
            System.out.println("║ Aucune transaction                                           ║");
        } else {
            for (Transaction t : transactions) {
                System.out.println("║ ID: " + t.getId() + 
                        " | Date: " + t.getDate().format(formatter) +
                        " | Type: " + t.getType() +
                        " | Montant: " + String.format("%.2f", t.getMontant()) + " €");
            }
        }
        
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ SOLDE TOTAL: " + String.format("%.2f", calculerSolde()) + " €");
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
    }

    /**
     * Affiche un résumé compact de l'agent.
     */
    public void afficherResume() {
        System.out.println("Agent [" + nom + "] - " + 
                transactions.size() + " transaction(s) - Solde: " + 
                String.format("%.2f", calculerSolde()) + " €");
    }

    @Override
    public String toString() {
        return "Agent{nom='" + nom + "', transactions=" + transactions.size() + 
                ", observers=" + observers.size() + ", strategy=" + strategy.getStrategyName() + "}";
    }
}
