package com.ex.gestion_conteneurs_agents.aspects;

import com.ex.gestion_conteneurs_agents.aspects.annotations.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Aspect de journalisation (Logging).
 * 
 * Programmation Orientée Aspect (AOP):
 * - Intercepte les méthodes annotées avec @Log
 * - Mesure et affiche la durée d'exécution de chaque méthode
 * - Sépare la préoccupation de journalisation du code métier
 */
@Aspect
@Component
public class LoggingAspect {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Advice Around qui intercepte les méthodes annotées @Log.
     * Mesure le temps d'exécution et affiche les informations de journalisation.
     */
    @Around("@annotation(log)")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint, Log log) throws Throwable {
        // Récupération des informations de la méthode
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = signature.getName();
        String customMessage = log.message();
        String level = log.level();

        // Timestamp de début
        LocalDateTime startTime = LocalDateTime.now();
        long startNanos = System.nanoTime();

        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 📋 [" + level + "] LOG - DÉBUT D'EXÉCUTION");
        System.out.println("│ 🕐 Timestamp: " + startTime.format(FORMATTER));
        System.out.println("│ 📦 Classe: " + className);
        System.out.println("│ 🔧 Méthode: " + methodName);
        if (!customMessage.isEmpty()) {
            System.out.println("│ 💬 Message: " + customMessage);
        }
        System.out.println("│ 📥 Arguments: " + formatArguments(joinPoint.getArgs()));
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        Object result;
        try {
            // Exécution de la méthode originale
            result = joinPoint.proceed();
            
            // Calcul de la durée
            long endNanos = System.nanoTime();
            double durationMs = (endNanos - startNanos) / 1_000_000.0;
            LocalDateTime endTime = LocalDateTime.now();

            System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
            System.out.println("│ ✅ [" + level + "] LOG - FIN D'EXÉCUTION (SUCCÈS)");
            System.out.println("│ 🕐 Timestamp: " + endTime.format(FORMATTER));
            System.out.println("│ 📦 Classe: " + className);
            System.out.println("│ 🔧 Méthode: " + methodName);
            System.out.println("│ ⏱️  Durée d'exécution: " + String.format("%.3f", durationMs) + " ms");
            System.out.println("│ 📤 Résultat: " + formatResult(result));
            System.out.println("└─────────────────────────────────────────────────────────────┘");

            return result;

        } catch (Throwable throwable) {
            // En cas d'exception
            long endNanos = System.nanoTime();
            double durationMs = (endNanos - startNanos) / 1_000_000.0;
            LocalDateTime endTime = LocalDateTime.now();

            System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
            System.out.println("│ ❌ [ERROR] LOG - FIN D'EXÉCUTION (ÉCHEC)");
            System.out.println("│ 🕐 Timestamp: " + endTime.format(FORMATTER));
            System.out.println("│ 📦 Classe: " + className);
            System.out.println("│ 🔧 Méthode: " + methodName);
            System.out.println("│ ⏱️  Durée d'exécution: " + String.format("%.3f", durationMs) + " ms");
            System.out.println("│ 💥 Exception: " + throwable.getClass().getSimpleName() + " - " + throwable.getMessage());
            System.out.println("└─────────────────────────────────────────────────────────────┘");

            throw throwable;
        }
    }

    /**
     * Formate les arguments pour l'affichage.
     */
    private String formatArguments(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(args[i] != null ? truncate(args[i].toString(), 50) : "null");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Formate le résultat pour l'affichage.
     */
    private String formatResult(Object result) {
        if (result == null) {
            return "void/null";
        }
        return truncate(result.toString(), 100);
    }

    /**
     * Tronque une chaîne si elle dépasse la longueur maximale.
     */
    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }
}
