package com.ex.gestion_conteneurs_agents.adapter;

/**
 * Interface VGA pour les anciens afficheurs.
 * Cette interface est incompatible avec l'interface standard HDMI du conteneur.
 * 
 * Design Pattern: ADAPTER (Interface existante à adapter)
 */
public interface VGADisplay {
    
    /**
     * Affiche le contenu via la connexion VGA.
     * @param text le texte à afficher
     */
    void displayViaVGA(String text);
    
    /**
     * Configure la résolution VGA.
     * @param width largeur
     * @param height hauteur
     */
    void setResolution(int width, int height);
}
