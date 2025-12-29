package com.ex.gestion_conteneurs_agents;

import com.ex.gestion_conteneurs_agents.adapter.HDMIMonitor;
import com.ex.gestion_conteneurs_agents.adapter.VGAToHDMIAdapter;
import com.ex.gestion_conteneurs_agents.container.AgentContainer;
import com.ex.gestion_conteneurs_agents.enums.TransactionType;
import com.ex.gestion_conteneurs_agents.model.Agent;
import com.ex.gestion_conteneurs_agents.model.Transaction;
import com.ex.gestion_conteneurs_agents.security.SecurityContext;
import com.ex.gestion_conteneurs_agents.strategy.HistoryStrategy;
import com.ex.gestion_conteneurs_agents.strategy.ScoringStrategy;
import com.ex.gestion_conteneurs_agents.strategy.StatisticsStrategy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.LocalDateTime;

/**
 * Application principale de démonstration des Design Patterns et AOP.
 * 
 * Design Patterns implémentés:
 * 1. BUILDER - Pour la création des Transactions
 * 2. OBSERVER - Pour la notification entre agents
 * 3. STRATEGY - Pour le traitement des notifications
 * 4. SINGLETON - Pour le conteneur d'agents
 * 5. ADAPTER - Pour la compatibilité HDMI/VGA
 * 
 * Aspects AOP implémentés:
 * 1. @Log - Journalisation avec durée d'exécution
 * 2. @Cachable - Mise en cache des résultats
 * 3. @SecuredBy - Sécurisation par rôles
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class GestionConteneursAgentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionConteneursAgentsApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(ScoringStrategy scoringStrategy, 
                          HistoryStrategy historyStrategy,
                          StatisticsStrategy statisticsStrategy) {
        return args -> {
            System.out.println("\n");
            System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                                                              ║");
            System.out.println("║     🎓 EXAMEN DESIGN PATTERNS ET PROGRAMMATION ORIENTÉE ASPECT              ║");
            System.out.println("║                        Classe: 3 GLSID | Pr. M.YOUSSFI                       ║");
            System.out.println("║                                                                              ║");
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");

            // ============================================================
            // 1. TEST DU PATTERN BUILDER (Transaction)
            // ============================================================
            System.out.println("\n═══════════════════════════════════════════════════════════════");
            System.out.println("        1. TEST DU PATTERN BUILDER (Transaction)");
            System.out.println("═══════════════════════════════════════════════════════════════\n");

            Transaction t1 = Transaction.builder()
                    .id("TXN-001")
                    .date(LocalDateTime.now())
                    .montant(1500.00)
                    .type(TransactionType.VENTE)
                    .build();

            Transaction t2 = Transaction.builder()
                    .id("TXN-002")
                    .date(LocalDateTime.now().minusDays(1))
                    .montant(800.50)
                    .type(TransactionType.ACHAT)
                    .build();

            Transaction t3 = Transaction.builder()
                    .id("TXN-003")
                    .date(LocalDateTime.now())
                    .montant(2500.00)
                    .type(TransactionType.VENTE)
                    .build();

            System.out.println("Transactions créées avec le pattern Builder:");
            t1.afficher();
            t2.afficher();
            t3.afficher();

            // ============================================================
            // 2. TEST DU PATTERN OBSERVER ET STRATEGY (Agent)
            // ============================================================
            System.out.println("\n═══════════════════════════════════════════════════════════════");
            System.out.println("        2. TEST DES PATTERNS OBSERVER ET STRATEGY (Agent)");
            System.out.println("═══════════════════════════════════════════════════════════════\n");

            // Création des agents
            Agent agent1 = new Agent("Agent-Alpha");
            Agent agent2 = new Agent("Agent-Beta");
            Agent agent3 = new Agent("Agent-Gamma");

            // Configuration des stratégies différentes pour chaque agent
            agent2.changerStrategie(scoringStrategy);
            agent3.changerStrategie(historyStrategy);

            // Souscription: Agent2 et Agent3 observent Agent1
            agent1.subscribe(agent2);
            agent1.subscribe(agent3);

            // Ajout d'une transaction à Agent1 (notifie Agent2 et Agent3)
            System.out.println("\n--- Ajout de transaction à Agent-Alpha (notifie les observateurs) ---");
            agent1.ajouterTransaction(t1);

            // Changement de stratégie dynamique
            System.out.println("\n--- Changement de stratégie de Agent-Beta vers StatisticsStrategy ---");
            agent2.changerStrategie(statisticsStrategy);

            // Ajout d'une autre transaction
            agent1.ajouterTransaction(t2);

            // Affichage des agents
            agent1.afficher();

            // ============================================================
            // 3. TEST DU PATTERN SINGLETON ET ADAPTER (Container)
            // ============================================================
            System.out.println("\n═══════════════════════════════════════════════════════════════");
            System.out.println("        3. TEST DES PATTERNS SINGLETON ET ADAPTER (Container)");
            System.out.println("═══════════════════════════════════════════════════════════════\n");

            // Test Singleton: même instance
            AgentContainer container1 = AgentContainer.getInstance();
            AgentContainer container2 = AgentContainer.getInstance();
            System.out.println("Singleton vérifié: container1 == container2 ? " + (container1 == container2));

            // Authentification nécessaire pour les opérations sécurisées
            SecurityContext.displayAvailableUsers();
            SecurityContext.login("admin", "admin123");

            // Ajout des agents au conteneur
            container1.ajouterAgent(agent1);
            container1.ajouterAgent(agent2);
            container1.ajouterAgent(agent3);

            // Affichage avec afficheur HDMI standard
            System.out.println("\n--- Affichage avec HDMI Monitor Standard ---");
            container1.connecterAfficheur(new HDMIMonitor());
            container1.afficherEtat();

            // Affichage avec adaptateur VGA vers HDMI
            System.out.println("\n--- Affichage avec Adaptateur VGA → HDMI ---");
            container1.connecterAfficheur(new VGAToHDMIAdapter());
            container1.afficherEtat();

            // ============================================================
            // 4. TEST DU CACHE (@Cachable)
            // ============================================================
            System.out.println("\n═══════════════════════════════════════════════════════════════");
            System.out.println("        4. TEST DE L'ASPECT CACHE (@Cachable)");
            System.out.println("═══════════════════════════════════════════════════════════════\n");

            // Ajout de transactions pour tester le cache
            agent1.ajouterTransaction(t3);

            System.out.println("\n--- Premier appel (CACHE MISS) ---");
            var maxTransaction1 = agent1.getTransactionMaxMontant();
            maxTransaction1.ifPresent(t -> System.out.println("Transaction max: " + t.getId() + " - " + t.getMontant() + " €"));

            System.out.println("\n--- Deuxième appel (CACHE HIT) ---");
            var maxTransaction2 = agent1.getTransactionMaxMontant();
            maxTransaction2.ifPresent(t -> System.out.println("Transaction max: " + t.getId() + " - " + t.getMontant() + " €"));

            // ============================================================
            // 5. TEST DE LA SÉCURITÉ (@SecuredBy)
            // ============================================================
            System.out.println("\n═══════════════════════════════════════════════════════════════");
            System.out.println("        5. TEST DE L'ASPECT SÉCURITÉ (@SecuredBy)");
            System.out.println("═══════════════════════════════════════════════════════════════\n");

            // Test avec utilisateur ADMIN (autorisé)
            System.out.println("--- Test avec utilisateur ADMIN ---");
            SecurityContext.login("admin", "admin123");
            Agent newAgent = new Agent("Agent-Delta");
            container1.ajouterAgent(newAgent);

            // Déconnexion et test avec utilisateur non autorisé
            SecurityContext.logout();
            
            System.out.println("\n--- Test avec utilisateur USER (non autorisé pour suppression) ---");
            SecurityContext.login("user", "user123");
            try {
                container1.supprimerAgent("Agent-Delta");
            } catch (com.ex.gestion_conteneurs_agents.security.SecurityException e) {
                System.out.println("🚫 Exception attendue: " + e.getMessage());
            }

            // ============================================================
            // 6. AFFICHAGE DE L'HISTORIQUE (Strategy)
            // ============================================================
            System.out.println("\n═══════════════════════════════════════════════════════════════");
            System.out.println("        6. AFFICHAGE DES RÉSULTATS DES STRATÉGIES");
            System.out.println("═══════════════════════════════════════════════════════════════\n");

            historyStrategy.displayHistory();
            statisticsStrategy.displayFullStatistics();
            System.out.println("Solde Scoring Strategy: " + String.format("%.2f", scoringStrategy.getSolde()) + " €");

            // Déconnexion finale
            SecurityContext.logout();

            System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                    🎉 FIN DE LA DÉMONSTRATION                                ║");
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");
        };
    }
}

