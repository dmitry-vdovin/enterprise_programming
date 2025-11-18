package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextWrapper {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("(\\t+)|([^\\t\\s]+)");

    public static List<String> wrapParagraphWithTabs(String paragraph, int width, int tabSize) {
        String normalized = paragraph.replaceAll("[\\u000B\\f\\r\\n ]+", " ").trim();
        if (normalized.isEmpty()) {
            List<String> empty = new ArrayList<>();
            empty.add(padRight("", width));
            return empty;
        }

        List<Token> tokens = new ArrayList<>();
        Matcher m = TOKEN_PATTERN.matcher(normalized);
        while (m.find()) {
            if (m.group(1) != null) {
                tokens.add(new Token(m.group(1), true)); // tabs
            } else {
                tokens.add(new Token(m.group(2), false)); // word
            }
        }

        List<String> result = new ArrayList<>();
        List<Token> current = new ArrayList<>();
        int sumWidths = 0;
        boolean isFirstLine = true;

        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            int tWidth = t.visualWidth(tabSize);

            if (current.isEmpty() && isFirstLine) {
                Token indent = new Token("\t", true);
                current.add(indent);
                sumWidths += indent.visualWidth(tabSize);
            }

            if (current.isEmpty()) {
                current.add(t);
                sumWidths += tWidth;
            } else {
                Token prev = current.get(current.size() - 1);
                int baseBetween = (prev.isTab || t.isTab) ? 0 : 1;

                int projected = sumWidths + baseBetween + tWidth;
                if (projected <= width) {
                    current.add(t);
                    sumWidths += tWidth;
                } else {
                    result.add(buildLine(current, sumWidths, width, tabSize, false));
                    current.clear();
                    sumWidths = 0;
                    isFirstLine = false;
                    current.add(t);
                    sumWidths += tWidth;
                }
            }
        }

        if (!current.isEmpty()) {
            result.add(buildLine(current, sumWidths, width, tabSize, true));
        }

        List<String> fixed = new ArrayList<>(result.size());
        for (String line : result) {
            fixed.add(padToVisualWidth(line, width, tabSize));
        }

        return fixed;
    }

    private static String buildLine(List<Token> current, int sumWidths, int width, int tabSize, boolean isLast) {
        if (current.size() == 0) return padRight("", width);

        if (current.size() == 1) {
            return padToVisualWidth(current.get(0).text, width, tabSize);
        }

        int gaps = current.size() - 1;
        int[] baseBetween = new int[gaps];
        int totalBase = 0;
        for (int i = 0; i < gaps; i++) {
            Token left = current.get(i);
            Token right = current.get(i + 1);
            int b = (left.isTab || right.isTab) ? 0 : 1;
            baseBetween[i] = b;
            totalBase += b;
        }

        StringBuilder sb = new StringBuilder();
        if (isLast) {
            for (int i = 0; i < current.size(); i++) {
                sb.append(current.get(i).text);
                if (i < current.size() - 1) {
                    int cnt = baseBetween[i];
                    for (int s = 0; s < cnt; s++) sb.append(' ');
                }
            }
            return sb.toString();
        } else {
            int totalSpacesToDistribute = width - sumWidths - totalBase;
            int addPerGap = (gaps > 0) ? totalSpacesToDistribute / gaps : 0;
            int extra = (gaps > 0) ? totalSpacesToDistribute % gaps : 0;

            for (int i = 0; i < current.size(); i++) {
                sb.append(current.get(i).text);
                if (i < current.size() - 1) {
                    int spaces = baseBetween[i] + addPerGap + (i < extra ? 1 : 0);
                    for (int s = 0; s < spaces; s++) sb.append(' ');
                }
            }
            return sb.toString();
        }
    }

    private static String padRight(String s, int width) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < width) sb.append(' ');
        return sb.toString();
    }

    private static String padToVisualWidth(String s, int width, int tabSize) {
        int vis = visualWidth(s, tabSize);
        StringBuilder sb = new StringBuilder(s);
        while (vis < width) {
            sb.append(' ');
            vis++;
        }
        if (vis > width) {
            while (sb.length() > 0 && visualWidth(sb.toString(), tabSize) > width) {
                sb.setLength(sb.length() - 1);
            }
            while (visualWidth(sb.toString(), tabSize) < width) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    private static int visualWidth(String s, int tabSize) {
        int w = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\t') w += tabSize;
            else w += 1;
        }
        return w;
    }

    private static class Token {
        String text;
        boolean isTab;

        Token(String t, boolean isTab) {
            this.text = t;
            this.isTab = isTab;
        }

        int visualWidth(int tabSize) {
            if (!isTab) return text.length();
            else return text.length() * tabSize;
        }
    }
}
