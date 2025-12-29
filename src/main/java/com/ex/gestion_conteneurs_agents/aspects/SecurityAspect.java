package com.ex.gestion_conteneurs_agents.aspects;

import com.ex.gestion_conteneurs_agents.aspects.annotations.SecuredBy;
import com.ex.gestion_conteneurs_agents.security.SecurityContext;
import com.ex.gestion_conteneurs_agents.security.SecurityException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

/**
 * Aspect de sécurité (Security).
 * 
 * Programmation Orientée Aspect (AOP):
 * - Intercepte les méthodes annotées avec @SecuredBy
 * - Vérifie que l'utilisateur authentifié possède un des rôles requis
 * - Bloque l'exécution si l'utilisateur n'est pas autorisé
 * - Sépare la logique de sécurité du code métier
 */
@Aspect
@Component
public class SecurityAspect {

    /**
     * Advice Around qui intercepte les méthodes annotées @SecuredBy.
     * Vérifie les permissions avant d'exécuter la méthode.
     */
    @Around("@annotation(securedBy)")
    public Object checkSecurity(ProceedingJoinPoint joinPoint, SecuredBy securedBy) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = signature.getName();
        String[] requiredRoles = securedBy.roles();

        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 🔐 [SECURITY CHECK] Vérification des permissions");
        System.out.println("│ 📦 Classe: " + className);
        System.out.println("│ 🔧 Méthode: " + methodName);
        System.out.println("│ 🎫 Rôles requis: " + Arrays.toString(requiredRoles));
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        // Vérification de l'authentification
        if (!SecurityContext.isAuthenticated()) {
            System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
            System.out.println("│ ❌ [SECURITY DENIED] Utilisateur non authentifié");
            System.out.println("│ 💡 Veuillez vous connecter avec SecurityContext.login()");
            System.out.println("└─────────────────────────────────────────────────────────────┘");
            throw new SecurityException("Accès refusé: Utilisateur non authentifié");
        }

        // Récupération des informations de l'utilisateur
        String username = SecurityContext.getCurrentUsername();
        Set<String> userRoles = SecurityContext.getCurrentUserRoles();

        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 👤 Utilisateur: " + username);
        System.out.println("│ 🎭 Rôles de l'utilisateur: " + userRoles);
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        // Vérification des rôles
        boolean hasRequiredRole = Arrays.stream(requiredRoles)
                .anyMatch(userRoles::contains);

        if (!hasRequiredRole) {
            System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
            System.out.println("│ ❌ [SECURITY DENIED] Permissions insuffisantes");
            System.out.println("│ 👤 Utilisateur: " + username);
            System.out.println("│ 🎭 Rôles de l'utilisateur: " + userRoles);
            System.out.println("│ 🎫 Rôles requis: " + Arrays.toString(requiredRoles));
            System.out.println("└─────────────────────────────────────────────────────────────┘");
            throw new SecurityException("Accès refusé: L'utilisateur '" + username + 
                    "' n'a pas les permissions requises. Rôles nécessaires: " + Arrays.toString(requiredRoles));
        }

        // Autorisation accordée
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ ✅ [SECURITY GRANTED] Accès autorisé");
        System.out.println("│ 👤 Utilisateur: " + username);
        System.out.println("│ 🎫 Rôle utilisé: " + findMatchingRole(requiredRoles, userRoles));
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        // Exécution de la méthode sécurisée
        return joinPoint.proceed();
    }

    /**
     * Trouve le premier rôle correspondant entre les rôles requis et ceux de l'utilisateur.
     */
    private String findMatchingRole(String[] requiredRoles, Set<String> userRoles) {
        return Arrays.stream(requiredRoles)
                .filter(userRoles::contains)
                .findFirst()
                .orElse("N/A");
    }
}
