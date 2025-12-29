package com.ex.gestion_conteneurs_agents.adapter;

import org.springframework.stereotype.Component;

/**
 * Implémentation concrète d'un afficheur HDMI.
 * Cet afficheur est nativement compatible avec l'interface standard.
 */
@Component
public class HDMIMonitor implements HDMIDisplay {

    @Override
    public void displayViaHDMI(String content) {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║           📺 HDMI MONITOR - AFFICHAGE                        ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println(content);
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
    }

    @Override
    public String getDisplayName() {
        return "HDMI Monitor Standard";
    }
}
