package org.showrunner.fileutils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ux02418 on 08.07.2016.
 * <p>
 * Eine Wrapper für eine zeilenbasierte Text-Datei.
 */
@Slf4j
public class TextFile {

    @Getter
    @Setter
    private List<String> content;


    public TextFile() {
        content = new ArrayList<>();
    }

    /**
     * Textdatei zeilenweise lesen. Das Encoding ist per Default "UTF-8".
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public void load(String fileName) throws IOException {
        load(fileName, StandardCharsets.UTF_8);
    }


    /**
     * Textdatei zeilenweise lesen. Das Encoding kann frei gewählt werden (siehe InputStreamReader).
     *
     * @param encoding
     * @param fileName
     * @throws IOException
     */
    public void load(String fileName, Charset encoding) throws IOException {
        checkContentForNull();
        content.clear();


        File file = new File(fileName);

        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), encoding);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                content.add(line);
            }
        }
    }

    /**
     * Datei wird aus dem Classpath gelesen. Das Encoding kann mitgegeben werden.
     *
     * @param fileName
     * @param encoding
     * @throws IOException
     */
    public void loadFromClasspath(String fileName, Charset encoding) throws IOException {
        checkContentForNull();
        content.clear();

        try (InputStreamReader isr = new InputStreamReader(this.getClass().getResourceAsStream(fileName), encoding);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                content.add(line);
            }
        }
    }


    /**
     * Default ist wieder UTF-8.
     *
     * @param fileName
     * @throws IOException
     */
    public void loadFromClasspath(String fileName) throws IOException {
        loadFromClasspath(fileName, StandardCharsets.UTF_8);
    }


    /**
     * Textdatei zeilenweise speichern.
     *
     * @return
     * @throws IOException
     */

    public boolean save(String fileName, Charset encoding) throws IOException {
        checkContentForNull();

        File file = new File(fileName);

        // Wenn die Datei nicht existiert UND nicht angelegt werden kann
        if (!file.exists() && !file.createNewFile()) {
            return false;
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), encoding)) {

            for (String line : content) {
                // Wenn nicht vorhanden wird ein Linefeed drangehängt
                if (!line.endsWith("\n")) {
                    line += "\n";
                }
                writer.write(line);
            }

        }
        return true;
    }

    public boolean save(String fileName) throws IOException {
        return save(fileName, StandardCharsets.UTF_8);
    }


    /**
     * An den Dateinamen wird ".bak" drangehängt.
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public boolean saveBak(String fileName) throws IOException {
        return save(fileName + ".bak");
    }


    /**
     * An den Dateinamen wird ein Timestamp gehängt.
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public boolean saveTimestamp(String fileName) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Timestamp now = new Timestamp(new Date().getTime());
        String timestamp = sdf.format(now);

        return save(fileName + "." + timestamp);
    }


    /**
     * Ändert den Inhalt der die n-te Zeile. Beginnend bei 0.
     *
     * @param nr
     * @param line
     */
    public void repaceLine(final int nr, String line) {
        int lineNr = nr;
        if (lineNr < 0) {
            lineNr = 0;
        } else if (lineNr > getNumberOfLines() - 1) {
            lineNr = getNumberOfLines() - 1;
        }

        content.remove(lineNr);
        content.add(lineNr, line);
    }

    /**
     * Hängt eine Zeile dran.
     *
     * @param line
     */
    public void addLine(String line) {
        content.add(line);
    }


    /**
     * Liefert die n-te Zeile. Beginnend bei 0.
     *
     * @param nr
     */
    public String getSingleLine(int nr) {
        int lineNr = nr;
        if (lineNr < 0) {
            lineNr = 0;
        } else if (lineNr > getNumberOfLines() - 1) {
            lineNr = getNumberOfLines() - 1;
        }

        return content.get(lineNr);
    }

    public int getNumberOfLines() {
        checkContentForNull();
        return content.size();
    }

    private void checkContentForNull() {
        if (content == null) {
            content = new ArrayList<>();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextFile textFile = (TextFile) o;

        return getContent() != null ? getContent().equals(textFile.getContent()) : textFile.getContent() == null;

    }

    @Override
    public int hashCode() {
        return getContent() != null ? getContent().hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (String line : content) {
            output.append(line + "\n");
        }

        return output.toString();
    }

}
