package com.ex.gestion_conteneurs_agents.strategy;

import com.ex.gestion_conteneurs_agents.observer.NotificationEvent;

/**
 * Interface Strategy pour le traitement des notifications.
 * 
 * Design Pattern utilisé: STRATEGY
 * - Définit une famille d'algorithmes interchangeables
 * - Permet de changer l'algorithme utilisé au moment de l'exécution
 * - Rend le code extensible (Open/Closed Principle)
 */
public interface NotificationStrategy {
    /**
     * Traite l'événement de notification selon la stratégie implémentée.
     * @param event l'événement de notification à traiter
     */
    void handleNotification(NotificationEvent event);

    /**
     * Retourne le nom de la stratégie.
     * @return le nom descriptif de la stratégie
     */
    String getStrategyName();
}
