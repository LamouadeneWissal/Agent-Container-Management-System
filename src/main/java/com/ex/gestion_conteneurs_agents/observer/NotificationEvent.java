package com.ex.gestion_conteneurs_agents.observer;

import com.ex.gestion_conteneurs_agents.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Classe représentant un événement de notification.
 * Contient le nom de l'agent source et la transaction ajoutée.
 * 
 * Utilisé dans le pattern Observer pour transmettre les informations
 * lors d'une notification entre agents.
 */
@Getter
@AllArgsConstructor
@ToString
public class NotificationEvent {
    private final String agentName;
    private final Transaction transaction;
}
