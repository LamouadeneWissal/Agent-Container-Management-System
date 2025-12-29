package com.ex.gestion_conteneurs_agents.aspects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation @SecuredBy pour la sécurisation des méthodes par rôles.
 * Les méthodes annotées nécessitent que l'utilisateur ait un des rôles spécifiés.
 * 
 * Utilisé avec l'aspect SecurityAspect pour implémenter la sécurité AOP.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecuredBy {
    /**
     * Liste des rôles autorisés à exécuter cette méthode.
     */
    String[] roles();
}
