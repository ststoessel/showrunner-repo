package org.showrunner.fileutils;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Created by ux02418 on 08.07.2016.
 */
public class TextFileTest {


    @Test
    public void testReadWrite() throws Exception {

        String fileName = "./testdata/new.txt";

        // Eine Textdatei wird angelegt
        TextFile textFileWriting = new TextFile();
        List<String> content = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            content.add("Line " + i);
        }

        textFileWriting.setContent(content);
        textFileWriting.save(fileName);

        // Die Datei wird nun gelesen
        TextFile textFileReading = new TextFile();
        textFileReading.load(fileName);

        List<String> content1 = textFileWriting.getContent();
        List<String> content2 = textFileReading.getContent();

        assertTrue(textFileReading.getNumberOfLines() == 10);

        // Nun wird der Inhalt verglichen
        for (int i = 0; i < content1.size(); i++) {
            assertTrue(content1.get(i).equals(content2.get(i)));
        }
    }

    @Test
    public void replaceLine() {
        TextFile textFile = new TextFile();

        for (int i = 0; i < 10; i++) {
            textFile.addLine("Line " + i);
        }
        System.out.println(textFile.toString());

        textFile.repaceLine(5, "XXX");
        assertTrue("XXX".equals(textFile.getSingleLine(5)));

        System.out.println(textFile.toString());

    }

    @Test
    public void loadFromClasspath() throws IOException {
        TextFile textFile = new TextFile();
        textFile.loadFromClasspath("/META-INF/MANIFEST.MF", StandardCharsets.UTF_8);
        // Das die Datei gelesen wird, ist der Test.
    }


}