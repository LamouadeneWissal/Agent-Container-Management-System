package com.ex.gestion_conteneurs_agents.strategy;

import com.ex.gestion_conteneurs_agents.observer.NotificationEvent;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stratégie de journalisation pour écrire les notifications dans un fichier log.
 * Exemple de stratégie supplémentaire démontrant l'extensibilité du pattern.
 * 
 * Design Pattern: STRATEGY
 * - Implémentation concrète extensible
 */
@Component
public class LoggingStrategy implements NotificationStrategy {
    
    private static final String LOG_FILE = "notifications.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void handleNotification(NotificationEvent event) {
        String logEntry = String.format("[%s] Agent: %s | Transaction: %s | Type: %s | Montant: %.2f €",
                LocalDateTime.now().format(FORMATTER),
                event.getAgentName(),
                event.getTransaction().getId(),
                event.getTransaction().getType(),
                event.getTransaction().getMontant());

        // Écriture dans le fichier log
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(logEntry);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier log: " + e.getMessage());
        }

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ [LOGGING STRATEGY] Notification journalisée                ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ " + logEntry);
        System.out.println("║ Écrit dans: " + LOG_FILE);
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    @Override
    public String getStrategyName() {
        return "LoggingStrategy";
    }
}
