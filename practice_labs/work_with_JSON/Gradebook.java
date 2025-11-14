package lab4;

import java.util.*;
import org.json.*;

class GradeBook {

    class Exams {

        private final String subject;
        private final int grade;
        private final String teacher;

        public Exams(String s, int gr, String t) {
            subject = (s != null && !s.isEmpty()) ? s : "unknown";
            grade = Math.max(gr, 0);
            teacher = (t != null && !t.isEmpty()) ? t : "unknown";
        }

        public Exams() {
            this("unknown", -1, "unknown");
        }

        public String Outputex() {
            return subject + " " + grade + " " + teacher;
        }

        public int GetGrade() {
            return grade;
        }
    }

    private String name;
    private double avergrade;
    private final Vector<Vector<Exams>> sessions = new Vector<>();

    public GradeBook() {
        this.name = "unknown";
        this.avergrade = 0;
    }

    public GradeBook(String n, Vector<Exams> v) {
        this.name = (n != null && !n.isEmpty()) ? n : "unknown";
        if (v != null) {
            sessions.add(new Vector<>(v));
        }
        RecalculateAverage();
    }

    public String GetName() {
        return name;
    }

    public double GetAvergrade() {
        return avergrade;
    }

    public int GetTotalExams() {
        int count = 0;
        for (Vector<Exams> s : sessions) {
            count += s.size();
        }
        return count;
    }

    public double FindAverage(Vector<Exams> ex) {
        if (ex == null || ex.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (Exams e : ex) {
            sum += e.GetGrade();
        }
        return sum / ex.size();
    }

    public void RecalculateAverage() {
        if (sessions.isEmpty()) {
            avergrade = 0;
            return;
        }
        double total = 0;
        for (Vector<Exams> s : sessions) {
            total += FindAverage(s);
        }
        avergrade = total / sessions.size();
    }

    public void FileInput(Scanner in) {
        if (in == null) {
            return;
        }

        // читаем имя
        String nameLine = "";
        while (in.hasNextLine() && nameLine.isEmpty()) {
            nameLine = in.nextLine().trim();
        }
        this.name = nameLine.isEmpty() ? "unknown" : nameLine;

        if (!in.hasNextInt()) {
            avergrade = 0;
            return;
        }

        int sessionCount = in.nextInt();
        in.nextLine();
        sessions.clear();

        for (int i = 0; i < sessionCount && in.hasNextLine(); i++) {
            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                i--;
                continue;
            }

            try {
                int examCount = Integer.parseInt(line);
                Vector<Exams> sessionExams = new Vector<>();

                for (int j = 0; j < examCount && in.hasNextLine(); j++) {
                    String examLine = in.nextLine().trim();
                    if (examLine.isEmpty()) {
                        continue;
                    }

                    String[] parts = examLine.split("\\s+", 3);
                    if (parts.length == 3) {
                        String subject = parts[0];
                        int grade = safeParseInt(parts[1], -1);
                        String teacher = parts[2];
                        sessionExams.add(new Exams(subject, grade, teacher));
                    }
                }

                if (!sessionExams.isEmpty()) {
                    sessions.add(sessionExams);
                }

            } catch (NumberFormatException ignored) {
            }
        }

        RecalculateAverage();
    }

    private int safeParseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    public String Output() {
        RecalculateAverage();
        StringBuilder out = new StringBuilder();
        out.append(name).append("; Total average: ").append(avergrade).append("\n");
        for (int i = 0; i < sessions.size(); i++) {
            double avg = FindAverage(sessions.get(i));
            out.append("Session ").append(i + 1).append("; Average grade: ").append(avg).append("\n");
            for (Exams e : sessions.get(i)) {
                out.append(e.Outputex()).append("\n");
            }
            out.append("\n");
        }
        return out.toString();
    }

    public static Comparator<GradeBook> byAverageDesc = Comparator.comparingDouble(GradeBook::GetAvergrade).reversed();
    public static Comparator<GradeBook> byNameAsc = Comparator.comparing(GradeBook::GetName);
    public static Comparator<GradeBook> byExamCountDesc = Comparator.comparingInt(GradeBook::GetTotalExams).reversed();

    public static void PrintExcellentStudents(Vector<GradeBook> list) {
        if (list == null) {
            return;
        }
        System.out.println("=== Excellent students ===");
        for (GradeBook gb : list) {
            gb.RecalculateAverage();
            if (gb.GetAvergrade() >= 9.0) {
                System.out.println(gb.GetName() + " -> " + gb.GetAvergrade());
            }
        }
    }

    public static void PrintAll(Vector<GradeBook> list) {
        if (list == null) {
            return;
        }
        for (GradeBook gb : list) {
            System.out.println(gb.Output());
        }
    }

    public String toJSON() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("averageGrade", avergrade);

        JSONArray sessionArray = new JSONArray();
        for (Vector<Exams> session : sessions) {
            JSONArray examsArray = new JSONArray();
            for (Exams e : session) {
                JSONObject exam = new JSONObject();
                exam.put("subject", e.subject);
                exam.put("grade", e.grade);
                exam.put("teacher", e.teacher);
                examsArray.put(exam);
            }
            JSONObject sessionObj = new JSONObject();
            sessionObj.put("exams", examsArray);
            sessionArray.put(sessionObj);
        }
        json.put("sessions", sessionArray);

        return json.toString(4);
    }

    public void writeToJSONFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            return;
        }
        try (java.io.FileWriter file = new java.io.FileWriter(filename)) {
            file.write(this.toJSON());
            System.out.println("Data successfully written to " + filename);
        } catch (java.io.IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void saveAllStudentsToJSON(Vector<GradeBook> students, String filename) {
        if (students == null || filename == null || filename.isEmpty()) {
            return;
        }
        try (java.io.FileWriter file = new java.io.FileWriter(filename)) {
            JSONArray jsonArray = new JSONArray();
            for (GradeBook gb : students) {
                jsonArray.put(new JSONObject(gb.toJSON()));
            }
            file.write(jsonArray.toString(4));
            System.out.println("All students successfully saved to " + filename);
        } catch (java.io.IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void readFromJSONString(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return;
        }
        try {
            JSONObject json = new JSONObject(jsonString);
            this.name = json.optString("name", "unknown");
            this.avergrade = json.optDouble("averageGrade", 0.0);

            this.sessions.clear();
            JSONArray jsonSessions = json.optJSONArray("sessions");
            if (jsonSessions != null) {
                for (int i = 0; i < jsonSessions.length(); i++) {
                    JSONObject jsonSession = jsonSessions.getJSONObject(i);
                    JSONArray jsonExams = jsonSession.optJSONArray("exams");
                    Vector<Exams> session = new Vector<>();

                    if (jsonExams != null) {
                        for (int j = 0; j < jsonExams.length(); j++) {
                            JSONObject jsonExam = jsonExams.getJSONObject(j);
                            String subject = jsonExam.optString("subject", "unknown");
                            int grade = jsonExam.optInt("grade", -1);
                            String teacher = jsonExam.optString("teacher", "unknown");
                            session.add(new Exams(subject, grade, teacher));
                        }
                    }
                    this.sessions.add(session);
                }
            }
            RecalculateAverage();
        } catch (Exception e) {
            System.out.println("Error reading from JSON string: " + e.getMessage());
        }
    }

    public static Vector<GradeBook> loadAllStudentsFromJSON(String filename) {
        Vector<GradeBook> students = new Vector<>();
        if (filename == null || filename.isEmpty()) {
            return students;
        }

        try (Scanner scanner = new Scanner(new java.io.File(filename))) {
            StringBuilder jsonContent = new StringBuilder();
            while (scanner.hasNextLine()) {
                jsonContent.append(scanner.nextLine());
            }

            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                GradeBook gb = new GradeBook();
                gb.readFromJSONString(jsonObject.toString());
                students.add(gb);
            }

            System.out.println("All students successfully loaded from " + filename);
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
        return students;
    }
}
