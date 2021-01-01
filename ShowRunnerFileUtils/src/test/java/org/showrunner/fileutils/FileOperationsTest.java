package org.showrunner.fileutils;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Created by ux02418 on 13.07.2016.
 */
public class FileOperationsTest {

    private String oldFileName = "./testdata/not_renamed.txt";
    private String newFileName = "./testdata/renamed.txt";

    @BeforeEach
    public void prepare() throws IOException {
        File oldFile = new File(oldFileName);
        oldFile.createNewFile();

        File newFile = new File(newFileName);
        newFile.delete();
    }


    @Test
    public void rename() throws Exception {
        FileOperations operations = new FileOperations();
        operations.rename(oldFileName, newFileName);
        File file = new File(newFileName);
        assertTrue(file.exists());
    }

    @Test
    public void createBackupWithTimestamp() throws Exception {
        FileOperations operations = new FileOperations();
        String newFileName = operations.createBackupWithTimeStamp(oldFileName);
        boolean exists = (new File(newFileName)).exists();
        System.out.println(newFileName);
        assertTrue(exists);
    }

    @Test
    public void binaryCompare() throws IOException {
        FileOperations operations = new FileOperations();
        boolean equal = operations.binaryCompare("./testdata/text.txt", "./testdata/text_identical.txt");
        assertTrue(equal);

        boolean notEqual = operations.binaryCompare("./testdata/text.txt", "./testdata/text_not_identical.txt");
        assertFalse(notEqual);

        boolean notEqual2 = operations.binaryCompare("./testdata/text.txt", "./testdata/text_not_identical_smaller.txt");
        assertFalse(notEqual2);


    }


}