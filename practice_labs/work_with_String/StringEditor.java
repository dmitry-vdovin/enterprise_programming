import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StringEditor {
    public static void main(String[] args) {
        System.out.println("Вводите строки текста. Пустая строка — конец ввода.");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            StringBuffer result = new StringBuffer();
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                result.append(fixLineByWords(line)).append(System.lineSeparator());
            }
            System.out.println("Исправленный текст:");
            System.out.print(result.toString());
        } catch (IOException e) {
            System.out.println("Ошибка чтения с клавиатуры");
        }
    }

    // В тексте после буквы Р, если она не последняя в слове, ошибочно напечатана буква А вместо О.
    private static String fixLineByWords(String line) {
        StringBuffer out = new StringBuffer(line.length());
        StringBuffer word = new StringBuffer();

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (Character.isLetter(ch)) {
                word.append(ch);
            } else {
                if (word.length() > 0) {
                    out.append(fixWord(word));
                    word.setLength(0);
                }
                out.append(ch);
            }
        }
        if (word.length() > 0) out.append(fixWord(word));
        return out.toString();
    }

    private static String fixWord(CharSequence w) {
        StringBuffer sb = new StringBuffer(w);
        for (int i = 0; i + 1 < sb.length(); i++) {
            char c = sb.charAt(i);
            char next = sb.charAt(i + 1);
            // учитываем все комбинации регистра после 'Р'/'р'
            if ((c == 'Р' || c == 'р') && (next == 'А' || next == 'а')) {
                sb.setCharAt(i + 1, Character.isUpperCase(next) ? 'О' : 'о');
            }
        }
        return sb.toString();
    }
}