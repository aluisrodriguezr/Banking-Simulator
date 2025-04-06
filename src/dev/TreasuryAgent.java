package dev;


public class TreasuryAgent implements Runnable {
    private final String agentId;
    private final BankAccount account1;
    private final BankAccount account2;

    public TreasuryAgent(String agentId, BankAccount account1, BankAccount account2) {
        this.agentId = agentId;
        this.account1 = account1;
        this.account2 = account2;
    }

    @Override
    public void run() {
        while (true) {
            double totalBalance = getTotalBalance();

            System.out.println("\n****************************************************************************************************\n");
            System.out.println("UNITED STATES DEPARTMENT OF TREASURY - BANK AUDIT BEGINNING...");
            System.out.println("\tThe total number of transactions since the last Internal Audit: " + BankingSimulator.getNextTransactionId());
            System.out.printf("\tTREASURY DEPT AUDITOR FINDS CURRENT BALANCE FOR %s TO BE: $%.2f\n", account1.getAccountId(), account1.getBalance());
            System.out.printf("\tTREASURY DEPT AUDITOR FINDS CURRENT BALANCE FOR %s TO BE: $%.2f\n", account2.getAccountId(), account2.getBalance());
            System.out.printf("\tTreasury reports the total balance across all accounts is: $%.2f\n", totalBalance);
            System.out.println("UNITED STATES DEPARTMENT OF TREASURY - BANK AUDIT COMPLETED...");
            System.out.println("\n****************************************************************************************************\n");
            sleepRandom();
        }
    }

    private double getTotalBalance() {
        account1.getLock().lock();
        account2.getLock().lock();

        try {
            return account1.getBalance() + account2.getBalance();
        } finally {
            account1.getLock().unlock();
            account2.getLock().unlock();
        }
    }

    private void sleepRandom() {
        try {
            Thread.sleep(10000); // Treasury audit aprox 10 sec
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
