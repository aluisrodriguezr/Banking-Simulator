package dev;

import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private double balance;
    private final String accountId; 	// JA-1 & JA-2
    private final ReentrantLock lock;

    public BankAccount(String accountId) {
        this.accountId = accountId;
        this.balance = 0;
        this.lock = new ReentrantLock();
    }

    public boolean withdraw(double amount) {
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;       	// insufficient funds
        } finally {
            lock.unlock();
        }
    }

    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public String getAccountId() {
        return accountId;
    }

    // Method for obtaining lock object
    public ReentrantLock getLock() {
        return lock;
    }
}

