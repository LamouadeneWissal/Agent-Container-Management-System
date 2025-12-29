package com.ex.gestion_conteneurs_agents.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Contexte de sécurité pour gérer l'authentification et les autorisations.
 * Simule un système d'authentification simple avec username/password et rôles.
 * 
 * Dans une application réelle, ceci serait remplacé par Spring Security.
 */
public class SecurityContext {

    // Base de données simulée des utilisateurs
    private static final Map<String, UserCredentials> USERS = new HashMap<>();
    
    // Utilisateur actuellement connecté (ThreadLocal pour la sécurité multi-thread)
    private static final ThreadLocal<UserCredentials> currentUser = new ThreadLocal<>();

    // Initialisation des utilisateurs par défaut
    static {
        // Administrateur avec tous les droits
        USERS.put("admin", new UserCredentials("admin", "admin123", Set.of("ADMIN", "MANAGER", "USER")));
        // Manager avec droits limités
        USERS.put("manager", new UserCredentials("manager", "manager123", Set.of("MANAGER", "USER")));
        // Utilisateur standard
        USERS.put("user", new UserCredentials("user", "user123", Set.of("USER")));
        // Invité avec accès lecture seule
        USERS.put("guest", new UserCredentials("guest", "guest123", Set.of("GUEST")));
    }

    /**
     * Structure pour stocker les credentials d'un utilisateur.
     */
    public record UserCredentials(String username, String password, Set<String> roles) {}

    /**
     * Authentifie un utilisateur avec son username et mot de passe.
     * @param username le nom d'utilisateur
     * @param password le mot de passe
     * @return true si l'authentification réussit, false sinon
     */
    public static boolean login(String username, String password) {
        UserCredentials user = USERS.get(username);
        
        if (user != null && user.password().equals(password)) {
            currentUser.set(user);
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║ ✅ AUTHENTIFICATION RÉUSSIE                                  ║");
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║ 👤 Utilisateur: " + username);
            System.out.println("║ 🎭 Rôles: " + user.roles());
            System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
            return true;
        }
        
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║ ❌ AUTHENTIFICATION ÉCHOUÉE                                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ 👤 Utilisateur: " + username);
        System.out.println("║ 💡 Vérifiez vos identifiants                                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
        return false;
    }

    /**
     * Déconnecte l'utilisateur actuel.
     */
    public static void logout() {
        UserCredentials user = currentUser.get();
        if (user != null) {
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║ 👋 DÉCONNEXION                                               ║");
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║ 👤 Utilisateur déconnecté: " + user.username());
            System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
        }
        currentUser.remove();
    }

    /**
     * Vérifie si un utilisateur est actuellement authentifié.
     * @return true si un utilisateur est connecté
     */
    public static boolean isAuthenticated() {
        return currentUser.get() != null;
    }

    /**
     * Retourne le nom d'utilisateur actuellement connecté.
     * @return le nom d'utilisateur ou null si non connecté
     */
    public static String getCurrentUsername() {
        UserCredentials user = currentUser.get();
        return user != null ? user.username() : null;
    }

    /**
     * Retourne les rôles de l'utilisateur actuellement connecté.
     * @return set des rôles ou set vide si non connecté
     */
    public static Set<String> getCurrentUserRoles() {
        UserCredentials user = currentUser.get();
        return user != null ? user.roles() : new HashSet<>();
    }

    /**
     * Vérifie si l'utilisateur actuel possède un rôle spécifique.
     * @param role le rôle à vérifier
     * @return true si l'utilisateur possède le rôle
     */
    public static boolean hasRole(String role) {
        return getCurrentUserRoles().contains(role);
    }

    /**
     * Ajoute un nouvel utilisateur au système.
     * @param username le nom d'utilisateur
     * @param password le mot de passe
     * @param roles les rôles de l'utilisateur
     */
    public static void addUser(String username, String password, Set<String> roles) {
        USERS.put(username, new UserCredentials(username, password, roles));
        System.out.println("👤 Utilisateur '" + username + "' ajouté avec les rôles: " + roles);
    }

    /**
     * Affiche tous les utilisateurs disponibles (pour le debug).
     */
    public static void displayAvailableUsers() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║           UTILISATEURS DISPONIBLES                           ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        USERS.forEach((username, creds) -> 
            System.out.println("║ 👤 " + username + " / " + creds.password() + " → Rôles: " + creds.roles())
        );
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
    }
}
