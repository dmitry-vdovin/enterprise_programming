package org.example;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in, "UTF-8");
        try {
            System.out.print("input: ");
            String inputPath = sc.nextLine().trim();

            System.out.print("output ( Enter for output.txt): ");
            String out = sc.nextLine().trim();
            String outputPath = out.isEmpty() ? "output.txt" : out;

            System.out.print("string length (целое > 0): ");
            int width;
            try {
                width = Integer.parseInt(sc.nextLine().trim());
                if (width <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.err.println("Ошибка: ширина должна быть положительным целым числом.");
                return;
            }

            int tabSize =4 ;

            Path in = Path.of(inputPath);
            Path outPath = Path.of(outputPath);

            FileProcessor.processFile(in, outPath, width, tabSize);
            System.out.println("ready: " + outPath.toAbsolutePath());
        } catch (Exception ex) {
            System.err.println("error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
