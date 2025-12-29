package com.ex.gestion_conteneurs_agents.aspects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation @Cachable pour la mise en cache des résultats.
 * Les méthodes annotées auront leur résultat mis en cache.
 * 
 * Utilisé avec l'aspect CachingAspect pour implémenter le cache AOP.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cachable {
    /**
     * Clé de cache optionnelle. Si non spécifiée, une clé sera générée automatiquement.
     */
    String key() default "";
    
    /**
     * Durée de vie du cache en secondes (par défaut: 300 secondes = 5 minutes).
     */
    long ttl() default 300;
}
