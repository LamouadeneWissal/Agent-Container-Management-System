package com.ex.gestion_conteneurs_agents.strategy;

import com.ex.gestion_conteneurs_agents.observer.NotificationEvent;
import org.springframework.stereotype.Component;

/**
 * Stratégie par défaut pour le traitement des notifications.
 * Affiche simplement les informations de la notification dans la console.
 */
@Component
public class DefaultStrategy implements NotificationStrategy {

    @Override
    public void handleNotification(NotificationEvent event) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ [DEFAULT STRATEGY] Notification reçue                      ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ Agent source: " + event.getAgentName());
        System.out.println("║ Transaction ID: " + event.getTransaction().getId());
        System.out.println("║ Montant: " + String.format("%.2f", event.getTransaction().getMontant()) + " €");
        System.out.println("║ Type: " + event.getTransaction().getType());
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    @Override
    public String getStrategyName() {
        return "DefaultStrategy";
    }
}
