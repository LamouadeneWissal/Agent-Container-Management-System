package com.ex.gestion_conteneurs_agents;

import com.ex.gestion_conteneurs_agents.enums.TransactionType;
import com.ex.gestion_conteneurs_agents.model.Agent;
import com.ex.gestion_conteneurs_agents.model.Transaction;
import com.ex.gestion_conteneurs_agents.observer.NotificationEvent;
import com.ex.gestion_conteneurs_agents.strategy.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Démonstration des tests de la classe Agent.
 * Affiche les résultats détaillés pour le rapport d'examen.
 * 
 * Patterns démontrés:
 * - Observer: Notification des agents lors d'une nouvelle transaction
 * - Strategy: Changement dynamique de la stratégie de traitement
 */
public class AgentTestDemo {

    private static int testsReussis = 0;
    private static int testsEchoues = 0;

    public static void main(String[] args) {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║     TESTS DE LA CLASSE AGENT (PATTERNS OBSERVER ET STRATEGY)                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");

        // ==================== TESTS DE CRÉATION ====================
        System.out.println("═══════════════════════════════════════════════════════════════════════════════");
        System.out.println("                        SECTION 1: TESTS DE CRÉATION                           ");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════\n");

        test1_CreationAgentAvecNom();
        test2_StrategieParDefaut();

        // ==================== TESTS DE TRANSACTIONS ====================
        System.out.println("\n═══════════════════════════════════════════════════════════════════════════════");
        System.out.println("                   SECTION 2: GESTION DES TRANSACTIONS                         ");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════\n");

        test3_AjouterTransaction();
        test4_SupprimerTransaction();
        test5_RechercherTransaction();
        test6_TransactionMontantMax();
        test7_CalculerSolde();
        test8_ExceptionTransactionNull();

        // ==================== TESTS PATTERN OBSERVER ====================
        System.out.println("\n═══════════════════════════════════════════════════════════════════════════════");
        System.out.println("                    SECTION 3: PATTERN OBSERVER                                ");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════\n");

        test9_SouscriptionObservateur();
        test10_PasAutoSouscription();
        test11_PasDoubleSouscription();
        test12_DesinscriptionObservateur();
        test13_NotificationObservateurs();

        // ==================== TESTS PATTERN STRATEGY ====================
        System.out.println("\n═══════════════════════════════════════════════════════════════════════════════");
        System.out.println("                    SECTION 4: PATTERN STRATEGY                                ");
        System.out.println("═══════════════════════════════════════════════════════════════════════════════\n");

        test14_ChangementStrategie();
        test15_ExceptionStrategieNull();
        test16_ScoringStrategy();
        test17_HistoryStrategy();
        test18_LoggingStrategy();
        test19_StatisticsStrategy();

        // ==================== RÉSUMÉ ====================
        printSummary();
    }

    // ==================== TESTS DE CRÉATION ====================

    private static void test1_CreationAgentAvecNom() {
        printTestHeader(1, "Création d'un agent avec nom");
        
        System.out.println("   Code exécuté:");
        System.out.println("   Agent agent = new Agent(\"Mon-Agent\");");
        System.out.println();

        try {
            Agent agent = new Agent("Mon-Agent");

            System.out.println("   Résultat:");
            System.out.println("   → Nom: " + agent.getNom());
            System.out.println("   → Transactions: " + agent.getTransactions().size() + " (liste vide)");
            System.out.println("   → Observers: " + agent.getObservers().size() + " (liste vide)");

            boolean success = "Mon-Agent".equals(agent.getNom()) 
                            && agent.getTransactions().isEmpty()
                            && agent.getObservers().isEmpty();
            
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test2_StrategieParDefaut() {
        printTestHeader(2, "Stratégie par défaut assignée (DefaultStrategy)");
        
        System.out.println("   Code exécuté:");
        System.out.println("   Agent agent = new Agent(\"Test\");");
        System.out.println("   NotificationStrategy strategy = agent.getStrategy();");
        System.out.println();

        try {
            Agent agent = new Agent("Test");
            NotificationStrategy strategy = agent.getStrategy();

            System.out.println("   Résultat:");
            System.out.println("   → Strategy: " + strategy.getClass().getSimpleName());
            System.out.println("   → Est DefaultStrategy: " + (strategy instanceof DefaultStrategy));

            boolean success = strategy != null && strategy instanceof DefaultStrategy;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    // ==================== TESTS DE TRANSACTIONS ====================

    private static void test3_AjouterTransaction() {
        printTestHeader(3, "Ajout d'une transaction");
        
        System.out.println("   Code exécuté:");
        System.out.println("   Agent agent = new Agent(\"Agent-Test\");");
        System.out.println("   Transaction tx = Transaction.builder()");
        System.out.println("       .id(\"TXN-001\").montant(1000).type(VENTE).build();");
        System.out.println("   agent.ajouterTransaction(tx);");
        System.out.println();

        try {
            Agent agent = new Agent("Agent-Test");
            Transaction tx = Transaction.builder()
                    .id("TXN-001")
                    .montant(1000.00)
                    .type(TransactionType.VENTE)
                    .build();
            
            agent.ajouterTransaction(tx);

            System.out.println("   Résultat:");
            System.out.println("   → Nombre de transactions: " + agent.getTransactions().size());
            System.out.println("   → Transaction présente: " + agent.getTransactions().contains(tx));

            boolean success = agent.getTransactions().size() == 1 
                            && agent.getTransactions().contains(tx);
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test4_SupprimerTransaction() {
        printTestHeader(4, "Suppression d'une transaction");
        
        System.out.println("   Code exécuté:");
        System.out.println("   agent.ajouterTransaction(tx);  // TXN-001");
        System.out.println("   boolean removed = agent.supprimerTransaction(\"TXN-001\");");
        System.out.println();

        try {
            Agent agent = new Agent("Agent-Test");
            Transaction tx = Transaction.builder()
                    .id("TXN-001")
                    .montant(1000.00)
                    .type(TransactionType.VENTE)
                    .build();
            
            agent.ajouterTransaction(tx);
            System.out.println("   Avant suppression: " + agent.getTransactions().size() + " transaction(s)");
            
            boolean removed = agent.supprimerTransaction("TXN-001");
            
            System.out.println("   Résultat:");
            System.out.println("   → Suppression réussie: " + removed);
            System.out.println("   → Après suppression: " + agent.getTransactions().size() + " transaction(s)");

            boolean success = removed && agent.getTransactions().isEmpty();
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test5_RechercherTransaction() {
        printTestHeader(5, "Recherche d'une transaction par ID");
        
        System.out.println("   Code exécuté:");
        System.out.println("   agent.ajouterTransaction(tx);  // TXN-001");
        System.out.println("   Optional<Transaction> found = agent.rechercherTransaction(\"TXN-001\");");
        System.out.println("   Optional<Transaction> notFound = agent.rechercherTransaction(\"XXX\");");
        System.out.println();

        try {
            Agent agent = new Agent("Agent-Test");
            Transaction tx = Transaction.builder()
                    .id("TXN-001")
                    .montant(1000.00)
                    .type(TransactionType.VENTE)
                    .build();
            
            agent.ajouterTransaction(tx);
            
            Optional<Transaction> found = agent.rechercherTransaction("TXN-001");
            Optional<Transaction> notFound = agent.rechercherTransaction("XXX");

            System.out.println("   Résultat:");
            System.out.println("   → Recherche \"TXN-001\": " + (found.isPresent() ? "TROUVÉ ✓" : "NON TROUVÉ"));
            System.out.println("   → Recherche \"XXX\": " + (notFound.isPresent() ? "TROUVÉ" : "NON TROUVÉ (correct) ✓"));

            boolean success = found.isPresent() && !notFound.isPresent();
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test6_TransactionMontantMax() {
        printTestHeader(6, "Obtenir la transaction avec le montant maximum");
        
        System.out.println("   Code exécuté:");
        System.out.println("   agent.ajouterTransaction(tx1);  // 1000 €");
        System.out.println("   agent.ajouterTransaction(tx2);  // 500 €");
        System.out.println("   agent.ajouterTransaction(tx3);  // 2000 €");
        System.out.println("   Optional<Transaction> max = agent.getTransactionMaxMontant();");
        System.out.println();

        try {
            Agent agent = new Agent("Agent-Test");
            
            Transaction tx1 = Transaction.builder().id("TXN-001").montant(1000).type(TransactionType.VENTE).build();
            Transaction tx2 = Transaction.builder().id("TXN-002").montant(500).type(TransactionType.ACHAT).build();
            Transaction tx3 = Transaction.builder().id("TXN-003").montant(2000).type(TransactionType.VENTE).build();
            
            agent.ajouterTransaction(tx1);
            agent.ajouterTransaction(tx2);
            agent.ajouterTransaction(tx3);
            
            Optional<Transaction> max = agent.getTransactionMaxMontant();

            System.out.println("   Résultat:");
            System.out.println("   → Transaction max trouvée: " + max.isPresent());
            if (max.isPresent()) {
                System.out.println("   → ID: " + max.get().getId());
                System.out.println("   → Montant: " + max.get().getMontant() + " € (le plus élevé)");
            }

            boolean success = max.isPresent() && max.get().getMontant() == 2000.00;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test7_CalculerSolde() {
        printTestHeader(7, "Calcul du solde (VENTE + / ACHAT -)");
        
        System.out.println("   Code exécuté:");
        System.out.println("   agent.ajouterTransaction(vente1);  // +1000 € (VENTE)");
        System.out.println("   agent.ajouterTransaction(achat);   // -500 €  (ACHAT)");
        System.out.println("   agent.ajouterTransaction(vente2);  // +2000 € (VENTE)");
        System.out.println("   double solde = agent.calculerSolde();");
        System.out.println();

        try {
            Agent agent = new Agent("Agent-Test");
            
            Transaction vente1 = Transaction.builder().id("TXN-001").montant(1000).type(TransactionType.VENTE).build();
            Transaction achat = Transaction.builder().id("TXN-002").montant(500).type(TransactionType.ACHAT).build();
            Transaction vente2 = Transaction.builder().id("TXN-003").montant(2000).type(TransactionType.VENTE).build();
            
            agent.ajouterTransaction(vente1);
            agent.ajouterTransaction(achat);
            agent.ajouterTransaction(vente2);
            
            double solde = agent.calculerSolde();

            System.out.println("   Résultat:");
            System.out.println("   → Calcul: 1000 - 500 + 2000 = 2500");
            System.out.println("   → Solde calculé: " + solde + " €");

            boolean success = solde == 2500.00;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test8_ExceptionTransactionNull() {
        printTestHeader(8, "Exception si transaction null");
        
        System.out.println("   Code exécuté:");
        System.out.println("   agent.ajouterTransaction(null);");
        System.out.println();

        try {
            Agent agent = new Agent("Agent-Test");
            agent.ajouterTransaction(null);
            
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

    // ==================== TESTS PATTERN OBSERVER ====================

    private static void test9_SouscriptionObservateur() {
        printTestHeader(9, "Souscription d'un observateur (subscribe)");
        
        System.out.println("   Code exécuté:");
        System.out.println("   Agent agent = new Agent(\"Subject\");");
        System.out.println("   Agent observer = new Agent(\"Observer\");");
        System.out.println("   agent.subscribe(observer);");
        System.out.println();

        try {
            Agent agent = new Agent("Subject");
            Agent observer = new Agent("Observer");
            
            agent.subscribe(observer);

            System.out.println("   Résultat:");
            System.out.println("   → Nombre d'observateurs: " + agent.getObservers().size());
            System.out.println("   → Observer inscrit: " + agent.getObservers().contains(observer));

            boolean success = agent.getObservers().size() == 1;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test10_PasAutoSouscription() {
        printTestHeader(10, "Un agent ne peut pas s'observer lui-même");
        
        System.out.println("   Code exécuté:");
        System.out.println("   Agent agent = new Agent(\"Self\");");
        System.out.println("   agent.subscribe(agent);  // Tentative d'auto-souscription");
        System.out.println();

        try {
            Agent agent = new Agent("Self");
            agent.subscribe(agent);  // Tentative d'auto-souscription

            System.out.println("   Résultat:");
            System.out.println("   → Nombre d'observateurs: " + agent.getObservers().size());
            System.out.println("   → Auto-souscription refusée: " + (agent.getObservers().size() == 0 ? "OUI ✓" : "NON"));

            boolean success = agent.getObservers().size() == 0;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test11_PasDoubleSouscription() {
        printTestHeader(11, "Pas de double souscription");
        
        System.out.println("   Code exécuté:");
        System.out.println("   Agent observer = new Agent(\"Observer\");");
        System.out.println("   agent.subscribe(observer);  // 1ère fois");
        System.out.println("   agent.subscribe(observer);  // 2ème fois (ignorée)");
        System.out.println();

        try {
            Agent agent = new Agent("Subject");
            Agent observer = new Agent("Observer");
            
            agent.subscribe(observer);
            agent.subscribe(observer);  // 2ème fois

            System.out.println("   Résultat:");
            System.out.println("   → Nombre d'observateurs: " + agent.getObservers().size() + " (pas de doublon)");

            boolean success = agent.getObservers().size() == 1;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test12_DesinscriptionObservateur() {
        printTestHeader(12, "Désinscription d'un observateur (unsubscribe)");
        
        System.out.println("   Code exécuté:");
        System.out.println("   agent.subscribe(observer);");
        System.out.println("   agent.unsubscribe(observer);");
        System.out.println();

        try {
            Agent agent = new Agent("Subject");
            Agent observer = new Agent("Observer");
            
            agent.subscribe(observer);
            System.out.println("   Avant unsubscribe: " + agent.getObservers().size() + " observateur(s)");
            
            agent.unsubscribe(observer);

            System.out.println("   Résultat:");
            System.out.println("   → Après unsubscribe: " + agent.getObservers().size() + " observateur(s)");

            boolean success = agent.getObservers().size() == 0;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test13_NotificationObservateurs() {
        printTestHeader(13, "Notification automatique lors d'ajout de transaction");
        
        System.out.println("   Code exécuté:");
        System.out.println("   Agent subject = new Agent(\"Subject\");");
        System.out.println("   Agent observer = new Agent(\"Observer\") {");
        System.out.println("       @Override public void update(NotificationEvent e) {");
        System.out.println("           // Reçoit la notification");
        System.out.println("       }");
        System.out.println("   };");
        System.out.println("   subject.subscribe(observer);");
        System.out.println("   subject.ajouterTransaction(tx);  // Déclenche notification");
        System.out.println();

        try {
            Agent subject = new Agent("Subject");
            AtomicBoolean notified = new AtomicBoolean(false);
            AtomicReference<NotificationEvent> receivedEvent = new AtomicReference<>();

            Agent observer = new Agent("Observer") {
                @Override
                public void update(NotificationEvent event) {
                    notified.set(true);
                    receivedEvent.set(event);
                    System.out.println("   [NOTIFICATION REÇUE]");
                    System.out.println("   → Agent source: " + event.getAgentName());
                    System.out.println("   → Transaction: " + event.getTransaction().getId());
                    System.out.println("   → Montant: " + event.getTransaction().getMontant() + " €");
                }
            };

            subject.subscribe(observer);
            
            Transaction tx = Transaction.builder()
                    .id("TXN-NOTIFY")
                    .montant(750.00)
                    .type(TransactionType.VENTE)
                    .build();
            
            System.out.println("   Exécution de ajouterTransaction()...\n");
            subject.ajouterTransaction(tx);

            System.out.println();
            System.out.println("   Résultat:");
            System.out.println("   → Observer notifié: " + notified.get());
            System.out.println("   → Event reçu: " + (receivedEvent.get() != null));

            boolean success = notified.get() && receivedEvent.get() != null;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    // ==================== TESTS PATTERN STRATEGY ====================

    private static void test14_ChangementStrategie() {
        printTestHeader(14, "Changement dynamique de stratégie");
        
        System.out.println("   Code exécuté:");
        System.out.println("   Agent agent = new Agent(\"Test\");  // DefaultStrategy par défaut");
        System.out.println("   agent.changerStrategie(new ScoringStrategy());");
        System.out.println();

        try {
            Agent agent = new Agent("Test");
            System.out.println("   → Stratégie initiale: " + agent.getStrategy().getClass().getSimpleName());
            
            ScoringStrategy newStrategy = new ScoringStrategy();
            agent.changerStrategie(newStrategy);

            System.out.println("   Résultat:");
            System.out.println("   → Nouvelle stratégie: " + agent.getStrategy().getClass().getSimpleName());

            boolean success = agent.getStrategy() == newStrategy;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test15_ExceptionStrategieNull() {
        printTestHeader(15, "Exception si stratégie null");
        
        System.out.println("   Code exécuté:");
        System.out.println("   agent.changerStrategie(null);");
        System.out.println();

        try {
            Agent agent = new Agent("Test");
            agent.changerStrategie(null);
            
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

    private static void test16_ScoringStrategy() {
        printTestHeader(16, "ScoringStrategy - Calcul du score/solde");
        
        System.out.println("   Code exécuté:");
        System.out.println("   ScoringStrategy strategy = new ScoringStrategy();");
        System.out.println("   strategy.handleNotification(venteEvent);   // +1000");
        System.out.println("   strategy.handleNotification(achatEvent);   // -500");
        System.out.println();

        try {
            ScoringStrategy strategy = new ScoringStrategy();
            
            Transaction vente = Transaction.builder().id("V1").montant(1000).type(TransactionType.VENTE).build();
            Transaction achat = Transaction.builder().id("A1").montant(500).type(TransactionType.ACHAT).build();
            
            NotificationEvent venteEvent = new NotificationEvent("Agent", vente);
            NotificationEvent achatEvent = new NotificationEvent("Agent", achat);
            
            strategy.handleNotification(venteEvent);
            System.out.println("   → Après VENTE +1000: solde = " + strategy.getSolde());
            
            strategy.handleNotification(achatEvent);
            System.out.println("   → Après ACHAT -500: solde = " + strategy.getSolde());

            System.out.println();
            System.out.println("   Résultat:");
            System.out.println("   → Solde final: " + strategy.getSolde() + " € (attendu: 500)");

            boolean success = strategy.getSolde() == 500.00;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test17_HistoryStrategy() {
        printTestHeader(17, "HistoryStrategy - Conservation de l'historique");
        
        System.out.println("   Code exécuté:");
        System.out.println("   HistoryStrategy strategy = new HistoryStrategy();");
        System.out.println("   strategy.handleNotification(event1);");
        System.out.println("   strategy.handleNotification(event2);");
        System.out.println("   int size = strategy.getHistorySize();");
        System.out.println();

        try {
            HistoryStrategy strategy = new HistoryStrategy();
            
            Transaction tx1 = Transaction.builder().id("H1").montant(100).type(TransactionType.VENTE).build();
            Transaction tx2 = Transaction.builder().id("H2").montant(200).type(TransactionType.ACHAT).build();
            
            NotificationEvent event1 = new NotificationEvent("Agent1", tx1);
            NotificationEvent event2 = new NotificationEvent("Agent2", tx2);
            
            strategy.handleNotification(event1);
            strategy.handleNotification(event2);

            System.out.println("   Résultat:");
            System.out.println("   → Taille historique: " + strategy.getHistorySize());
            System.out.println("   → Transactions stockées: " + strategy.getTransactionsHistory().size());

            boolean success = strategy.getHistorySize() == 2;
            printTestResult(success);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test18_LoggingStrategy() {
        printTestHeader(18, "LoggingStrategy - Journalisation des événements");
        
        System.out.println("   Code exécuté:");
        System.out.println("   LoggingStrategy strategy = new LoggingStrategy();");
        System.out.println("   strategy.handleNotification(event);");
        System.out.println();

        try {
            LoggingStrategy strategy = new LoggingStrategy();
            
            Transaction tx = Transaction.builder().id("LOG-001").montant(999).type(TransactionType.VENTE).build();
            NotificationEvent event = new NotificationEvent("LogAgent", tx);
            
            System.out.println("   Sortie du logging:");
            strategy.handleNotification(event);

            System.out.println();
            System.out.println("   Résultat:");
            System.out.println("   → Log généré avec succès");
            System.out.println("   → Fichier: notifications.log");

            printTestResult(true);
        } catch (Exception e) {
            printTestFailed(e);
        }
    }

    private static void test19_StatisticsStrategy() {
        printTestHeader(19, "StatisticsStrategy - Calcul des statistiques");
        
        System.out.println("   Code exécuté:");
        System.out.println("   StatisticsStrategy strategy = new StatisticsStrategy();");
        System.out.println("   strategy.handleNotification(vente1);  // 100 € VENTE");
        System.out.println("   strategy.handleNotification(vente2);  // 200 € VENTE");
        System.out.println("   strategy.handleNotification(achat);   // 300 € ACHAT");
        System.out.println();

        try {
            StatisticsStrategy strategy = new StatisticsStrategy();
            
            Transaction tx1 = Transaction.builder().id("S1").montant(100).type(TransactionType.VENTE).build();
            Transaction tx2 = Transaction.builder().id("S2").montant(200).type(TransactionType.VENTE).build();
            Transaction tx3 = Transaction.builder().id("S3").montant(300).type(TransactionType.ACHAT).build();
            
            System.out.println("   Exécution des notifications:");
            System.out.println();
            strategy.handleNotification(new NotificationEvent("Agent", tx1));
            System.out.println();
            strategy.handleNotification(new NotificationEvent("Agent", tx2));
            System.out.println();
            strategy.handleNotification(new NotificationEvent("Agent", tx3));

            System.out.println();
            System.out.println("   Résultat:");
            System.out.println("   → Statistiques calculées automatiquement à chaque notification");

            printTestResult(true);
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
