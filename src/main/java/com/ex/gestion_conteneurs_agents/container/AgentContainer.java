package com.ex.gestion_conteneurs_agents.container;

import com.ex.gestion_conteneurs_agents.adapter.HDMIDisplay;
import com.ex.gestion_conteneurs_agents.adapter.HDMIMonitor;
import com.ex.gestion_conteneurs_agents.aspects.annotations.Log;
import com.ex.gestion_conteneurs_agents.aspects.annotations.SecuredBy;
import com.ex.gestion_conteneurs_agents.model.Agent;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Conteneur d'agents utilisant le Pattern Singleton.
 * 
 * Design Patterns utilisés:
 * - SINGLETON: Une seule instance du conteneur existe dans l'application
 * - ADAPTER: Délègue l'affichage à des afficheurs via l'interface standard HDMI
 * 
 * Le conteneur:
 * - Gère une collection HashMap d'agents (clé=nom, valeur=agent)
 * - Fournit des opérations CRUD sur les agents
 * - Délègue l'affichage à des afficheurs HDMI (ou adaptés)
 */
public class AgentContainer {

    // ==================== SINGLETON ====================
    
    /**
     * Instance unique du conteneur (volatile pour thread-safety).
     */
    private static volatile AgentContainer instance;

    /**
     * Obtient l'instance unique du conteneur (Thread-safe avec double-checked locking).
     * @return l'instance unique du conteneur
     */
    public static AgentContainer getInstance() {
        if (instance == null) {
            synchronized (AgentContainer.class) {
                if (instance == null) {
                    instance = new AgentContainer();
                }
            }
        }
        return instance;
    }

    /**
     * Réinitialise l'instance (utile pour les tests).
     */
    public static void resetInstance() {
        synchronized (AgentContainer.class) {
            instance = null;
        }
    }

    // ==================== ATTRIBUTS ====================

    /**
     * Collection des agents (clé = nom de l'agent).
     */
    private final Map<String, Agent> agents;

    /**
     * Afficheur HDMI connecté au conteneur.
     */
    @Getter
    private HDMIDisplay display;

    // ==================== CONSTRUCTEUR ====================

    /**
     * Constructeur privé (Singleton).
     */
    private AgentContainer() {
        this.agents = new HashMap<>();
        this.display = new HDMIMonitor(); // Afficheur par défaut
        System.out.println("🏗️  Conteneur d'agents initialisé (Singleton)");
    }

    // ==================== GESTION DES AGENTS ====================

    /**
     * Ajoute un agent au conteneur.
     * @param agent l'agent à ajouter
     * @return true si l'agent a été ajouté, false s'il existe déjà
     */
    @Log
    @SecuredBy(roles = {"ADMIN", "MANAGER"})
    public boolean ajouterAgent(Agent agent) {
        if (agent == null) {
            throw new IllegalArgumentException("L'agent ne peut pas être null");
        }
        if (agents.containsKey(agent.getNom())) {
            System.out.println("⚠️  Agent [" + agent.getNom() + "] existe déjà dans le conteneur");
            return false;
        }
        agents.put(agent.getNom(), agent);
        System.out.println("✅ Agent [" + agent.getNom() + "] ajouté au conteneur");
        return true;
    }

    /**
     * Supprime un agent du conteneur par son nom.
     * @param nom le nom de l'agent à supprimer
     * @return l'agent supprimé, ou Optional.empty() si non trouvé
     */
    @Log
    @SecuredBy(roles = {"ADMIN"})
    public Optional<Agent> supprimerAgent(String nom) {
        Agent removed = agents.remove(nom);
        if (removed != null) {
            System.out.println("🗑️  Agent [" + nom + "] supprimé du conteneur");
            return Optional.of(removed);
        }
        System.out.println("⚠️  Agent [" + nom + "] non trouvé dans le conteneur");
        return Optional.empty();
    }

    /**
     * Recherche un agent par son nom.
     * @param nom le nom de l'agent
     * @return Optional contenant l'agent si trouvé
     */
    @Log
    public Optional<Agent> rechercherAgent(String nom) {
        return Optional.ofNullable(agents.get(nom));
    }

    /**
     * Vérifie si un agent existe dans le conteneur.
     * @param nom le nom de l'agent
     * @return true si l'agent existe
     */
    public boolean contientAgent(String nom) {
        return agents.containsKey(nom);
    }

    /**
     * Retourne tous les agents du conteneur.
     * @return collection de tous les agents
     */
    public Collection<Agent> getTousLesAgents() {
        return agents.values();
    }

    /**
     * Retourne le nombre d'agents dans le conteneur.
     * @return le nombre d'agents
     */
    public int getNombreAgents() {
        return agents.size();
    }

    /**
     * Vide le conteneur de tous ses agents.
     */
    @Log
    @SecuredBy(roles = {"ADMIN"})
    public void vider() {
        agents.clear();
        System.out.println("🧹 Conteneur vidé - Tous les agents ont été supprimés");
    }

    // ==================== AFFICHAGE (PATTERN ADAPTER) ====================

    /**
     * Connecte un afficheur HDMI au conteneur.
     * @param display l'afficheur à connecter
     */
    @Log
    public void connecterAfficheur(HDMIDisplay display) {
        if (display == null) {
            throw new IllegalArgumentException("L'afficheur ne peut pas être null");
        }
        this.display = display;
        System.out.println("🔌 Afficheur connecté: " + display.getDisplayName());
    }

    /**
     * Affiche l'état du conteneur via l'afficheur HDMI connecté.
     */
    @Log
    public void afficherEtat() {
        StringBuilder content = new StringBuilder();
        content.append("║           ÉTAT DU CONTENEUR D'AGENTS                         ║\n");
        content.append("╠══════════════════════════════════════════════════════════════╣\n");
        content.append("║ Nombre d'agents: ").append(agents.size()).append("\n");
        content.append("╠══════════════════════════════════════════════════════════════╣\n");
        
        if (agents.isEmpty()) {
            content.append("║ Aucun agent dans le conteneur                                ║\n");
        } else {
            content.append("║ LISTE DES AGENTS:                                            ║\n");
            for (Agent agent : agents.values()) {
                content.append("║ • ").append(agent.getNom())
                       .append(" | Transactions: ").append(agent.getTransactions().size())
                       .append(" | Solde: ").append(String.format("%.2f", agent.calculerSolde())).append(" €\n");
            }
        }
        
        display.displayViaHDMI(content.toString());
    }

    /**
     * Affiche les détails d'un agent spécifique via l'afficheur.
     * @param nom le nom de l'agent à afficher
     */
    @Log
    public void afficherAgent(String nom) {
        Optional<Agent> agentOpt = rechercherAgent(nom);
        if (agentOpt.isPresent()) {
            Agent agent = agentOpt.get();
            StringBuilder content = new StringBuilder();
            content.append("║           DÉTAILS DE L'AGENT: ").append(agent.getNom()).append("\n");
            content.append("╠══════════════════════════════════════════════════════════════╣\n");
            content.append("║ Stratégie: ").append(agent.getStrategy().getStrategyName()).append("\n");
            content.append("║ Nombre de transactions: ").append(agent.getTransactions().size()).append("\n");
            content.append("║ Solde: ").append(String.format("%.2f", agent.calculerSolde())).append(" €\n");
            content.append("╠══════════════════════════════════════════════════════════════╣\n");
            content.append("║ TRANSACTIONS:                                                ║\n");
            
            agent.getTransactions().forEach(t -> 
                content.append("║ • ").append(t.getId())
                       .append(" | ").append(t.getType())
                       .append(" | ").append(String.format("%.2f", t.getMontant())).append(" €\n")
            );
            
            display.displayViaHDMI(content.toString());
        } else {
            display.displayViaHDMI("Agent [" + nom + "] non trouvé dans le conteneur");
        }
    }

    @Override
    public String toString() {
        return "AgentContainer{agents=" + agents.size() + ", display=" + display.getDisplayName() + "}";
    }
}
