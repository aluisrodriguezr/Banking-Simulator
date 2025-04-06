package dev;

public class AuditorAgent implements Runnable {
    private final String agentId;
    private final BankAccount account1;
    private final BankAccount account2;

    public AuditorAgent(String agentId, BankAccount account1, BankAccount account2) {
        this.agentId = agentId;
        this.account1 = account1;
        this.account2 = account2;
    }

    @Override
    public void run() {
        while (true)
        {
            performAudit();
            sleepRandom();
        }
    }

    private void performAudit() {
        account1.getLock().lock();
        account2.getLock().lock();

        try {
            System.out.println("****************************************************************************************************");
            System.out.println("INTERNAL BANK AUDIT BEGINNING...");
            System.out.println("\tThe total number of transactions since the last Internal Audit: " + BankingSimulator.getNextTransactionId());
            System.out.printf("\tINTERNAL AUDITOR FINDS CURRENT BALANCE FOR %s TO BE: $%.2f\n", account1.getAccountId(), account1.getBalance());
            System.out.printf("\tINTERNAL AUDITOR FINDS CURRENT BALANCE FOR %s TO BE: $%.2f\n", account2.getAccountId(), account2.getBalance());
            System.out.println("INTERNAL BANK AUDIT COMPLETED...");
            System.out.println("****************************************************************************************************");
        } finally {
            account1.getLock().unlock();
            account2.getLock().unlock();
        }
    }

    private void sleepRandom() {
        try {
            Thread.sleep(5000); // internal audit approx. 5 sec
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}



