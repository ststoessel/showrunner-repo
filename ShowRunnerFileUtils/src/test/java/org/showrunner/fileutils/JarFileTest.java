package org.showrunner.fileutils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class JarFileTest {

    private static final String jarFileName = System.getProperty("user.dir").replace("\\", "/") + "/testdata/empty.jar";

    @BeforeEach
    public void cleanUp() {
        File previous = new File(jarFileName);
        if (previous.exists()) {
            previous.delete();
        }
    }

    @Test
    public void create() throws IOException {

        log.info("jarFileName = " + jarFileName);

        JarFile jarFile = new JarFile(jarFileName);

        jarFile.addFile("./testdata/text.txt", "/test.txt");

        assertTrue(new File(jarFileName).exists());

    }
}