package com.ex.gestion_conteneurs_agents;

import com.ex.gestion_conteneurs_agents.enums.TransactionType;
import com.ex.gestion_conteneurs_agents.model.Transaction;

import java.time.LocalDateTime;

/**
 * Classe de démonstration pour tester la classe Transaction.
 * Exécuter avec: mvn exec:java -Dexec.mainClass="com.ex.gestion_conteneurs_agents.TransactionTestDemo"
 * Ou simplement: java TransactionTestDemo
 */
public class TransactionTestDemo {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║           TESTS DE LA CLASSE TRANSACTION (PATTERN BUILDER)                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");

        // Test 1: Création avec tous les champs
        test1_CreationAvecTousLesChamps();
        
        // Test 2: Création transaction VENTE
        test2_CreationTransactionVente();
        
        // Test 3: Création transaction ACHAT
        test3_CreationTransactionAchat();
        
        // Test 4: Date par défaut si non spécifiée
        test4_DateParDefaut();
        
        // Test 5: Montant zéro accepté
        test5_MontantZeroAccepte();
        
        // Test 6: Exception si ID manquant
        test6_ExceptionIdManquant();
        
        // Test 7: Exception si ID vide
        test7_ExceptionIdVide();
        
        // Test 8: Exception si type manquant
        test8_ExceptionTypeManquant();
        
        // Test 9: Exception si montant négatif
        test9_ExceptionMontantNegatif();
        
        // Test 10: Vérification des labels des types
        test10_VerificationLabelsTypes();

        // Résumé
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              RÉSUMÉ DES TESTS                                ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   ✅ Tests réussis: " + testsPassed);
        System.out.println("║   ❌ Tests échoués: " + testsFailed);
        System.out.println("║   📊 Total: " + (testsPassed + testsFailed) + " tests");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");
        
        if (testsFailed == 0) {
            System.out.println("🎉 TOUS LES TESTS SONT PASSÉS AVEC SUCCÈS !\n");
        }
    }

    private static void test1_CreationAvecTousLesChamps() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 1: Création d'une transaction avec tous les champs                    │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        
        try {
            LocalDateTime date = LocalDateTime.of(2024, 12, 29, 10, 30);
            
            Transaction transaction = Transaction.builder()
                    .id("TXN-001")
                    .date(date)
                    .montant(1500.00)
                    .type(TransactionType.VENTE)
                    .build();
            
            System.out.println("   Code exécuté:");
            System.out.println("   Transaction transaction = Transaction.builder()");
            System.out.println("       .id(\"TXN-001\")");
            System.out.println("       .date(LocalDateTime.of(2024, 12, 29, 10, 30))");
            System.out.println("       .montant(1500.00)");
            System.out.println("       .type(TransactionType.VENTE)");
            System.out.println("       .build();");
            System.out.println();
            System.out.println("   Résultat:");
            System.out.println("   → ID: " + transaction.getId());
            System.out.println("   → Date: " + transaction.getDate());
            System.out.println("   → Montant: " + transaction.getMontant() + " €");
            System.out.println("   → Type: " + transaction.getType());
            
            if ("TXN-001".equals(transaction.getId()) && 
                transaction.getMontant() == 1500.00 &&
                transaction.getType() == TransactionType.VENTE) {
                System.out.println("\n   ✅ TEST RÉUSSI\n");
                testsPassed++;
            } else {
                System.out.println("\n   ❌ TEST ÉCHOUÉ\n");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("   ❌ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void test2_CreationTransactionVente() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 2: Création d'une transaction de type VENTE                           │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        
        try {
            Transaction transaction = Transaction.builder()
                    .id("TXN-VENTE-001")
                    .montant(2500.00)
                    .type(TransactionType.VENTE)
                    .build();
            
            System.out.println("   Code: Transaction.builder().id(\"TXN-VENTE-001\").montant(2500.00).type(VENTE).build()");
            System.out.println("   Résultat: Type = " + transaction.getType() + " | Montant = " + transaction.getMontant() + " €");
            
            if (transaction.getType() == TransactionType.VENTE) {
                System.out.println("   ✅ TEST RÉUSSI\n");
                testsPassed++;
            } else {
                System.out.println("   ❌ TEST ÉCHOUÉ\n");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("   ❌ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void test3_CreationTransactionAchat() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 3: Création d'une transaction de type ACHAT                           │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        
        try {
            Transaction transaction = Transaction.builder()
                    .id("TXN-ACHAT-001")
                    .montant(800.50)
                    .type(TransactionType.ACHAT)
                    .build();
            
            System.out.println("   Code: Transaction.builder().id(\"TXN-ACHAT-001\").montant(800.50).type(ACHAT).build()");
            System.out.println("   Résultat: Type = " + transaction.getType() + " | Montant = " + transaction.getMontant() + " €");
            
            if (transaction.getType() == TransactionType.ACHAT) {
                System.out.println("   ✅ TEST RÉUSSI\n");
                testsPassed++;
            } else {
                System.out.println("   ❌ TEST ÉCHOUÉ\n");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("   ❌ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void test4_DateParDefaut() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 4: Date par défaut si non spécifiée                                   │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        
        try {
            Transaction transaction = Transaction.builder()
                    .id("TXN-003")
                    .montant(500.00)
                    .type(TransactionType.VENTE)
                    .build();
            
            System.out.println("   Code: Transaction.builder().id(\"TXN-003\").montant(500).type(VENTE).build()");
            System.out.println("   Note: Pas de .date() spécifié");
            System.out.println("   Résultat: Date = " + transaction.getDate());
            
            if (transaction.getDate() != null) {
                System.out.println("   ✅ TEST RÉUSSI - La date a été générée automatiquement\n");
                testsPassed++;
            } else {
                System.out.println("   ❌ TEST ÉCHOUÉ - La date est null\n");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("   ❌ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void test5_MontantZeroAccepte() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 5: Montant zéro accepté                                               │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        
        try {
            Transaction transaction = Transaction.builder()
                    .id("TXN-ZERO")
                    .montant(0)
                    .type(TransactionType.VENTE)
                    .build();
            
            System.out.println("   Code: Transaction.builder().id(\"TXN-ZERO\").montant(0).type(VENTE).build()");
            System.out.println("   Résultat: Montant = " + transaction.getMontant());
            
            if (transaction.getMontant() == 0) {
                System.out.println("   ✅ TEST RÉUSSI - Montant zéro accepté\n");
                testsPassed++;
            } else {
                System.out.println("   ❌ TEST ÉCHOUÉ\n");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("   ❌ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    private static void test6_ExceptionIdManquant() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 6: Exception si ID manquant                                           │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        
        System.out.println("   Code: Transaction.builder().montant(500).type(VENTE).build()");
        System.out.println("   Note: Pas de .id() spécifié");
        
        try {
            Transaction transaction = Transaction.builder()
                    .montant(500.00)
                    .type(TransactionType.VENTE)
                    .build();
            
            System.out.println("   ❌ TEST ÉCHOUÉ - Aucune exception levée\n");
            testsFailed++;
        } catch (IllegalStateException e) {
            System.out.println("   Exception levée: " + e.getClass().getSimpleName());
            System.out.println("   Message: \"" + e.getMessage() + "\"");
            System.out.println("   ✅ TEST RÉUSSI - L'exception attendue a été levée\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("   ❌ TEST ÉCHOUÉ - Mauvais type d'exception: " + e.getClass().getSimpleName() + "\n");
            testsFailed++;
        }
    }

    private static void test7_ExceptionIdVide() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 7: Exception si ID vide                                               │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        
        System.out.println("   Code: Transaction.builder().id(\"\").montant(500).type(VENTE).build()");
        
        try {
            Transaction transaction = Transaction.builder()
                    .id("")
                    .montant(500.00)
                    .type(TransactionType.VENTE)
                    .build();
            
            System.out.println("   ❌ TEST ÉCHOUÉ - Aucune exception levée\n");
            testsFailed++;
        } catch (IllegalStateException e) {
            System.out.println("   Exception levée: " + e.getClass().getSimpleName());
            System.out.println("   Message: \"" + e.getMessage() + "\"");
            System.out.println("   ✅ TEST RÉUSSI - L'exception attendue a été levée\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("   ❌ TEST ÉCHOUÉ - Mauvais type d'exception\n");
            testsFailed++;
        }
    }

    private static void test8_ExceptionTypeManquant() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 8: Exception si type manquant                                         │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        
        System.out.println("   Code: Transaction.builder().id(\"TXN-004\").montant(500).build()");
        System.out.println("   Note: Pas de .type() spécifié");
        
        try {
            Transaction transaction = Transaction.builder()
                    .id("TXN-004")
                    .montant(500.00)
                    .build();
            
            System.out.println("   ❌ TEST ÉCHOUÉ - Aucune exception levée\n");
            testsFailed++;
        } catch (IllegalStateException e) {
            System.out.println("   Exception levée: " + e.getClass().getSimpleName());
            System.out.println("   Message: \"" + e.getMessage() + "\"");
            System.out.println("   ✅ TEST RÉUSSI - L'exception attendue a été levée\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("   ❌ TEST ÉCHOUÉ - Mauvais type d'exception\n");
            testsFailed++;
        }
    }

    private static void test9_ExceptionMontantNegatif() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 9: Exception si montant négatif                                       │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        
        System.out.println("   Code: Transaction.builder().id(\"TXN-005\").montant(-100).type(VENTE).build()");
        
        try {
            Transaction transaction = Transaction.builder()
                    .id("TXN-005")
                    .montant(-100.00)
                    .type(TransactionType.VENTE)
                    .build();
            
            System.out.println("   ❌ TEST ÉCHOUÉ - Aucune exception levée\n");
            testsFailed++;
        } catch (IllegalStateException e) {
            System.out.println("   Exception levée: " + e.getClass().getSimpleName());
            System.out.println("   Message: \"" + e.getMessage() + "\"");
            System.out.println("   ✅ TEST RÉUSSI - L'exception attendue a été levée\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("   ❌ TEST ÉCHOUÉ - Mauvais type d'exception\n");
            testsFailed++;
        }
    }

    private static void test10_VerificationLabelsTypes() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ TEST 10: Vérification des labels des types de transaction                  │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        
        String venteLabel = TransactionType.VENTE.getLabel();
        String achatLabel = TransactionType.ACHAT.getLabel();
        
        System.out.println("   Code: TransactionType.VENTE.getLabel() → \"" + venteLabel + "\"");
        System.out.println("   Code: TransactionType.ACHAT.getLabel() → \"" + achatLabel + "\"");
        
        if ("Vente".equals(venteLabel) && "Achat".equals(achatLabel)) {
            System.out.println("   ✅ TEST RÉUSSI - Les labels sont corrects\n");
            testsPassed++;
        } else {
            System.out.println("   ❌ TEST ÉCHOUÉ - Labels incorrects\n");
            testsFailed++;
        }
    }
}
