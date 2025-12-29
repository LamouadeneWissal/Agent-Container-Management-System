package com.ex.gestion_conteneurs_agents.strategy;

import com.ex.gestion_conteneurs_agents.model.Transaction;
import com.ex.gestion_conteneurs_agents.observer.NotificationEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stratégie d'historique pour garder trace des transactions notifiées.
 * Conserve l'historique complet des transactions reçues via notifications.
 * 
 * Design Pattern: STRATEGY
 * - Implémentation concrète de la stratégie d'historique
 */
@Component
public class HistoryStrategy implements NotificationStrategy {
    
    private final List<NotificationEvent> historique = new ArrayList<>();

    @Override
    public void handleNotification(NotificationEvent event) {
        historique.add(event);
        
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ [HISTORY STRATEGY] Transaction ajoutée à l'historique      ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ Agent source: " + event.getAgentName());
        System.out.println("║ Transaction ID: " + event.getTransaction().getId());
        System.out.println("║ Taille de l'historique: " + historique.size() + " transaction(s)");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    @Override
    public String getStrategyName() {
        return "HistoryStrategy";
    }

    /**
     * Retourne l'historique complet des notifications.
     * @return liste non modifiable des événements
     */
    public List<NotificationEvent> getHistorique() {
        return Collections.unmodifiableList(historique);
    }

    /**
     * Retourne uniquement les transactions de l'historique.
     * @return liste des transactions
     */
    public List<Transaction> getTransactionsHistory() {
        return historique.stream()
                .map(NotificationEvent::getTransaction)
                .toList();
    }

    /**
     * Retourne le nombre de transactions dans l'historique.
     * @return la taille de l'historique
     */
    public int getHistorySize() {
        return historique.size();
    }

    /**
     * Vide l'historique.
     */
    public void clearHistory() {
        historique.clear();
    }

    /**
     * Affiche l'historique complet.
     */
    public void displayHistory() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              HISTORIQUE DES NOTIFICATIONS                  ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        if (historique.isEmpty()) {
            System.out.println("║ Aucune notification dans l'historique                      ║");
        } else {
            int index = 1;
            for (NotificationEvent event : historique) {
                System.out.println("║ " + index + ". Agent: " + event.getAgentName() + 
                        " | Transaction: " + event.getTransaction().getId() +
                        " | Montant: " + String.format("%.2f", event.getTransaction().getMontant()) + " €");
                index++;
            }
        }
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
}
