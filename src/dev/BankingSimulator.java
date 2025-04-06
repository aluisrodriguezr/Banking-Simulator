package dev;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class BankingSimulator {
    public static final BankAccount account1 = new BankAccount("JA-1");
    public static final BankAccount account2 = new BankAccount("JA-2");
    private static final AtomicInteger transactionCounter = new AtomicInteger(1);   // safeguard

    public static void main(String[] args) throws IOException {
        PrintStream originalOut = System.out;
        PrintStream fileOut = new PrintStream(new FileOutputStream("output.csv"));

        PrintStream dualOut = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                originalOut.write(b);
                fileOut.write(b);
            }
        });
        System.setOut(dualOut);

        System.out.println("SIMULATION BEGINS...");

        // 19 threads in total
        ExecutorService pool = Executors.newFixedThreadPool(19);

        // 5 deposit agents
        for (int i = 0; i < 5; i++) {
            pool.execute(new DepositorAgent("DT" + i, account1, account2));
        }

        // 10 withdrawals agents
        for (int i = 0; i < 10; i++) {
            pool.execute(new WithdrawalAgent("WT" + i, account1, account2));
        }

        // 2 transfer agents
        for (int i = 0; i < 2; i++) {
            pool.execute(new TransferAgent("TR" + i, account1, account2));
        }

        // 1 internal auditor
        pool.execute(new AuditorAgent("InternalAudit", account1, account2));

        // 1 US treasury auditor
        pool.execute(new TreasuryAgent("TreasuryAudit", account1, account2));

        pool.shutdown();	// close thread pool after all tasks are complete
    }

    // increment transaction number
    public static int getNextTransactionId() {
        return transactionCounter.getAndIncrement();
    }
}



