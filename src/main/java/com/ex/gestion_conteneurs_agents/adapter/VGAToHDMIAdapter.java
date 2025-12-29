package com.ex.gestion_conteneurs_agents.adapter;

import org.springframework.stereotype.Component;

/**
 * Adaptateur VGA vers HDMI.
 * Permet de connecter un afficheur VGA au conteneur via l'interface standard HDMI.
 * 
 * Design Pattern: ADAPTER (Adaptateur)
 * - Convertit l'interface VGA en interface HDMI
 * - Permet la compatibilité entre interfaces incompatibles
 * - Le conteneur peut utiliser n'importe quel afficheur via cet adaptateur
 */
@Component
public class VGAToHDMIAdapter implements HDMIDisplay {

    private final VGADisplay vgaDisplay;

    /**
     * Constructeur de l'adaptateur.
     * @param vgaDisplay l'afficheur VGA à adapter
     */
    public VGAToHDMIAdapter(VGADisplay vgaDisplay) {
        this.vgaDisplay = vgaDisplay;
        // Configuration par défaut de la résolution
        this.vgaDisplay.setResolution(1920, 1080);
    }

    /**
     * Constructeur par défaut avec un VGAMonitor.
     */
    public VGAToHDMIAdapter() {
        this(new VGAMonitor());
    }

    /**
     * Adapte l'appel HDMI vers VGA.
     * Convertit le signal HDMI en format compatible VGA.
     */
    @Override
    public void displayViaHDMI(String content) {
        System.out.println("🔌 [ADAPTER] Conversion HDMI → VGA en cours...");
        // Conversion du contenu si nécessaire (ici simple passage)
        vgaDisplay.displayViaVGA(content);
    }

    @Override
    public String getDisplayName() {
        return "VGA Monitor (via HDMI Adapter)";
    }

    /**
     * Permet de configurer la résolution du moniteur VGA sous-jacent.
     */
    public void configureVGAResolution(int width, int height) {
        vgaDisplay.setResolution(width, height);
    }
}
