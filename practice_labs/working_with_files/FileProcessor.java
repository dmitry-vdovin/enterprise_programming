package org.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileProcessor {

    private static final Pattern TAB_SEQUENCE = Pattern.compile("(\\t+)");

    public static void processFile(Path input, Path output, int width, int tabSize) throws IOException {
        String content = Files.readString(input, StandardCharsets.UTF_8);

        List<String> segments = new ArrayList<>();
        Matcher m = TAB_SEQUENCE.matcher(content);
        int last = 0;
        while (m.find()) {
            if (m.start() > last) {
                segments.add(content.substring(last, m.start()));
            }
            segments.add(m.group(1));
            last = m.end();
        }
        if (last < content.length()) segments.add(content.substring(last));

        List<String> outLines = new ArrayList<>();

        for (String seg : segments) {
            if (seg.isEmpty()) {
                outLines.add(padRight("", width));
                continue;
            }

            if (isTabsOnly(seg)) {

                String line = seg;
                line = padToVisualWidth(line, width, tabSize);
                outLines.add(line);
            } else {
                String normalizedSegment = seg.replaceAll("\\r\\n", "\n"); // unify endings
                String[] paras = normalizedSegment.split("\\n\\s*\\n");
                for (int i = 0; i < paras.length; i++) {
                    String p = paras[i].trim();
                    if (p.isEmpty()) {
                        outLines.add(padRight("", width));
                    } else {
                        List<String> wrapped = TextWrapper.wrapParagraphWithTabs(p, width, tabSize);
                        outLines.addAll(wrapped);
                    }
                }
            }
        }

        Files.write(output, outLines, StandardCharsets.UTF_8);
    }

    private static boolean isTabsOnly(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '\t') return false;
        }
        return true;
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
}
