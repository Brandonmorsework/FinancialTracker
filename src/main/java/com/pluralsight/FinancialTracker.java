package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        loadTransactions("transactions.csv");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to the Transaction Application!");
            System.out.println(" ");
            System.out.println("Please Choose an option:");
            System.out.println(" ");
            System.out.println("Press 'D' to Add Deposit");
            System.out.println("Press 'P' to Make Payment (Debit)");
            System.out.println("Press 'L' to go to Ledger");
            System.out.println("Press 'X' to Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case ("X"):
                    System.out.println("Thank You For Using The Transaction Application, Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static ArrayList<Transaction> loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.

        String line;

        try {
            BufferedReader br = new BufferedReader(new FileReader("transactions.csv"));
            while((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactions.add(transaction);

            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.

        System.out.println("(1 / 5) Please Enter the Date in The following Format: (yyyy-MM-dd)");
        String userDateInput = scanner.nextLine();
        LocalDate parsedDate = LocalDate.parse(userDateInput, DATE_FORMATTER);

        System.out.println("(2 / 5) Please Enter the Time in The following Format: (HH:mm:ss)");
        String userTimeInput = scanner.nextLine();
        LocalTime parsedTime = LocalTime.parse(userTimeInput, TIME_FORMATTER);

        System.out.println("(3 / 5) Please Enter the Description of the Deposit: ");
        String userDescriptionInput = scanner.nextLine();

        System.out.println("(4 / 5) Please Enter the Name of The Vendor: ");
        String userVendorInput = scanner.nextLine();

        System.out.println("(5 / 5) Finally, Enter the Amount of The Deposit: ");
        double userDepositInput = scanner.nextDouble();
        scanner.nextLine();

        Transaction deposit = new Transaction(parsedDate, parsedTime, userDescriptionInput, userVendorInput, userDepositInput);
        transactions.add(deposit);

        System.out.println("Deposit Successfully Added!");
        System.out.println(" ");
        System.out.println(parsedDate.format(DATE_FORMATTER) + " | " + parsedTime.format(TIME_FORMATTER) + " | " + userDescriptionInput + " | " + userVendorInput + " | " + userDepositInput );
        System.out.println(" ");
    }

    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number then transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.

        System.out.println("(1 / 5) Please Enter the Date in The following Format: (yyyy-MM-dd)");
        String userDateInput = scanner.nextLine();
        LocalDate parsedDate = LocalDate.parse(userDateInput);

        System.out.println("(2 / 5) Please Enter the Time in The following Format: (HH:mm:ss)");
        String userTimeInput = scanner.nextLine();
        LocalTime parsedTime = LocalTime.parse(userTimeInput);

        System.out.println("(3 / 5) Please Enter the Description of the Deposit: ");
        String userDescriptionInput = scanner.nextLine();

        System.out.println("(4 / 5) Please Enter the Name of The Vendor: ");
        String userVendorInput = scanner.nextLine();

        System.out.println("(5 / 5) Finally, Enter the Amount of The Payment: ");
        double userPaymentInput = scanner.nextDouble();
        scanner.nextLine();
        if (userPaymentInput < 0) {
            System.out.println("Please Input a Positive Number For The Payment Amount");
            return;
        }
        userPaymentInput = userPaymentInput * -1;

        Transaction payment = new Transaction(parsedDate, parsedTime, userDescriptionInput, userVendorInput, userPaymentInput);
        transactions.add(payment);

        System.out.println("Payment Successfully Added!");
        System.out.println(" ");
        System.out.println(userDateInput + " | " + userTimeInput + " | " + userDescriptionInput + " | " + userVendorInput + " | " + userPaymentInput);
        System.out.println(" ");

    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("Press A to show All");
            System.out.println("Press D to show Deposits");
            System.out.println("Press P to show Payments");
            System.out.println("Press R to show Reports");
            System.out.println("Press H to show Home");
            System.out.println(" ");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        for (Transaction transaction : transactions) {
            System.out.println(transactions);
            System.out.println(" ");
        }
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.

    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println(" ");
            System.out.println("Choose an option:");
            System.out.println(" ");
            System.out.println("Press '1' for Month To Date");
            System.out.println("Press '2' for Previous Month");
            System.out.println("Press '3' for Year To Date");
            System.out.println("Press '4' for Previous Year");
            System.out.println("Press '5' for Search for by Vendor");
            System.out.println("Press '0' to go Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }
}

