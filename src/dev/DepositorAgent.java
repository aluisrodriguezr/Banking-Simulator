package dev;

import java.util.Random;

public class DepositorAgent implements Runnable {
    private final String agentId;
    private final BankAccount account1;
    private final BankAccount account2;
    private final Random random = new Random(); 	// for amount to deposit

    public DepositorAgent(String agentId, BankAccount account1, BankAccount account2) {
        this.agentId = agentId;
        this.account1 = account1;
        this.account2 = account2;
    }

    @Override
    public void run() {
        while (true) { // infinite loop
            BankAccount account = random.nextBoolean() ? account1 : account2; // deposit to account 1 or 2 at random

            double depositAmount = random.nextInt(600) + 1;

            account.deposit(depositAmount);

            int transactionNumber = BankingSimulator.getNextTransactionId();

            System.out.printf("Agent %s deposits $%.2f into %s. (+) New Balance: $%.2f Transaction #%d\n", agentId, depositAmount, account.getAccountId(), account.getBalance(), transactionNumber);

            if (depositAmount > 450)
            {
                TransactionLogger.logFlaggedTransaction(agentId, "deposit", depositAmount, transactionNumber, account.getAccountId());
                System.out.printf("* * * Flagged Transaction * * * Agent %s made a deposit over $450 USD - See Flagged Transaction Log\n", agentId);
            }

            sleepRandom();
        }
    }

    private void sleepRandom() {
        try {
            Thread.sleep(random.nextInt(1000)); 	// random sleep
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();        	// handling thread interrupts
        }
    }
}

