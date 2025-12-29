package com.ex.gestion_conteneurs_agents;

import com.ex.gestion_conteneurs_agents.enums.TransactionType;
import com.ex.gestion_conteneurs_agents.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Transaction.
 * Vérifie le bon fonctionnement du pattern Builder.
 */
@DisplayName("Tests de la classe Transaction (Pattern Builder)")
class TransactionTest {

    @Test
    @DisplayName("Création d'une transaction avec le Builder - tous les champs")
    void testCreateTransactionWithAllFields() {
        // Given
        String id = "TXN-001";
        LocalDateTime date = LocalDateTime.of(2024, 12, 29, 10, 30);
        double montant = 1500.00;
        TransactionType type = TransactionType.VENTE;

        // When
        Transaction transaction = Transaction.builder()
                .id(id)
                .date(date)
                .montant(montant)
                .type(type)
                .build();

        // Then
        assertNotNull(transaction);
        assertEquals(id, transaction.getId());
        assertEquals(date, transaction.getDate());
        assertEquals(montant, transaction.getMontant());
        assertEquals(type, transaction.getType());
    }

    @Test
    @DisplayName("Création d'une transaction ACHAT")
    void testCreateTransactionAchat() {
        Transaction transaction = Transaction.builder()
                .id("TXN-002")
                .montant(800.50)
                .type(TransactionType.ACHAT)
                .build();

        assertEquals(TransactionType.ACHAT, transaction.getType());
        assertEquals(800.50, transaction.getMontant());
    }

    @Test
    @DisplayName("Date par défaut si non spécifiée")
    void testDefaultDate() {
        Transaction transaction = Transaction.builder()
                .id("TXN-003")
                .montant(500.00)
                .type(TransactionType.VENTE)
                .build();

        assertNotNull(transaction.getDate());
    }

    @Test
    @DisplayName("Exception si ID manquant")
    void testExceptionWhenIdMissing() {
        assertThrows(IllegalStateException.class, () -> {
            Transaction.builder()
                    .montant(500.00)
                    .type(TransactionType.VENTE)
                    .build();
        });
    }

    @Test
    @DisplayName("Exception si ID vide")
    void testExceptionWhenIdEmpty() {
        assertThrows(IllegalStateException.class, () -> {
            Transaction.builder()
                    .id("")
                    .montant(500.00)
                    .type(TransactionType.VENTE)
                    .build();
        });
    }

    @Test
    @DisplayName("Exception si type manquant")
    void testExceptionWhenTypeMissing() {
        assertThrows(IllegalStateException.class, () -> {
            Transaction.builder()
                    .id("TXN-004")
                    .montant(500.00)
                    .build();
        });
    }

    @Test
    @DisplayName("Exception si montant négatif")
    void testExceptionWhenMontantNegative() {
        assertThrows(IllegalStateException.class, () -> {
            Transaction.builder()
                    .id("TXN-005")
                    .montant(-100.00)
                    .type(TransactionType.VENTE)
                    .build();
        });
    }

    @Test
    @DisplayName("Montant zéro accepté")
    void testZeroMontantAccepted() {
        Transaction transaction = Transaction.builder()
                .id("TXN-006")
                .montant(0)
                .type(TransactionType.VENTE)
                .build();

        assertEquals(0, transaction.getMontant());
    }

    @Test
    @DisplayName("Types de transaction - Vente label")
    void testTransactionTypeVenteLabel() {
        assertEquals("Vente", TransactionType.VENTE.getLabel());
    }

    @Test
    @DisplayName("Types de transaction - Achat label")
    void testTransactionTypeAchatLabel() {
        assertEquals("Achat", TransactionType.ACHAT.getLabel());
    }
}
