package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<>();
    private static ArrayList<Transaction> deposits = new ArrayList<>();
    private static ArrayList<Transaction> payments = new ArrayList<>();

    private static final String FILE_NAME = "transactions.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        loadTransactions("transactions.csv");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println(" ");
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
                    System.out.println(" ");
                    System.out.println("Invalid option, Returning to Home...");
                    System.out.println(" ");
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
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactions.add(transaction);

                if (amount > 0) {
                    deposits.add(transaction);
                } else {
                    payments.add(transaction);
                }
            }

            br.close();

        } catch (Exception e) {
            System.out.println("Error");
        }
        return transactions;
    }

    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.

        LocalDate parsedDate = null;
        LocalTime parsedTime = null;
        double userDepositInput = 0.0;

        while (parsedDate == null) {
            try {
                System.out.println(" ");
                System.out.println("(1 / 5) Please Enter the Date in The following Format: (yyyy-MM-dd)");
                System.out.println(" ");
                String userDateInput = scanner.nextLine();
                parsedDate = LocalDate.parse(userDateInput, DATE_FORMATTER);
            } catch (Exception e) {
                System.out.println(" ");
                System.out.println("Invalid Date Format, Please Try Again.");
                System.out.println(" ");
            }
        }

        while (parsedTime == null) {
            try {
                System.out.println(" ");
                System.out.println("(2 / 5) Please Enter the Time in The following Format: (HH:mm:ss)");
                System.out.println(" ");
                String userTimeInput = scanner.nextLine();
                parsedTime = LocalTime.parse(userTimeInput, TIME_FORMATTER);
            } catch (Exception e) {
                System.out.println(" ");
                System.out.println("Invalid Time Format, Please Try Again.");
                System.out.println(" ");
            }
        }
        System.out.println(" ");
        System.out.println("(3 / 5) Please Enter the Description of the Deposit: ");
        System.out.println(" ");
        String userDescriptionInput = scanner.nextLine();

        System.out.println(" ");
        System.out.println("(4 / 5) Please Enter the Name of The Vendor: ");
        System.out.println(" ");
        String userVendorInput = scanner.nextLine();

        System.out.println(" ");
        System.out.println("(5 / 5) Finally, Enter the Amount of The Deposit: ");
        System.out.println(" ");
        userDepositInput = scanner.nextDouble();
        scanner.nextLine();

        Transaction deposit = new Transaction(parsedDate, parsedTime, userDescriptionInput, userVendorInput, userDepositInput);
        transactions.add(deposit);
        deposits.add(deposit);

       try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            String line = String.format("%-12s | %-10s | %-30s | %-20s | $%10.2f\n", deposit.getDate().format(DATE_FORMATTER), deposit.getTime().format(TIME_FORMATTER), deposit.getDescription(), deposit.getVendor(), deposit.getAmount());
            writer.write(line);
            writer.newLine();
        } catch (Exception e) {
           System.out.println(" ");
           System.out.println("Error Writing This Deposit to File...");
       }

        System.out.println(" ");
        System.out.println("Deposit Successfully Added!");
        System.out.println(" ");
        System.out.printf("%-12s | %-10s | %-30s | %-20s | $%10.2f\n", deposit.getDate().format(DATE_FORMATTER), deposit.getTime().format(TIME_FORMATTER), deposit.getDescription(), deposit.getVendor(), deposit.getAmount());
        System.out.println(" ");
    }

    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number then transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.

        LocalDate parsedDate = null;
        LocalTime parsedTime = null;
        double userDepositInput = 0.0;

        while (parsedDate == null) {
            try {
                System.out.println(" ");
                System.out.println("(1 / 5) Please Enter the Date in The following Format: (yyyy-MM-dd)");
                System.out.println(" ");
                String userDateInput = scanner.nextLine();
                parsedDate = LocalDate.parse(userDateInput, DATE_FORMATTER);
            } catch (Exception e) {
                System.out.println(" ");
                System.out.println("Invalid Date Format, Please Try Again.");
                System.out.println(" ");
            }
        }

        while (parsedTime == null) {
            try {
                System.out.println(" ");
                System.out.println("(2 / 5) Please Enter the Time in The following Format: (HH:mm:ss)");
                System.out.println(" ");
                String userTimeInput = scanner.nextLine();
                parsedTime = LocalTime.parse(userTimeInput, TIME_FORMATTER);
            } catch (Exception e) {
                System.out.println(" ");
                System.out.println("Invalid Time Format, Please Try Again.");
                System.out.println(" ");
            }
        }

        System.out.println(" ");
        System.out.println("(3 / 5) Please Enter the Description of the Deposit: ");
        System.out.println(" ");
        String userDescriptionInput = scanner.nextLine();

        System.out.println(" ");
        System.out.println("(4 / 5) Please Enter the Name of The Vendor: ");
        System.out.println(" ");
        String userVendorInput = scanner.nextLine();

        System.out.println(" ");
        System.out.println("(5 / 5) Finally, Enter the Amount of The Payment: ");
        System.out.println(" ");
        double userPaymentInput = scanner.nextDouble();
        scanner.nextLine();
        if (userPaymentInput < 0) {
            System.out.println(" ");
            System.out.println("Please Input a Positive Number For The Payment Amount");
            System.out.println(" ");
            return;
        }

        userPaymentInput = userPaymentInput * -1;

        Transaction payment = new Transaction(parsedDate, parsedTime, userDescriptionInput, userVendorInput, userPaymentInput);
        transactions.add(payment);
        payments.add(payment);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            String line = String.format("%-12s | %-10s | %-30s | %-20s | $%10.2f\n", payment.getDate().format(DATE_FORMATTER), payment.getTime().format(TIME_FORMATTER), payment.getDescription(), payment.getVendor(), payment.getAmount());
            writer.write(line);
            writer.newLine();
        } catch (Exception e) {
            System.out.println(" ");
            System.out.println("Error Writing This Payment to File...");
        }

        System.out.println(" ");
        System.out.println("Payment Successfully Added!");
        System.out.println(" ");
        System.out.printf("%-12s | %-10s | %-30s | %-20s | $%10.2f\n", payment.getDate().format(DATE_FORMATTER), payment.getTime().format(TIME_FORMATTER), payment.getDescription(), payment.getVendor(), payment.getAmount());
        System.out.println(" ");

    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println(" ");
            System.out.println("Ledger");
            System.out.println(" ");
            System.out.println("Choose an option:");
            System.out.println(" ");
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
                    System.out.println(" ");
                    System.out.println("Returning Home...");
                    running = false;
                    break;
                default:
                    System.out.println(" ");
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println(" ");
        System.out.println("Here is Your Current 'All' Ledger");
        for (Transaction transaction : transactions) {
            System.out.println(" ");

            System.out.printf("%-12s | %-10s | %-30s | %-20s | $%10.2f\n", transaction.getDate().format(DATE_FORMATTER), transaction.getTime().format(TIME_FORMATTER), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
        }
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
            System.out.println(" ");
            System.out.println("Here Are Your Deposits:");
        for (Transaction transaction : deposits) {
            System.out.println(" ");
            System.out.printf("%-12s | %-10s | %-30s | %-20s | $%10.2f\n", transaction.getDate().format(DATE_FORMATTER), transaction.getTime().format(TIME_FORMATTER), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());

        }

    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println(" ");
        System.out.println("Here Are Your Payments:");
        for (Transaction transaction : payments) {
            System.out.println(" ");
            System.out.printf("%-12s | %-10s | %-30s | %-20s | $%10.2f\n", transaction.getDate().format(DATE_FORMATTER), transaction.getTime().format(TIME_FORMATTER), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());

        }
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println(" ");
            System.out.println("Reports");
            System.out.println(" ");
            System.out.println("Select a Time Period You Would Like to See Reports For:");
            System.out.println(" ");
            System.out.println("Press '1' for Month To Date");
            System.out.println("Press '2' for Last Month");
            System.out.println("Press '3' for Year To Date");
            System.out.println("Press '4' for Previous Year");
            System.out.println("Press '5' for Search for by Vendor");
            System.out.println("Press '0' to go Back");
            System.out.println(" ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                LocalDate endDate = LocalDate.now();
                LocalDate startDate = endDate.withDayOfMonth(1);
                    filterTransactionsByDate(startDate, endDate);
                    break;

                case "2":
                    LocalDate presentDay = LocalDate.now();
                    LocalDate startOfLastMonth = presentDay.minusMonths(1).withDayOfMonth(1);
                    LocalDate endOfLastMonth = presentDay.withDayOfMonth(1).minusDays(1);
                    filterTransactionsByDate(startOfLastMonth, endOfLastMonth);
                    break;

                case "3":
                    LocalDate endOfYearToDate = LocalDate.now();
                    LocalDate startOfYearToDate = endOfYearToDate.withDayOfYear(1);
                    filterTransactionsByDate(startOfYearToDate, endOfYearToDate);
                    break;

                case "4":
                    LocalDate presentDayPreviousYear = LocalDate.now();
                    LocalDate startOfLastYear = presentDayPreviousYear.minusYears(1).withDayOfYear(1);
                    LocalDate endOfLastYear = presentDayPreviousYear.minusYears(1).withMonth(12).withDayOfMonth(31);
                    filterTransactionsByDate(startOfLastYear, endOfLastYear);
                    break;

                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                case "0":
                    System.out.println(" ");
                    System.out.println("Returning Home...");
                    System.out.println(" ");
                    running = false;
                    break;
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

        boolean foundInRange = false;
        System.out.println(" ");
        System.out.println("Transactions Filtered From: (" + startDate + "  to  " + endDate + ")");
        System.out.println(" ");



        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if ((transactionDate.isEqual(startDate) || transactionDate.isAfter(startDate)) && (transactionDate.isEqual(endDate) || transactionDate.isBefore(endDate))) {

                System.out.printf("%-12s | %-10s | %-30s | %-20s | $%10.2f\n", transaction.getDate().format(DATE_FORMATTER), transaction.getTime().format(TIME_FORMATTER), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());


                foundInRange = true;
            }
        }
        if (!foundInRange) {
            System.out.println("No Transactions Found Within This Date Range");
            System.out.println(" ");
            System.out.println("Returning Back to Reports Page...");
            return;
        }
    }

    private static void filterTransactionsByVendor(String vendorName) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.

        boolean foundName = false;
        System.out.println("Transactions for Vendor: " + vendorName + ")");

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendorName)) {
                System.out.println();
            }
        }

    }
}