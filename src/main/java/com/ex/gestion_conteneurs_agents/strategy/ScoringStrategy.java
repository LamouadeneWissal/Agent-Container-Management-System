package com.ex.gestion_conteneurs_agents.strategy;

import com.ex.gestion_conteneurs_agents.enums.TransactionType;
import com.ex.gestion_conteneurs_agents.observer.NotificationEvent;
import org.springframework.stereotype.Component;

/**
 * Stratégie de Scoring pour le calcul du solde.
 * Calcule un solde en ajoutant (VENTE) ou retranchant (ACHAT) le montant.
 * 
 * Design Pattern: STRATEGY
 * - Implémentation concrète de la stratégie de scoring
 */
@Component
public class ScoringStrategy implements NotificationStrategy {
    
    private double solde = 0.0;

    @Override
    public void handleNotification(NotificationEvent event) {
        double montant = event.getTransaction().getMontant();
        TransactionType type = event.getTransaction().getType();

        if (type == TransactionType.VENTE) {
            solde += montant;
            System.out.println("╔════════════════════════════════════════════════════════════╗");
            System.out.println("║ [SCORING STRATEGY] Transaction VENTE                       ║");
            System.out.println("╠════════════════════════════════════════════════════════════╣");
            System.out.println("║ Agent source: " + event.getAgentName());
            System.out.println("║ Montant ajouté: +" + String.format("%.2f", montant) + " €");
            System.out.println("║ Nouveau solde: " + String.format("%.2f", solde) + " €");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
        } else if (type == TransactionType.ACHAT) {
            solde -= montant;
            System.out.println("╔════════════════════════════════════════════════════════════╗");
            System.out.println("║ [SCORING STRATEGY] Transaction ACHAT                       ║");
            System.out.println("╠════════════════════════════════════════════════════════════╣");
            System.out.println("║ Agent source: " + event.getAgentName());
            System.out.println("║ Montant retranché: -" + String.format("%.2f", montant) + " €");
            System.out.println("║ Nouveau solde: " + String.format("%.2f", solde) + " €");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
        }
    }

    @Override
    public String getStrategyName() {
        return "ScoringStrategy";
    }

    /**
     * Retourne le solde actuel calculé.
     * @return le solde
     */
    public double getSolde() {
        return solde;
    }

    /**
     * Réinitialise le solde à zéro.
     */
    public void resetSolde() {
        this.solde = 0.0;
    }
}
