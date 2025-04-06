package dev;


import java.util.Random;

public class TransferAgent implements Runnable {
    private final String agentId;
    private final BankAccount account1;
    private final BankAccount account2;
    private final Random random = new Random();

    public TransferAgent(String agentId, BankAccount account1, BankAccount account2) {
        this.agentId = agentId;
        this.account1 = account1;
        this.account2 = account2;
    }

    @Override
    public void run() {
        while (true) {
            double transferAmount = random.nextInt(50) + 1;
            BankAccount fromAccount = random.nextBoolean() ? account1 : account2;
            BankAccount toAccount = (fromAccount == account1) ? account2 : account1;

            fromAccount.getLock().lock();
            toAccount.getLock().lock();

            try {
                if (fromAccount.getBalance() >= transferAmount)
                {
                    fromAccount.withdraw(transferAmount);
                    toAccount.deposit(transferAmount);

                    int transactionNumber = BankingSimulator.getNextTransactionId();

                    System.out.printf("TRANSFER --> Agent %s transferring $%.2f from %s to %s -- %s Balance: $%.2f, %s Balance: $%.2f Transaction #%d\n", agentId, transferAmount, fromAccount.getAccountId(), toAccount.getAccountId(), fromAccount.getAccountId(), fromAccount.getBalance(), toAccount.getAccountId(), toAccount.getBalance(), transactionNumber);
                } else {
                    System.out.printf("TRANSFER BLOCKED --> Agent %s cannot transfer $%.2f due to insufficient funds in %s\n", agentId, transferAmount, fromAccount.getAccountId());
                }
            } finally {
                fromAccount.getLock().unlock();
                toAccount.getLock().unlock();
            }

            sleepRandom();
        }
    }

    private void sleepRandom() {
        try {
            Thread.sleep(random.nextInt(2000)); // random interval between transfers
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

