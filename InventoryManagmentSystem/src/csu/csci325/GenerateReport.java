package csu.csci325;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateReport {
    private static Map<String, Integer> reportCounts = new HashMap<>();

    // Method to handle the Generate Report use case
    public static void generateReport(Scanner scanner) {
        boolean generateAnother = true;
        while (generateAnother) {
            // Trigger: The user navigates to generate a report
            System.out.println("=== Generate Report ===");

            // Get available report types
            String[] reportTypes = getReportTypes();

            // Select report type
            String reportType = selectReportType(scanner, reportTypes);

            if (reportType != null) {
                // Generate and save the report
                if (reportType.equals("Sales Report")) {
                    generateSalesReport(scanner);
                } else if (reportType.equals("Inventory Report")) {
                    generateInventoryReport(scanner);
                } else if (reportType.equals("Shipment Report")) {
                    generateShipmentReport(scanner);
                } else if (reportType.equalsIgnoreCase("Return to Menu")) {
                    return; // Return to Inventory Management menu
                } else {
                    // For other report types, prompt for additional parameters
                    System.out.println("Enter additional parameters or filters for the selected report type:");
                    String parameters = scanner.nextLine();
                    generateAndSaveReport(reportType, parameters);
                }
            }

            // Ask if the user wants to generate another report
            generateAnother = askGenerateAnotherReport(scanner);
        }
    }

    // Method to generate the sales report
    private static void generateSalesReport(Scanner scanner) {
        String dateRange = "";
        boolean validRange = false;
        while (!validRange) {
            System.out.print("Date range (MM/DD/YYYY-MM/DD/YYYY): ");
            dateRange = scanner.nextLine();
            if (isValidDateRangeFormat(dateRange)) {
                validRange = true;
            } else {
                System.out.println("Invalid date range format. Please enter date range in MM/DD/YYYY-MM/DD/YYYY format.");
            }
        }

        // Ask for Sales amount
        String salesAmount = "";
        boolean validAmount = false;
        while (!validAmount) {
            System.out.print("Sales amount in USD ($0.00 format): ");
            salesAmount = scanner.nextLine();
            if (isValidSalesAmountFormat(salesAmount)) {
                validAmount = true;
            } else {
                System.out.println("Invalid sales amount format. Please enter sales amount in $0.00 format.");
            }
        }

        // Generate and save the report
        String parameters = "Date range: " + dateRange + ", Sales amount: " + salesAmount;
        generateAndSaveReport("Sales Report", parameters);
    }

    // Method to check if the sales amount format is valid ($0.00 format)
    private static boolean isValidSalesAmountFormat(String salesAmount) {
        Pattern pattern = Pattern.compile("\\$\\d+\\.\\d{2}");
        Matcher matcher = pattern.matcher(salesAmount);
        return matcher.matches();
    }

    // Method to check if the date range format is valid (MM/DD/YYYY-MM/DD/YYYY)
    private static boolean isValidDateRangeFormat(String dateRange) {
        String[] dates = dateRange.split("-");
        if (dates.length != 2) {
            return false;
        }
        return isValidDateFormat(dates[0]) && isValidDateFormat(dates[1]);
    }

    // Method to generate the inventory report
    private static void generateInventoryReport(Scanner scanner) {
        // Prompt for Item SKU
        System.out.print("Item SKU: ");
        String itemSku = scanner.nextLine().trim();

        // Prompt for Item Name
        System.out.print("Item Name: ");
        String itemName = scanner.nextLine().trim();

        // Prompt for List Price
        System.out.print("List Price in USD ($0.00 format): ");
        String listPrice = scanner.nextLine();
        while (!isValidSalesAmountFormat(listPrice)) {
            System.out.print("Invalid list price format. Please enter list price in $0.00 format: ");
            listPrice = scanner.nextLine();
        }

        // Prompt for Item Quantity
        int itemQuantity = 0;
        boolean validQuantity = false;
        while (!validQuantity) {
            try {
                System.out.print("Item Quantity: ");
                itemQuantity = Integer.parseInt(scanner.nextLine());
                validQuantity = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for Item Quantity.");
            }
        }

        // Generate and save the report
        String parameters = "Item SKU: " + itemSku + ", Item Name: " + itemName + ", List Price: " + listPrice + ", Current Quantity: " + itemQuantity;
        generateAndSaveReport("Inventory Report", parameters);
    }

    // Method to generate the shipment report
    private static void generateShipmentReport(Scanner scanner) {
        // Prompt for Shipping number
        System.out.print("Shipping number: ");
        String shippingNumber = scanner.nextLine();

        // Prompt for Status Update
        System.out.print("Status update: ");
        String statusUpdate = scanner.nextLine();

        // Generate and save the report
        String parameters = "Shipping number: " + shippingNumber + ", Status update: " + statusUpdate;
        generateAndSaveReport("Shipment Report", parameters);
    }

    // Method to check if the date format is valid (MM/DD/YYYY)
    private static boolean isValidDateFormat(String date) {
        Pattern pattern = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    // Method to get available report types
    public static String[] getReportTypes() {
        return new String[]{"Sales Report", "Inventory Report", "Shipment Report", "Return to Menu"};
    }

    // Method to select report type
    public static String selectReportType(Scanner scanner, String[] reportTypes) {
        System.out.println("Select the type of report to generate:");
        for (int i = 0; i < reportTypes.length; i++) {
            System.out.println((i + 1) + ". " + reportTypes[i]);
        }

        int choice;
        do {
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt() && !scanner.hasNext("[qQ]")) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next();
            }
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                choice = -1;
            }
        } while (choice < 1 || choice > reportTypes.length);

        return (choice > 0 && choice <= reportTypes.length) ? reportTypes[choice - 1] : "Return to Menu";
    }

    // Method to generate and save the report
    private static void generateAndSaveReport(String reportType, String parameters) {
        if (reportType.equalsIgnoreCase("Return to Menu")) {
            return;
        }

        int reportNumber = reportCounts.getOrDefault(reportType, 0) + 1;
        reportCounts.put(reportType, reportNumber);

        // Generate report content based on report type and parameters
        String reportContent = generateReportContent(reportType, parameters);

        // Save report to a file
        saveReportToFile(reportContent, reportType, reportNumber);
    }

    // Method to generate report content based on report type and parameters
    private static String generateReportContent(String reportType, String parameters) {
        StringBuilder reportContent = new StringBuilder();
        reportContent.append(reportType.toUpperCase()).append(" REPORT").append("\n");
        reportContent.append("=== Current Date: ").append(getCurrentDate()).append(" ===").append("\n");
        reportContent.append("=== ").append(reportType.toUpperCase()).append(" ===").append("\n");
        reportContent.append(parameters).append("\n");
        return reportContent.toString();
    }

    // Method to get the current date
    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    // Method to save report to a file
    private static void saveReportToFile(String reportContent, String reportType, int reportNumber) {
        String fileName = reportType.replace(" ", "_") + "_Report_" + reportNumber + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(reportContent);
            System.out.println("Report saved successfully to " + fileName);
        } catch (IOException e) {
            System.err.println("Error occurred while saving report: " + e.getMessage());
        }
    }

    // Method to ask if the user wants to generate another report
    private static boolean askGenerateAnotherReport(Scanner scanner) {
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Generate another report? (Y/N): ");
            String input = scanner.nextLine().toUpperCase();
            if (input.equals("Y")) {
                validInput = true;
                return true;
            } else if (input.equals("N")) {
                validInput = true;
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
            }
        }
        return false;
    }
}
