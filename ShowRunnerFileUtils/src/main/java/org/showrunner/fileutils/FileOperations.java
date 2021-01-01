package org.showrunner.fileutils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;

/**
 * Created by ux02418 on 13.07.2016.
 * <p>
 * Eine Sammelklasse für Dateioperationen.
 */
@Slf4j
@NoArgsConstructor
public class FileOperations {

    public boolean rename(String oldName, String newName) throws FileNotFoundException {
        File oldFile = new File(oldName);
        File newFile = new File(newName);

        if (!oldFile.exists()) {
            throw new FileNotFoundException(oldName + " does not exist!");
        }

        return oldFile.renameTo(newFile);
    }

    public void createCopy(String oldName, String newName) throws IOException {
        File oldFile = new File(oldName);
        File newFile = new File(newName);

        if (!oldFile.exists()) {
            throw new FileNotFoundException(oldName + " does not exist!");
        }

        try (InputStream is = new FileInputStream(oldFile); OutputStream os = new FileOutputStream(newFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    public String createBackupWithTimeStamp(String oldName) throws IOException {
        String timestamp = (new Timestamp(System.currentTimeMillis())).toString().replace(" ", "_").replace(":", "");
        String newName = oldName + "." + timestamp;

        createCopy(oldName, newName);
        return newName;
    }

    public boolean existFile(String fielName) {
        return (new File(fielName).exists());
    }

    public boolean isDirectory(String fileName) {
        return new File(fileName).isDirectory();
    }

    public boolean deleteFile(String fileName) {
        try {
            Files.delete(Paths.get(fileName));
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    public boolean binaryCompare(String fileName1, String fileName2) throws IOException {
        File file1 = new File(fileName1);
        File file2 = new File(fileName2);

        if (file1.length() != file2.length()) {
            return false;
        }

        try (InputStream is1 = new FileInputStream(file1); InputStream is2 = new FileInputStream(file2)) {
            byte[] buffer1 = new byte[1024];
            byte[] buffer2 = new byte[1024];

            int length;
            while ((length = is1.read(buffer1)) > 0) {
                int length2 = is2.read(buffer2);

                if (length != length2) {
                    return false;
                }

                for (int i = 0; i < length; i++) {
                    if (buffer1[i] != buffer2[i]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


}
