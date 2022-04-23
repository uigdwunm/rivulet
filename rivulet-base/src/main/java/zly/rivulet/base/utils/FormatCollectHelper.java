package zly.rivulet.base.utils;

import java.util.StringJoiner;

public class FormatCollectHelper {

    private final StringJoiner allLine = new StringJoiner(System.lineSeparator());

    private StringBuilder currLine = new StringBuilder();

    private int currLineTab;

    // tab
    private static final String TAB = "    ";

    /**
     * 换行
     **/
    public void line() {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < currLineTab; i++) {
            line.append(TAB);
        }
        line.append(currLine);
        allLine.add(line);
        currLine = new StringBuilder();
    }

    public void tab() {
        currLineTab++;
    }

    public void returnTab() {
        currLineTab--;
    }

    public void append(String str) {
        currLine.append(str);
    }

    public void append(StringBuilder str) {
        currLine.append(str);
    }

    public void append(int str) {
        currLine.append(str);
    }

    public void append(long str) {
        currLine.append(str);
    }

    public void append(boolean str) {
        currLine.append(str);
    }

    public void append(char str) {
        currLine.append(str);
    }
}
