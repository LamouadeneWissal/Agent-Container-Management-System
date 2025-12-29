package com.ex.gestion_conteneurs_agents;

import com.ex.gestion_conteneurs_agents.adapter.HDMIDisplay;
import com.ex.gestion_conteneurs_agents.adapter.HDMIMonitor;
import com.ex.gestion_conteneurs_agents.adapter.VGAToHDMIAdapter;
import com.ex.gestion_conteneurs_agents.container.AgentContainer;
import com.ex.gestion_conteneurs_agents.enums.TransactionType;
import com.ex.gestion_conteneurs_agents.model.Agent;
import com.ex.gestion_conteneurs_agents.model.Transaction;
import com.ex.gestion_conteneurs_agents.security.SecurityContext;
import com.ex.gestion_conteneurs_agents.security.SecurityException;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe AgentContainer.
 * Vérifie le bon fonctionnement des patterns Singleton et Adapter.
 */
@DisplayName("Tests de la classe AgentContainer (Patterns Singleton et Adapter)")
class ContainerTest {

    private AgentContainer container;
    private Agent agent1;
    private Agent agent2;

    @BeforeEach
    void setUp() {
        // Reset singleton pour chaque test
        AgentContainer.resetInstance();
        container = AgentContainer.getInstance();
        
        agent1 = new Agent("Agent-A");
        agent2 = new Agent("Agent-B");

        // Login admin pour les tests
        SecurityContext.login("admin", "admin123");
    }

    @AfterEach
    void tearDown() {
        SecurityContext.logout();
        AgentContainer.resetInstance();
    }

    @Nested
    @DisplayName("Tests du pattern Singleton")
    class SingletonTests {

        @Test
        @DisplayName("Instance unique garantie")
        void testSingletonInstance() {
            AgentContainer container1 = AgentContainer.getInstance();
            AgentContainer container2 = AgentContainer.getInstance();
            
            assertSame(container1, container2);
        }

        @Test
        @DisplayName("Reset de l'instance")
        void testResetInstance() {
            AgentContainer container1 = AgentContainer.getInstance();
            AgentContainer.resetInstance();
            AgentContainer container2 = AgentContainer.getInstance();
            
            assertNotSame(container1, container2);
        }
    }

    @Nested
    @DisplayName("Tests de gestion des agents")
    class AgentManagementTests {

        @Test
        @DisplayName("Ajout d'un agent")
        void testAjouterAgent() {
            boolean added = container.ajouterAgent(agent1);
            
            assertTrue(added);
            assertEquals(1, container.getNombreAgents());
            assertTrue(container.contientAgent("Agent-A"));
        }

        @Test
        @DisplayName("Ajout d'un agent existant retourne false")
        void testAjouterAgentExistant() {
            container.ajouterAgent(agent1);
            boolean addedAgain = container.ajouterAgent(agent1);
            
            assertFalse(addedAgain);
            assertEquals(1, container.getNombreAgents());
        }

        @Test
        @DisplayName("Exception si agent null")
        void testAjouterAgentNull() {
            assertThrows(IllegalArgumentException.class, () -> {
                container.ajouterAgent(null);
            });
        }

        @Test
        @DisplayName("Suppression d'un agent")
        void testSupprimerAgent() {
            container.ajouterAgent(agent1);
            Optional<Agent> removed = container.supprimerAgent("Agent-A");
            
            assertTrue(removed.isPresent());
            assertEquals(agent1, removed.get());
            assertEquals(0, container.getNombreAgents());
        }

        @Test
        @DisplayName("Suppression d'un agent inexistant")
        void testSupprimerAgentInexistant() {
            Optional<Agent> removed = container.supprimerAgent("Agent-XXX");
            
            assertFalse(removed.isPresent());
        }

        @Test
        @DisplayName("Recherche d'un agent")
        void testRechercherAgent() {
            container.ajouterAgent(agent1);
            Optional<Agent> found = container.rechercherAgent("Agent-A");
            
            assertTrue(found.isPresent());
            assertEquals(agent1, found.get());
        }

        @Test
        @DisplayName("Recherche d'un agent inexistant")
        void testRechercherAgentInexistant() {
            Optional<Agent> found = container.rechercherAgent("Agent-XXX");
            
            assertFalse(found.isPresent());
        }

        @Test
        @DisplayName("Vider le conteneur")
        void testVider() {
            container.ajouterAgent(agent1);
            container.ajouterAgent(agent2);
            container.vider();
            
            assertEquals(0, container.getNombreAgents());
        }

        @Test
        @DisplayName("Récupération de tous les agents")
        void testGetTousLesAgents() {
            container.ajouterAgent(agent1);
            container.ajouterAgent(agent2);
            
            assertEquals(2, container.getTousLesAgents().size());
            assertTrue(container.getTousLesAgents().contains(agent1));
            assertTrue(container.getTousLesAgents().contains(agent2));
        }
    }

    @Nested
    @DisplayName("Tests du pattern Adapter")
    class AdapterTests {

        @Test
        @DisplayName("Connexion d'un afficheur HDMI")
        void testConnecterAfficheurHDMI() {
            HDMIDisplay hdmiDisplay = new HDMIMonitor();
            container.connecterAfficheur(hdmiDisplay);
            
            assertEquals(hdmiDisplay, container.getDisplay());
        }

        @Test
        @DisplayName("Connexion d'un afficheur VGA via adaptateur")
        void testConnecterAfficheurVGAAdapter() {
            VGAToHDMIAdapter adapter = new VGAToHDMIAdapter();
            container.connecterAfficheur(adapter);
            
            assertEquals(adapter, container.getDisplay());
            assertEquals("VGA Monitor (via HDMI Adapter)", adapter.getDisplayName());
        }

        @Test
        @DisplayName("Exception si afficheur null")
        void testConnecterAfficheurNull() {
            assertThrows(IllegalArgumentException.class, () -> {
                container.connecterAfficheur(null);
            });
        }

        @Test
        @DisplayName("Affichage de l'état via HDMI")
        void testAfficherEtat() {
            container.ajouterAgent(agent1);
            // Pas d'exception = test réussi
            assertDoesNotThrow(() -> container.afficherEtat());
        }

        @Test
        @DisplayName("Affichage d'un agent spécifique")
        void testAfficherAgent() {
            container.ajouterAgent(agent1);
            agent1.ajouterTransaction(Transaction.builder()
                    .id("TXN-001")
                    .montant(1000)
                    .type(TransactionType.VENTE)
                    .build());
            
            assertDoesNotThrow(() -> container.afficherAgent("Agent-A"));
        }
    }

    @Nested
    @DisplayName("Tests de sécurité")
    class SecurityTests {

        @Test
        @DisplayName("Opérations fonctionnent avec authentification ADMIN")
        void testOperationsWithAdmin() {
            SecurityContext.login("admin", "admin123");
            
            // Les opérations passent car l'aspect de sécurité intercepte les beans Spring
            // Dans ce test unitaire sans Spring context complet, le conteneur fonctionne normalement
            assertDoesNotThrow(() -> {
                container.ajouterAgent(agent1);
            });
        }

        @Test
        @DisplayName("Accès autorisé avec rôle ADMIN")
        void testAccessGrantedWithAdmin() {
            SecurityContext.login("admin", "admin123");
            
            assertDoesNotThrow(() -> {
                container.ajouterAgent(agent1);
            });
            
            assertTrue(container.contientAgent("Agent-A"));
        }

        @Test
        @DisplayName("Accès autorisé avec rôle MANAGER pour ajout")
        void testAccessGrantedWithManager() {
            SecurityContext.login("manager", "manager123");
            
            assertDoesNotThrow(() -> {
                container.ajouterAgent(agent1);
            });
            
            assertTrue(container.contientAgent("Agent-A"));
        }

        @Test
        @DisplayName("Vérification que SecurityContext fonctionne")
        void testSecurityContextWorks() {
            // Test sans authentification
            SecurityContext.logout();
            assertFalse(SecurityContext.isAuthenticated());
            assertNull(SecurityContext.getCurrentUsername());
            
            // Test avec authentification
            SecurityContext.login("admin", "admin123");
            assertTrue(SecurityContext.isAuthenticated());
            assertEquals("admin", SecurityContext.getCurrentUsername());
            assertTrue(SecurityContext.hasRole("ADMIN"));
            
            // Test utilisateur USER n'a pas le rôle ADMIN
            SecurityContext.login("user", "user123");
            assertTrue(SecurityContext.isAuthenticated());
            assertEquals("user", SecurityContext.getCurrentUsername());
            assertFalse(SecurityContext.hasRole("ADMIN"));
            assertTrue(SecurityContext.hasRole("USER"));
        }
    }
}
