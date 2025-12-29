package com.ex.gestion_conteneurs_agents;

import com.ex.gestion_conteneurs_agents.enums.TransactionType;
import com.ex.gestion_conteneurs_agents.model.Agent;
import com.ex.gestion_conteneurs_agents.model.Transaction;
import com.ex.gestion_conteneurs_agents.observer.NotificationEvent;
import com.ex.gestion_conteneurs_agents.observer.Observer;
import com.ex.gestion_conteneurs_agents.strategy.DefaultStrategy;
import com.ex.gestion_conteneurs_agents.strategy.HistoryStrategy;
import com.ex.gestion_conteneurs_agents.strategy.NotificationStrategy;
import com.ex.gestion_conteneurs_agents.strategy.ScoringStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Agent.
 * Vérifie le bon fonctionnement des patterns Observer et Strategy.
 */
@DisplayName("Tests de la classe Agent (Patterns Observer et Strategy)")
class AgentTest {

    private Agent agent;
    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;

    @BeforeEach
    void setUp() {
        agent = new Agent("Agent-Test");
        
        transaction1 = Transaction.builder()
                .id("TXN-001")
                .date(LocalDateTime.now())
                .montant(1000.00)
                .type(TransactionType.VENTE)
                .build();

        transaction2 = Transaction.builder()
                .id("TXN-002")
                .date(LocalDateTime.now())
                .montant(500.00)
                .type(TransactionType.ACHAT)
                .build();

        transaction3 = Transaction.builder()
                .id("TXN-003")
                .date(LocalDateTime.now())
                .montant(2000.00)
                .type(TransactionType.VENTE)
                .build();
    }

    @Nested
    @DisplayName("Tests de création d'agent")
    class CreationTests {

        @Test
        @DisplayName("Création d'un agent avec nom")
        void testCreateAgentWithName() {
            Agent agent = new Agent("Mon-Agent");
            assertEquals("Mon-Agent", agent.getNom());
            assertTrue(agent.getTransactions().isEmpty());
            assertTrue(agent.getObservers().isEmpty());
        }

        @Test
        @DisplayName("Stratégie par défaut assignée")
        void testDefaultStrategy() {
            Agent agent = new Agent("Test");
            assertNotNull(agent.getStrategy());
            assertTrue(agent.getStrategy() instanceof DefaultStrategy);
        }
    }

    @Nested
    @DisplayName("Tests de gestion des transactions")
    class TransactionTests {

        @Test
        @DisplayName("Ajout d'une transaction")
        void testAjouterTransaction() {
            agent.ajouterTransaction(transaction1);
            assertEquals(1, agent.getTransactions().size());
            assertTrue(agent.getTransactions().contains(transaction1));
        }

        @Test
        @DisplayName("Suppression d'une transaction")
        void testSupprimerTransaction() {
            agent.ajouterTransaction(transaction1);
            boolean removed = agent.supprimerTransaction("TXN-001");
            assertTrue(removed);
            assertTrue(agent.getTransactions().isEmpty());
        }

        @Test
        @DisplayName("Suppression d'une transaction inexistante")
        void testSupprimerTransactionInexistante() {
            boolean removed = agent.supprimerTransaction("TXN-XXX");
            assertFalse(removed);
        }

        @Test
        @DisplayName("Recherche d'une transaction existante")
        void testRechercherTransaction() {
            agent.ajouterTransaction(transaction1);
            Optional<Transaction> found = agent.rechercherTransaction("TXN-001");
            assertTrue(found.isPresent());
            assertEquals(transaction1, found.get());
        }

        @Test
        @DisplayName("Recherche d'une transaction inexistante")
        void testRechercherTransactionInexistante() {
            Optional<Transaction> found = agent.rechercherTransaction("TXN-XXX");
            assertFalse(found.isPresent());
        }

        @Test
        @DisplayName("Transaction avec montant max")
        void testGetTransactionMaxMontant() {
            agent.ajouterTransaction(transaction1);
            agent.ajouterTransaction(transaction2);
            agent.ajouterTransaction(transaction3);

            Optional<Transaction> max = agent.getTransactionMaxMontant();
            assertTrue(max.isPresent());
            assertEquals(transaction3, max.get());
            assertEquals(2000.00, max.get().getMontant());
        }

        @Test
        @DisplayName("Transaction max sur liste vide")
        void testGetTransactionMaxMontantEmpty() {
            Optional<Transaction> max = agent.getTransactionMaxMontant();
            assertFalse(max.isPresent());
        }

        @Test
        @DisplayName("Calcul du solde")
        void testCalculerSolde() {
            agent.ajouterTransaction(transaction1); // +1000
            agent.ajouterTransaction(transaction2); // -500
            agent.ajouterTransaction(transaction3); // +2000

            double solde = agent.calculerSolde();
            assertEquals(2500.00, solde); // 1000 - 500 + 2000 = 2500
        }

        @Test
        @DisplayName("Exception si transaction null")
        void testAjouterTransactionNull() {
            assertThrows(IllegalArgumentException.class, () -> {
                agent.ajouterTransaction(null);
            });
        }
    }

    @Nested
    @DisplayName("Tests du pattern Observer")
    class ObserverTests {

        @Test
        @DisplayName("Souscription d'un observateur")
        void testSubscribe() {
            Agent observer = new Agent("Observer");
            agent.subscribe(observer);
            assertEquals(1, agent.getObservers().size());
        }

        @Test
        @DisplayName("Un agent ne peut pas s'observer lui-même")
        void testCannotSubscribeToSelf() {
            agent.subscribe(agent);
            assertEquals(0, agent.getObservers().size());
        }

        @Test
        @DisplayName("Pas de double souscription")
        void testNoDoubleSubscription() {
            Agent observer = new Agent("Observer");
            agent.subscribe(observer);
            agent.subscribe(observer);
            assertEquals(1, agent.getObservers().size());
        }

        @Test
        @DisplayName("Désinscription d'un observateur")
        void testUnsubscribe() {
            Agent observer = new Agent("Observer");
            agent.subscribe(observer);
            agent.unsubscribe(observer);
            assertEquals(0, agent.getObservers().size());
        }

        @Test
        @DisplayName("Notification des observateurs lors d'ajout de transaction")
        void testNotifyObservers() {
            AtomicBoolean notified = new AtomicBoolean(false);
            AtomicReference<NotificationEvent> receivedEvent = new AtomicReference<>();

            // Utilisation d'un Agent réel comme observateur
            Agent observerAgent = new Agent("Observer-Agent") {
                @Override
                public void update(NotificationEvent event) {
                    notified.set(true);
                    receivedEvent.set(event);
                }
            };

            agent.subscribe(observerAgent);
            agent.ajouterTransaction(transaction1);

            assertTrue(notified.get());
            assertNotNull(receivedEvent.get());
            assertEquals("Agent-Test", receivedEvent.get().getAgentName());
            assertEquals(transaction1, receivedEvent.get().getTransaction());
        }
    }

    @Nested
    @DisplayName("Tests du pattern Strategy")
    class StrategyTests {

        @Test
        @DisplayName("Changement de stratégie")
        void testChangerStrategie() {
            ScoringStrategy newStrategy = new ScoringStrategy();
            agent.changerStrategie(newStrategy);
            assertEquals(newStrategy, agent.getStrategy());
        }

        @Test
        @DisplayName("Exception si stratégie null")
        void testChangerStrategieNull() {
            assertThrows(IllegalArgumentException.class, () -> {
                agent.changerStrategie(null);
            });
        }

        @Test
        @DisplayName("ScoringStrategy calcule le solde")
        void testScoringStrategy() {
            ScoringStrategy strategy = new ScoringStrategy();
            
            NotificationEvent venteEvent = new NotificationEvent("Test", transaction1);
            strategy.handleNotification(venteEvent);
            assertEquals(1000.00, strategy.getSolde());

            NotificationEvent achatEvent = new NotificationEvent("Test", transaction2);
            strategy.handleNotification(achatEvent);
            assertEquals(500.00, strategy.getSolde()); // 1000 - 500
        }

        @Test
        @DisplayName("HistoryStrategy garde l'historique")
        void testHistoryStrategy() {
            HistoryStrategy strategy = new HistoryStrategy();
            
            NotificationEvent event1 = new NotificationEvent("Agent1", transaction1);
            NotificationEvent event2 = new NotificationEvent("Agent2", transaction2);

            strategy.handleNotification(event1);
            strategy.handleNotification(event2);

            assertEquals(2, strategy.getHistorySize());
            assertEquals(2, strategy.getTransactionsHistory().size());
        }
    }
}
