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
    private static final String FILE_NAME = "transactions.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println(" ");
            System.out.println("Welcome to the Transaction Application!");
            System.out.println(" ");
            System.out.println("Please Choose an option:");
            System.out.println(" ");
            System.out.println("Press 'D' to Add Deposit");
            System.out.println("Press 'P' to Add Payment");
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

    public static void loadTransactions(String fileName) {

        String line;

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null) {
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
            System.out.println("Error, File Not Found!");
        }
    }

    private static void addDeposit(Scanner scanner) {

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

        Transaction transaction = new Transaction(parsedDate, parsedTime, userDescriptionInput, userVendorInput, userDepositInput);
        transactions.add(transaction);


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {

            String formattedLine = String.format("%s|%s|%s|%s|%.2f", transaction.getDate().format(DATE_FORMATTER), transaction.getTime().format(TIME_FORMATTER), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            writer.newLine();
            writer.write(formattedLine);


        } catch (Exception e) {
            System.out.println(" ");
            System.out.println("Error Writing This Deposit to File...");
        }

        System.out.println(" ");
        System.out.println("Deposit Successfully Added!");
        System.out.println(" ");
        System.out.println(transaction);
        System.out.println(" ");
    }

    private static void addPayment(Scanner scanner) {

        LocalDate parsedDate = null;
        LocalTime parsedTime = null;

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

        Transaction transaction = new Transaction(parsedDate, parsedTime, userDescriptionInput, userVendorInput, userPaymentInput);
        transactions.add(transaction);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {

            String formattedLine = String.format("%s|%s|%s|%s|%.2f", transaction.getDate().format(DATE_FORMATTER), transaction.getTime().format(TIME_FORMATTER), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            writer.newLine();
            writer.write(formattedLine);

        } catch (Exception e) {

            System.out.println(" ");
            System.out.println("Error Writing This Payment to File...");

        }

        System.out.println(" ");
        System.out.println("Payment Successfully Added!");
        System.out.println(" ");
        System.out.println(transaction.toString());
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

        System.out.println(" ");
        System.out.println("Here is Your Current 'All' Ledger");
        for (Transaction transaction : transactions) {
            System.out.println(" ");

            System.out.println(transaction.toString());
        }
    }

    private static void displayDeposits() {

        System.out.println(" ");
        System.out.println("Here Are Your Deposits:");
        System.out.println(" ");

        boolean foundDeposit = false;

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(" ");
                System.out.println(transaction.toString());

                foundDeposit = true;
            }

        }
        if (!foundDeposit) {
            System.out.println("No Deposit Transactions Found!");
        }
    }

    private static void displayPayments() {

        System.out.println(" ");
        System.out.println("Here Are Your Payments:");
        System.out.println(" ");

        boolean foundPayment = false;

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(" ");
                System.out.println(transaction);

                foundPayment = true;

            }
        }
        if (!foundPayment) {
            System.out.println("No Payment transactions found");
    }
}

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println(" ");
            System.out.println("Reports");
            System.out.println(" ");
            System.out.println("Select the Range or Search Method You Would Like to See Reports For:");
            System.out.println(" ");
            System.out.println("Press '1' for Month To Date");
            System.out.println("Press '2' for Last Month");
            System.out.println("Press '3' for Year To Date");
            System.out.println("Press '4' for Previous Year");
            System.out.println("Press '5' to Search by Vendor");
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
                    System.out.println(" ");
                    System.out.println("Type in the Name of The Vendor: ");
                    String vendorName = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendorName);
                    break;

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

        boolean foundInRange = false;
        System.out.println(" ");
        System.out.println("Transactions Filtered From: (" + startDate + "  to  " + endDate + ")");
        System.out.println(" ");



        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)) {

                System.out.println(transaction);

                foundInRange = true;
            }
        }
        if (!foundInRange) {
            System.out.println("No Transactions Found Within This Date Range");
            System.out.println(" ");
            System.out.println("Returning Back to Reports Page...");
        }
    }

    private static void filterTransactionsByVendor(String vendorName) {

        boolean foundName = false;
        System.out.println("(Transactions from Vendor: " + vendorName + ")");
        System.out.println(" ");

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendorName)) {
                System.out.println(transaction);

                foundName = true;

            }
        }
        if (!foundName) {
            System.out.println("No Transactions Found For This Vendor...");
            System.out.println(" ");
            System.out.println("Returning Back to Reports Page...");
            return;
        }
    }
}