package com.ex.gestion_conteneurs_agents.adapter;

/**
 * Implémentation concrète d'un afficheur VGA.
 * Cet afficheur n'est PAS compatible avec l'interface HDMI standard.
 */
public class VGAMonitor implements VGADisplay {

    private int width = 800;
    private int height = 600;

    @Override
    public void displayViaVGA(String text) {
        System.out.println("\n┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│           🖥️  VGA MONITOR - AFFICHAGE                        │");
        System.out.println("│           Résolution: " + width + "x" + height);
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println(text);
        System.out.println("└──────────────────────────────────────────────────────────────┘\n");
    }

    @Override
    public void setResolution(int width, int height) {
        this.width = width;
        this.height = height;
        System.out.println("📐 Résolution VGA configurée: " + width + "x" + height);
    }
}
