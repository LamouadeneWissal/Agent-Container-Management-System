package com.ex.gestion_conteneurs_agents.enums;

/**
 * Enumération définissant les types de transactions possibles.
 * Utilisé pour classifier les transactions en Vente ou Achat.
 */
public enum TransactionType {
    VENTE("Vente"),
    ACHAT("Achat");

    private final String label;

    TransactionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
