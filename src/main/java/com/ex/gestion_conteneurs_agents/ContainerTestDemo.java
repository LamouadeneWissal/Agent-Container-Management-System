package com.ex.gestion_conteneurs_agents;

import com.ex.gestion_conteneurs_agents.adapter.HDMIDisplay;
import com.ex.gestion_conteneurs_agents.adapter.HDMIMonitor;
import com.ex.gestion_conteneurs_agents.adapter.VGAMonitor;
import com.ex.gestion_conteneurs_agents.adapter.VGAToHDMIAdapter;
import com.ex.gestion_conteneurs_agents.container.AgentContainer;
import com.ex.gestion_conteneurs_agents.enums.TransactionType;
import com.ex.gestion_conteneurs_agents.model.Agent;
import com.ex.gestion_conteneurs_agents.model.Transaction;
import com.ex.gestion_conteneurs_agents.security.SecurityContext;

import java.util.Optional;

/**
 * Démonstration des tests de la classe AgentContainer.
 * Affiche les résultats détaillés pour le rapport d'examen.
 * 
 * Patterns démontrés:
 * - Singleton: Instance unique du conteneur
 * - Adapter: Connexion d'afficheurs VGA via adaptateur HDMI
 */
public class ContainerTestDemo {

    private static int testsReussis = 0;
    private static int testsEchoues = 0;

    public static void main(String[] args) {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║    TESTS DE LA CLASSE AGENTCONTAINER (PATTERNS SINGLETON ET ADAPTER)         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");

        // ==================== TESTS PATTERN SINGLETON ====================
        System.out.println("═══════════════════════════════════════════════════════════════════════════════");
        System.out.println("                     SECTION 1: PATTERN SINGLETON                              ");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════\n");

        test1_SingletonInstance();
        test2_ResetInstance();
        test3_ThreadSafeSingleton();

        // ==================== TESTS GESTION DES AGENTS ====================
        System.out.println("\n═══════════════════════════════════════════════════════════════════════════════");
        System.out.println("                   SECTION 2: GESTION DES AGENTS                               ");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════\n");

        test4_AjouterAgent();
        test5_AjouterAgentExistant();
        test6_ExceptionAgentNull();
        test7_SupprimerAgent();
        test8_RechercherAgent();
        test9_ViderConteneur();
        test10_GetTousLesAgents();

        // ==================== TESTS PATTERN ADAPTER ====================
        System.out.println("\n═══════════════════════════════════════════════════════════════════════════════");
        System.out.println("                     SECTION 3: PATTERN ADAPTER                                ");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════\n");

        test11_ConnecterAfficheurHDMI();
        test12_ConnecterAfficheurVGAAdapter();
        test13_ExceptionAfficheurNull();
        test14_AfficherEtat();
        test15_AfficherAgentSpecifique();

        // ==================== TESTS SÉCURITÉ ====================
        System.out.println("\n═══════════════════════════════════════════════════════════════════════════════");
        System.out.println("                     SECTION 4: SÉCURITÉ (SecurityContext)                     ");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════\n");

        test16_SecurityContextLogin();
        test17_SecurityContextRoles();

        // ==================== RÉSUMÉ ====================
        printSummary();
    }

    // ==================== TESTS PATTERN SINGLETON ====================

    private static void test1_SingletonInstance() {
        printTestHeader(1, "Instance unique garantie (Singleton)");
        
        System.out.println("   Code exécuté:");
        System.out.println("   AgentContainer container1 = AgentContainer.getInstance();");
        System.out.println("   AgentContainer container2 = AgentContainer.getInstance();");
        System.out.println("   boolean sameInstance = (container1 == container2);");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container1 = AgentContainer.getInstance();
            AgentContainer container2 = AgentContainer.getInstance();

            System.out.println("   Résultat:");
            System.out.println("   → container1 hashCode: " + System.identityHashCode(container1));
            System.out.println("   → container2 hashCode: " + System.identityHashCode(container2));
            System.out.println("   → Même instance: " + (container1 == container2));

            boolean success = container1 == container2;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test2_ResetInstance() {
        printTestHeader(2, "Reset de l'instance Singleton");
        
        System.out.println("   Code exécuté:");
        System.out.println("   AgentContainer container1 = AgentContainer.getInstance();");
        System.out.println("   AgentContainer.resetInstance();  // Détruit l'instance");
        System.out.println("   AgentContainer container2 = AgentContainer.getInstance();");
        System.out.println("   boolean differentInstances = (container1 != container2);");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container1 = AgentContainer.getInstance();
            int hash1 = System.identityHashCode(container1);
            
            AgentContainer.resetInstance();
            AgentContainer container2 = AgentContainer.getInstance();
            int hash2 = System.identityHashCode(container2);

            System.out.println("   Résultat:");
            System.out.println("   → container1 hashCode (avant reset): " + hash1);
            System.out.println("   → container2 hashCode (après reset): " + hash2);
            System.out.println("   → Instances différentes: " + (container1 != container2));

            boolean success = container1 != container2;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test3_ThreadSafeSingleton() {
        printTestHeader(3, "Thread-safety du Singleton (Double-Checked Locking)");
        
        System.out.println("   Code du pattern:");
        System.out.println("   public static AgentContainer getInstance() {");
        System.out.println("       if (instance == null) {                    // 1er check");
        System.out.println("           synchronized (AgentContainer.class) {");
        System.out.println("               if (instance == null) {            // 2ème check");
        System.out.println("                   instance = new AgentContainer();");
        System.out.println("               }");
        System.out.println("           }");
        System.out.println("       }");
        System.out.println("       return instance;");
        System.out.println("   }");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            
            // Simulation d'accès concurrent
            final AgentContainer[] containers = new AgentContainer[2];
            Thread t1 = new Thread(() -> containers[0] = AgentContainer.getInstance());
            Thread t2 = new Thread(() -> containers[1] = AgentContainer.getInstance());
            
            t1.start();
            t2.start();
            t1.join();
            t2.join();

            System.out.println("   Résultat (accès concurrent):");
            System.out.println("   → Thread 1 obtient: " + System.identityHashCode(containers[0]));
            System.out.println("   → Thread 2 obtient: " + System.identityHashCode(containers[1]));
            System.out.println("   → Même instance garantie: " + (containers[0] == containers[1]));

            boolean success = containers[0] == containers[1];
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    // ==================== TESTS GESTION DES AGENTS ====================

    private static void test4_AjouterAgent() {
        printTestHeader(4, "Ajout d'un agent au conteneur");
        
        System.out.println("   Code exécuté:");
        System.out.println("   AgentContainer container = AgentContainer.getInstance();");
        System.out.println("   Agent agent = new Agent(\"Agent-A\");");
        System.out.println("   boolean added = container.ajouterAgent(agent);");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container = AgentContainer.getInstance();
            Agent agent = new Agent("Agent-A");
            
            boolean added = container.ajouterAgent(agent);

            System.out.println("   Résultat:");
            System.out.println("   → Agent ajouté: " + added);
            System.out.println("   → Nombre d'agents: " + container.getNombreAgents());
            System.out.println("   → Contient \"Agent-A\": " + container.contientAgent("Agent-A"));

            boolean success = added && container.getNombreAgents() == 1 && container.contientAgent("Agent-A");
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test5_AjouterAgentExistant() {
        printTestHeader(5, "Ajout d'un agent déjà existant (retourne false)");
        
        System.out.println("   Code exécuté:");
        System.out.println("   container.ajouterAgent(agent);  // 1ère fois → true");
        System.out.println("   container.ajouterAgent(agent);  // 2ème fois → false");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container = AgentContainer.getInstance();
            Agent agent = new Agent("Agent-A");
            
            boolean firstAdd = container.ajouterAgent(agent);
            boolean secondAdd = container.ajouterAgent(agent);

            System.out.println("   Résultat:");
            System.out.println("   → 1er ajout: " + firstAdd);
            System.out.println("   → 2ème ajout (doublon): " + secondAdd);
            System.out.println("   → Nombre d'agents (pas de doublon): " + container.getNombreAgents());

            boolean success = firstAdd && !secondAdd && container.getNombreAgents() == 1;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test6_ExceptionAgentNull() {
        printTestHeader(6, "Exception si agent null");
        
        System.out.println("   Code exécuté:");
        System.out.println("   container.ajouterAgent(null);");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container = AgentContainer.getInstance();
            container.ajouterAgent(null);
            
            System.out.println("   ✗ Aucune exception levée!");
            printTestResult(false);
        } catch (IllegalArgumentException e) {
            System.out.println("   Résultat:");
            System.out.println("   → Exception levée: " + e.getClass().getSimpleName());
            System.out.println("   → Message: \"" + e.getMessage() + "\"");
            printTestResult(true);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test7_SupprimerAgent() {
        printTestHeader(7, "Suppression d'un agent");
        
        System.out.println("   Code exécuté:");
        System.out.println("   container.ajouterAgent(agent);");
        System.out.println("   Optional<Agent> removed = container.supprimerAgent(\"Agent-A\");");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container = AgentContainer.getInstance();
            Agent agent = new Agent("Agent-A");
            
            container.ajouterAgent(agent);
            System.out.println("   Avant suppression: " + container.getNombreAgents() + " agent(s)");
            
            Optional<Agent> removed = container.supprimerAgent("Agent-A");

            System.out.println("   Résultat:");
            System.out.println("   → Agent supprimé: " + removed.isPresent());
            System.out.println("   → Après suppression: " + container.getNombreAgents() + " agent(s)");

            boolean success = removed.isPresent() && container.getNombreAgents() == 0;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test8_RechercherAgent() {
        printTestHeader(8, "Recherche d'un agent par nom");
        
        System.out.println("   Code exécuté:");
        System.out.println("   container.ajouterAgent(new Agent(\"Agent-A\"));");
        System.out.println("   Optional<Agent> found = container.rechercherAgent(\"Agent-A\");");
        System.out.println("   Optional<Agent> notFound = container.rechercherAgent(\"XXX\");");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container = AgentContainer.getInstance();
            Agent agent = new Agent("Agent-A");
            
            container.ajouterAgent(agent);
            
            Optional<Agent> found = container.rechercherAgent("Agent-A");
            Optional<Agent> notFound = container.rechercherAgent("XXX");

            System.out.println("   Résultat:");
            System.out.println("   → Recherche \"Agent-A\": " + (found.isPresent() ? "TROUVÉ ✓" : "NON TROUVÉ"));
            System.out.println("   → Recherche \"XXX\": " + (notFound.isPresent() ? "TROUVÉ" : "NON TROUVÉ (correct) ✓"));

            boolean success = found.isPresent() && !notFound.isPresent();
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test9_ViderConteneur() {
        printTestHeader(9, "Vider le conteneur");
        
        System.out.println("   Code exécuté:");
        System.out.println("   container.ajouterAgent(agent1);");
        System.out.println("   container.ajouterAgent(agent2);");
        System.out.println("   container.vider();");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container = AgentContainer.getInstance();
            
            container.ajouterAgent(new Agent("Agent-A"));
            container.ajouterAgent(new Agent("Agent-B"));
            System.out.println("   Avant vider(): " + container.getNombreAgents() + " agent(s)");
            
            container.vider();

            System.out.println("   Résultat:");
            System.out.println("   → Après vider(): " + container.getNombreAgents() + " agent(s)");

            boolean success = container.getNombreAgents() == 0;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test10_GetTousLesAgents() {
        printTestHeader(10, "Récupération de tous les agents");
        
        System.out.println("   Code exécuté:");
        System.out.println("   container.ajouterAgent(agent1);");
        System.out.println("   container.ajouterAgent(agent2);");
        System.out.println("   Collection<Agent> agents = container.getTousLesAgents();");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container = AgentContainer.getInstance();
            
            Agent agent1 = new Agent("Agent-A");
            Agent agent2 = new Agent("Agent-B");
            
            container.ajouterAgent(agent1);
            container.ajouterAgent(agent2);

            System.out.println("   Résultat:");
            System.out.println("   → Nombre total: " + container.getTousLesAgents().size());
            System.out.println("   → Contient Agent-A: " + container.getTousLesAgents().contains(agent1));
            System.out.println("   → Contient Agent-B: " + container.getTousLesAgents().contains(agent2));

            boolean success = container.getTousLesAgents().size() == 2;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    // ==================== TESTS PATTERN ADAPTER ====================

    private static void test11_ConnecterAfficheurHDMI() {
        printTestHeader(11, "Connexion d'un afficheur HDMI natif");
        
        System.out.println("   Code exécuté:");
        System.out.println("   HDMIDisplay hdmiDisplay = new HDMIMonitor();");
        System.out.println("   container.connecterAfficheur(hdmiDisplay);");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container = AgentContainer.getInstance();
            
            HDMIDisplay hdmiDisplay = new HDMIMonitor();
            container.connecterAfficheur(hdmiDisplay);

            System.out.println("   Résultat:");
            System.out.println("   → Afficheur connecté: " + (container.getDisplay() != null));
            System.out.println("   → Type: " + container.getDisplay().getClass().getSimpleName());
            System.out.println("   → Nom: " + container.getDisplay().getDisplayName());

            boolean success = container.getDisplay() == hdmiDisplay;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test12_ConnecterAfficheurVGAAdapter() {
        printTestHeader(12, "Connexion d'un afficheur VGA via Adapter");
        
        System.out.println("   Diagramme du pattern Adapter:");
        System.out.println("   ┌─────────────┐     ┌─────────────────┐     ┌─────────────┐");
        System.out.println("   │ Container   │────▶│ VGAToHDMIAdapter│────▶│ VGAMonitor  │");
        System.out.println("   │ (HDMIDisplay)│     │ (implements     │     │ (VGADisplay)│");
        System.out.println("   └─────────────┘     │  HDMIDisplay)   │     └─────────────┘");
        System.out.println("                       └─────────────────┘");
        System.out.println();
        System.out.println("   Code exécuté:");
        System.out.println("   VGAMonitor vgaMonitor = new VGAMonitor();");
        System.out.println("   VGAToHDMIAdapter adapter = new VGAToHDMIAdapter(vgaMonitor);");
        System.out.println("   container.connecterAfficheur(adapter);  // VGA → HDMI");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container = AgentContainer.getInstance();
            
            VGAMonitor vgaMonitor = new VGAMonitor();
            VGAToHDMIAdapter adapter = new VGAToHDMIAdapter(vgaMonitor);
            container.connecterAfficheur(adapter);

            System.out.println("   Résultat:");
            System.out.println("   → Afficheur connecté: " + (container.getDisplay() != null));
            System.out.println("   → Type: " + container.getDisplay().getClass().getSimpleName());
            System.out.println("   → Nom: " + container.getDisplay().getDisplayName());
            System.out.println("   → Adaptateur VGA→HDMI fonctionnel ✓");

            boolean success = container.getDisplay() == adapter 
                           && adapter.getDisplayName().contains("VGA");
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test13_ExceptionAfficheurNull() {
        printTestHeader(13, "Exception si afficheur null");
        
        System.out.println("   Code exécuté:");
        System.out.println("   container.connecterAfficheur(null);");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container = AgentContainer.getInstance();
            container.connecterAfficheur(null);
            
            System.out.println("   ✗ Aucune exception levée!");
            printTestResult(false);
        } catch (IllegalArgumentException e) {
            System.out.println("   Résultat:");
            System.out.println("   → Exception levée: " + e.getClass().getSimpleName());
            System.out.println("   → Message: \"" + e.getMessage() + "\"");
            printTestResult(true);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test14_AfficherEtat() {
        printTestHeader(14, "Affichage de l'état du conteneur via HDMI");
        
        System.out.println("   Code exécuté:");
        System.out.println("   container.ajouterAgent(new Agent(\"Agent-Demo\"));");
        System.out.println("   container.afficherEtat();");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container = AgentContainer.getInstance();
            
            container.ajouterAgent(new Agent("Agent-Demo"));
            
            System.out.println("   Sortie de l'affichage:");
            container.afficherEtat();

            System.out.println();
            System.out.println("   Résultat:");
            System.out.println("   → Affichage réussi via HDMI");

            printTestResult(true);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test15_AfficherAgentSpecifique() {
        printTestHeader(15, "Affichage d'un agent spécifique avec ses transactions");
        
        System.out.println("   Code exécuté:");
        System.out.println("   Agent agent = new Agent(\"Agent-Ventes\");");
        System.out.println("   agent.ajouterTransaction(Transaction.builder()");
        System.out.println("       .id(\"TXN-001\").montant(1500).type(VENTE).build());");
        System.out.println("   container.ajouterAgent(agent);");
        System.out.println("   container.afficherAgent(\"Agent-Ventes\");");
        System.out.println();

        try {
            AgentContainer.resetInstance();
            AgentContainer container = AgentContainer.getInstance();
            
            Agent agent = new Agent("Agent-Ventes");
            agent.ajouterTransaction(Transaction.builder()
                    .id("TXN-001")
                    .montant(1500)
                    .type(TransactionType.VENTE)
                    .build());
            agent.ajouterTransaction(Transaction.builder()
                    .id("TXN-002")
                    .montant(750)
                    .type(TransactionType.ACHAT)
                    .build());
            
            container.ajouterAgent(agent);
            
            System.out.println("   Sortie de l'affichage:");
            container.afficherAgent("Agent-Ventes");

            System.out.println();
            System.out.println("   Résultat:");
            System.out.println("   → Affichage détaillé de l'agent avec transactions");

            printTestResult(true);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    // ==================== TESTS SÉCURITÉ ====================

    private static void test16_SecurityContextLogin() {
        printTestHeader(16, "SecurityContext - Authentification");
        
        System.out.println("   Code exécuté:");
        System.out.println("   SecurityContext.logout();");
        System.out.println("   boolean authenticated = SecurityContext.isAuthenticated(); // false");
        System.out.println("   SecurityContext.login(\"admin\", \"admin123\");");
        System.out.println("   authenticated = SecurityContext.isAuthenticated(); // true");
        System.out.println();

        try {
            SecurityContext.logout();
            boolean beforeLogin = SecurityContext.isAuthenticated();
            String userBefore = SecurityContext.getCurrentUsername();
            
            SecurityContext.login("admin", "admin123");
            boolean afterLogin = SecurityContext.isAuthenticated();
            String userAfter = SecurityContext.getCurrentUsername();

            System.out.println("   Résultat:");
            System.out.println("   → Avant login: authenticated=" + beforeLogin + ", user=" + userBefore);
            System.out.println("   → Après login: authenticated=" + afterLogin + ", user=" + userAfter);

            boolean success = !beforeLogin && afterLogin && "admin".equals(userAfter);
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test17_SecurityContextRoles() {
        printTestHeader(17, "SecurityContext - Vérification des rôles");
        
        System.out.println("   Code exécuté:");
        System.out.println("   SecurityContext.login(\"admin\", \"admin123\");");
        System.out.println("   boolean hasAdmin = SecurityContext.hasRole(\"ADMIN\");  // true");
        System.out.println("   SecurityContext.login(\"user\", \"user123\");");
        System.out.println("   hasAdmin = SecurityContext.hasRole(\"ADMIN\");  // false");
        System.out.println("   boolean hasUser = SecurityContext.hasRole(\"USER\");  // true");
        System.out.println();

        try {
            // Test avec admin
            SecurityContext.login("admin", "admin123");
            boolean adminHasAdmin = SecurityContext.hasRole("ADMIN");
            boolean adminHasManager = SecurityContext.hasRole("MANAGER");
            
            // Test avec user
            SecurityContext.login("user", "user123");
            boolean userHasAdmin = SecurityContext.hasRole("ADMIN");
            boolean userHasUser = SecurityContext.hasRole("USER");
            
            // Test avec manager
            SecurityContext.login("manager", "manager123");
            boolean managerHasManager = SecurityContext.hasRole("MANAGER");

            System.out.println("   Résultat:");
            System.out.println("   → admin a rôle ADMIN: " + adminHasAdmin);
            System.out.println("   → admin a rôle MANAGER: " + adminHasManager);
            System.out.println("   → user a rôle ADMIN: " + userHasAdmin + " (correct: false)");
            System.out.println("   → user a rôle USER: " + userHasUser);
            System.out.println("   → manager a rôle MANAGER: " + managerHasManager);

            boolean success = adminHasAdmin && !userHasAdmin && userHasUser && managerHasManager;
            printTestResult(success);
            
            // Cleanup
            SecurityContext.logout();
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    // ==================== UTILITAIRES ====================

    private static void printTestHeader(int num, String title) {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ TEST %d: %-65s │%n", num, title);
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
    }

    private static void printTestResult(boolean success) {
        if (success) {
            System.out.println("\n   ✓ TEST RÉUSSI\n");
            testsReussis++;
        } else {
            System.out.println("\n   ✗ TEST ÉCHOUÉ\n");
            testsEchoues++;
        }
    }

    private static void printTestFailed(Exception e) {
        System.out.println("   ✗ ERREUR INATTENDUE: " + e.getClass().getSimpleName());
        System.out.println("   Message: " + e.getMessage());
        System.out.println("\n   ✗ TEST ÉCHOUÉ\n");
        testsEchoues++;
    }

    private static void printSummary() {
        int total = testsReussis + testsEchoues;
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              RÉSUMÉ DES TESTS                                ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║   ✓ Tests réussis: %-56d ║%n", testsReussis);
        System.out.printf("║   ✗ Tests échoués: %-56d ║%n", testsEchoues);
        System.out.printf("║   ⊕ Total: %-64d ║%n", total);
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        
        if (testsEchoues == 0) {
            System.out.println("\n✓ TOUS LES TESTS SONT PASSÉS AVEC SUCCÈS !\n");
        } else {
            System.out.println("\n✗ CERTAINS TESTS ONT ÉCHOUÉ !\n");
        }
    }
}
