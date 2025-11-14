package lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Vector<GradeBook> students = new Vector<>();

        // Чтение студентов из файла
        try (Scanner fileScanner = new Scanner(new File("students.txt"))) {
            if (!fileScanner.hasNextInt()) {
                System.out.println("Invalid input file format.");
                return;
            }
            int count_stud = fileScanner.nextInt();
            fileScanner.nextLine();

            for (int i = 0; i < count_stud; i++) {
                GradeBook gb = new GradeBook();
                gb.FileInput(fileScanner);
                students.add(gb);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File 'input_stud.txt' not found.");
            return;
        } catch (Exception e) {
            System.out.println("Error reading input file: " + e.getMessage());
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose option: ");

            switch (choice) {
                case 1 -> {
                    students.sort(GradeBook.byNameAsc);
                    System.out.println("Sorted by name:");
                    GradeBook.PrintAll(students);
                }
                case 2 -> {
                    students.sort(GradeBook.byAverageDesc);
                    System.out.println("Sorted by average grade:");
                    GradeBook.PrintAll(students);
                }
                case 3 -> {
                    students.sort(GradeBook.byExamCountDesc);
                    System.out.println("Sorted by number of exams:");
                    GradeBook.PrintAll(students);
                }
                case 4 -> {
                    System.out.println("All students:");
                    GradeBook.PrintAll(students);
                }
                case 5 -> GradeBook.PrintExcellentStudents(students);
                case 6 -> {
                    System.out.println("JSON of all students:");
                    for (GradeBook gb : students) {
                        System.out.println(gb.toJSON());
                    }
                }
                case 7 -> {
                    String filename = readString("Enter filename to save all students: ");
                    GradeBook.saveAllStudentsToJSON(students, filename);
                }
                case 8 -> {
                    String filename = readString("Enter filename to load all students: ");
                    Vector<GradeBook> loaded = GradeBook.loadAllStudentsFromJSON(filename);
                    if (!loaded.isEmpty()) {
                        students = loaded;
                        System.out.println("All students loaded successfully.");
                    }
                }
                case 9 -> {
                    students.clear();
                    System.out.println("GradeBook data cleared.");
                }
                case 0 -> running = false;
                default -> System.out.println("Invalid option. Try again.");
            }
        }

        System.out.println("Program finished.");
    }

    private static void printMenu() {
        System.out.println("\n===== GradeBook Menu =====");
        System.out.println("1. Sort by name");
        System.out.println("2. Sort by average grade");
        System.out.println("3. Sort by number of exams");
        System.out.println("4. Show all students");
        System.out.println("5. Show excellent students");
        System.out.println("6. Show JSON of all students");
        System.out.println("7. Save students to JSON file");
        System.out.println("8. Load GradeBook from JSON file");
        System.out.println("9. Clear Gradebook data");
        System.out.println("0. Exit");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
