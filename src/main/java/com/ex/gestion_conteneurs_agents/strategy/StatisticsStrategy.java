package com.ex.gestion_conteneurs_agents.strategy;

import com.ex.gestion_conteneurs_agents.enums.TransactionType;
import com.ex.gestion_conteneurs_agents.observer.NotificationEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Stratégie de statistiques pour analyser les transactions.
 * Calcule des statistiques sur les transactions notifiées.
 * 
 * Design Pattern: STRATEGY
 * - Autre implémentation extensible démontrant la flexibilité
 */
@Component
public class StatisticsStrategy implements NotificationStrategy {
    
    private int totalVentes = 0;
    private int totalAchats = 0;
    private double montantTotalVentes = 0.0;
    private double montantTotalAchats = 0.0;
    private final Map<String, Integer> transactionsParAgent = new HashMap<>();

    @Override
    public void handleNotification(NotificationEvent event) {
        // Mise à jour des statistiques
        String agentName = event.getAgentName();
        transactionsParAgent.merge(agentName, 1, Integer::sum);

        if (event.getTransaction().getType() == TransactionType.VENTE) {
            totalVentes++;
            montantTotalVentes += event.getTransaction().getMontant();
        } else {
            totalAchats++;
            montantTotalAchats += event.getTransaction().getMontant();
        }

        // Affichage des statistiques mises à jour
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ [STATISTICS STRATEGY] Statistiques mises à jour            ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ Total ventes: " + totalVentes + " (Montant: " + String.format("%.2f", montantTotalVentes) + " €)");
        System.out.println("║ Total achats: " + totalAchats + " (Montant: " + String.format("%.2f", montantTotalAchats) + " €)");
        System.out.println("║ Transactions de " + agentName + ": " + transactionsParAgent.get(agentName));
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    @Override
    public String getStrategyName() {
        return "StatisticsStrategy";
    }

    public void displayFullStatistics() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              STATISTIQUES COMPLÈTES                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ VENTES: " + totalVentes + " transactions | Total: " + String.format("%.2f", montantTotalVentes) + " €");
        System.out.println("║ ACHATS: " + totalAchats + " transactions | Total: " + String.format("%.2f", montantTotalAchats) + " €");
        System.out.println("║ SOLDE NET: " + String.format("%.2f", (montantTotalVentes - montantTotalAchats)) + " €");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ TRANSACTIONS PAR AGENT:                                    ║");
        transactionsParAgent.forEach((agent, count) -> 
            System.out.println("║   - " + agent + ": " + count + " transaction(s)"));
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }

    public void reset() {
        totalVentes = 0;
        totalAchats = 0;
        montantTotalVentes = 0.0;
        montantTotalAchats = 0.0;
        transactionsParAgent.clear();
    }
}
