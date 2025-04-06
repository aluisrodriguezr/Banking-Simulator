package dev;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionLogger {
    private static final String FILE_PATH = "transactions.csv";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static synchronized void logFlaggedTransaction(String agent, String type, double amount, int transactionNumber, String accountId) {
        String timeStamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("Agent %s issued %s of $%.2f into %s at %s. Transaction Number: %d\n", agent, type, amount, accountId, timeStamp, transactionNumber);

        try (FileWriter writer = new FileWriter(FILE_PATH, true))
        {
            writer.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

