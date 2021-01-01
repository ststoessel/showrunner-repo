package org.showrunner.fileutils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JarFile {

    @Getter
    private String jarFileName;

    public JarFile(String jarFileName) throws IOException {
        if (jarFileName != null && jarFileName.startsWith("/")) {
            jarFileName = jarFileName.substring(1);
        }

        this.jarFileName = jarFileName;

        createManifestMf();
    }

    public JarFile createManifestMf() throws IOException {
        addFolder("/META-INF/").addEmptyFile("/META-INF/MANIFEST.MF");
        return this;
    }

    /**
     * Einen Ordner in der Jar-Datei anlegen.
     *
     * @param folderName
     * @return
     * @throws IOException
     */
    public JarFile addFolder(String folderName) throws IOException {
        FileSystem zipfs = obtainFileSystem();
        Path metaInfFolder = zipfs.getPath(folderName);

        if (Files.notExists(metaInfFolder)) {
            Files.createDirectory(metaInfFolder);
        }
        zipfs.close();

        return this;
    }

    /**
     * Eine leer Datei in der Jar-Datei anlegen.
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public JarFile addEmptyFile(String fileName) throws IOException {
        FileSystem zipfs = obtainFileSystem();
        Path manifestFile = zipfs.getPath(fileName);

        if (Files.notExists(manifestFile)) {
            Files.createFile(manifestFile);
        }

        zipfs.close();

        return this;
    }

    /**
     * Eine externe Datei hinzufügen.
     *
     * @param source
     * @param desination
     * @return
     * @throws IOException
     */
    public JarFile addFile(String source, String desination) throws IOException {
        FileSystem zipfs = obtainFileSystem();

        Path externalTxtFile = Paths.get(source);
        Path pathInZipfile = zipfs.getPath(desination);
        // copy a file into the zip file
        Files.copy(externalTxtFile, pathInZipfile, StandardCopyOption.REPLACE_EXISTING);

        zipfs.close();

        return this;
    }

    /**
     * Todo: Muss noch korrigiert werden.
     *
     * @param destination
     * @throws IOException
     */
    private JarFile extractTo(String destination) throws IOException {
        FileSystem zipfs = obtainFileSystem();
        Path pathInZipfile = zipfs.getPath("/");
        Path output = Paths.get(destination);
        Files.copy(pathInZipfile, output, StandardCopyOption.REPLACE_EXISTING);
        zipfs.close();
        return this;
    }

    /**
     * Liefert ein virtuelles File-System.
     * Ggf. wird die JAR-Datei erzeugt.
     *
     * @return
     * @throws IOException
     */
    private FileSystem obtainFileSystem() throws IOException {
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");

        // locate file system by using the syntax
        // defined in java.net.JarURLConnection
        URI uri = URI.create("jar:file:/" + jarFileName);

        return FileSystems.newFileSystem(uri, env);
    }

}
