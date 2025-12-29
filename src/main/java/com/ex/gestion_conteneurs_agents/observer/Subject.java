package com.ex.gestion_conteneurs_agents.observer;

/**
 * Interface Subject pour le pattern Observer.
 * Définit le contrat pour les objets observables.
 * 
 * Design Pattern utilisé: OBSERVER
 * - Permet aux observateurs de s'enregistrer/se désinscrire
 * - Permet de notifier tous les observateurs enregistrés
 */
public interface Subject {
    /**
     * Enregistre un observateur (souscription).
     * @param observer l'observateur à enregistrer
     */
    void subscribe(Observer observer);

    /**
     * Désinscrit un observateur.
     * @param observer l'observateur à désinscrire
     */
    void unsubscribe(Observer observer);

    /**
     * Notifie tous les observateurs enregistrés.
     * @param event l'événement à transmettre aux observateurs
     */
    void notifyObservers(NotificationEvent event);
}
