package com.ex.gestion_conteneurs_agents.aspects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation @Log pour la journalisation des méthodes.
 * Les méthodes annotées auront leur durée d'exécution logée.
 * 
 * Utilisé avec l'aspect LoggingAspect pour implémenter la journalisation AOP.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
    /**
     * Message optionnel à inclure dans le log.
     */
    String message() default "";
    
    /**
     * Niveau de log (INFO, DEBUG, WARN, ERROR).
     */
    String level() default "INFO";
}
