package com.ex.gestion_conteneurs_agents.security;

/**
 * Exception personnalisée pour les erreurs de sécurité.
 * Lancée lorsqu'un utilisateur tente d'accéder à une ressource non autorisée.
 */
public class SecurityException extends RuntimeException {

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
