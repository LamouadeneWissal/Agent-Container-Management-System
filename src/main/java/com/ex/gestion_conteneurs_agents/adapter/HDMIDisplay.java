package com.ex.gestion_conteneurs_agents.adapter;

/**
 * Interface standard HDMI pour l'affichage.
 * Tout afficheur implémentant cette interface peut être connecté au conteneur.
 * 
 * Design Pattern: ADAPTER (Interface cible)
 * - Définit l'interface standard que le conteneur utilise pour l'affichage
 */
public interface HDMIDisplay {
    
    /**
     * Affiche le contenu via la connexion HDMI.
     * @param content le contenu à afficher
     */
    void displayViaHDMI(String content);
    
    /**
     * Retourne le nom de l'afficheur.
     * @return le nom de l'afficheur
     */
    String getDisplayName();
}
