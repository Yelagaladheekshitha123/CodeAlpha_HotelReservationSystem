package com.codealpha.hotel.model;

import java.io.Serializable;

/**
 * Represents a simulated payment for a reservation.
 * No real payment gateway is used — this models the calculation
 * and a mock transaction reference, as required by the task.
 */
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum PaymentStatus {
        PENDING, PAID, REFUNDED
    }

    private final double amount;
    private PaymentStatus status;
    private String transactionRef;

    public Payment(double amount) {
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
        this.transactionRef = null;
    }

    /**
     * Simulates processing the payment (e.g., via card/UPI) and generates
     * a mock transaction reference.
     */
    public void processPayment() {
        this.transactionRef = "TXN" + System.currentTimeMillis();
        this.status = PaymentStatus.PAID;
    }

    public void refund() {
        this.status = PaymentStatus.REFUNDED;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    @Override
    public String toString() {
        return String.format("Payment[amount=Rs.%.2f, status=%s, ref=%s]",
                amount, status, transactionRef == null ? "N/A" : transactionRef);
    }
}
