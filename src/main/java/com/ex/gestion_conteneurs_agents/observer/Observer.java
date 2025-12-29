package com.ex.gestion_conteneurs_agents.observer;

/**
 * Interface Observer pour le pattern Observer.
 * Définit le contrat que doivent implémenter les observateurs.
 * 
 * Design Pattern utilisé: OBSERVER
 * - Permet de définir une dépendance un-à-plusieurs entre objets
 * - Quand un objet change d'état, tous ses dépendants sont notifiés
 */
public interface Observer {
    /**
     * Méthode appelée lorsqu'un événement est notifié.
     * @param event l'événement de notification contenant les informations
     */
    void update(NotificationEvent event);
}
