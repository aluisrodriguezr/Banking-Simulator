package dev;

import java.util.Random;

public class WithdrawalAgent implements Runnable {
    private final String agentId;
    private final BankAccount account1;
    private final BankAccount account2;
    private final Random random = new Random();

    public WithdrawalAgent(String agentId, BankAccount account1, BankAccount account2) {
        this.agentId = agentId;
        this.account1 = account1;
        this.account2 = account2;
    }

    @Override
    public void run() {
        while (true) {
            BankAccount account = random.nextBoolean() ? account1 : account2;
            double withdrawalAmount = random.nextInt(99) + 1;

            if (account.withdraw(withdrawalAmount))
            {
                int transactionNumber = BankingSimulator.getNextTransactionId();
                System.out.printf("Agent %s withdraws $%.2f from %s. (-) New Balance: $%.2f Transaction #%d\n", agentId, withdrawalAmount, account.getAccountId(), account.getBalance(), transactionNumber);

                if (withdrawalAmount > 90)
                {
                    TransactionLogger.logFlaggedTransaction(agentId, "withdrawal", withdrawalAmount, transactionNumber, account.getAccountId());

                    System.out.printf("* * * Flagged Transaction * * * Agent %s made a withdrawal over $90 USD - See Flagged Transaction Log\n", agentId);
                }
            } else {
                System.out.printf("Agent %s attempts to withdraw $%.2f from %s (****) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!! Balance: $%.2f\n", agentId, withdrawalAmount, account.getAccountId(), account.getBalance());
            }

            sleepRandom();
        }
    }

    private void sleepRandom() {
        try {
            Thread.sleep(random.nextInt(500)); // withdrawal agents sleep less than depositors
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

