package org.showrunner.fileutils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@NoArgsConstructor
public class FileSearch {

    /**
     * @param path
     * @param searchFragment
     * @param filterMode
     * @return
     */
    public List<String> searchRecursive(String path, String searchFragment, FilterMode filterMode) {
        return searchRecursive(path, searchFragment, filterMode, TreeScanMode.SKIP_HIDDEN);
    }

    /**
     * @param path
     * @param searchFragment
     * @param filterMode
     * @param fileType
     * @return
     */
    public List<String> searchRecursive(String path, String searchFragment, FilterMode filterMode, FileType fileType) {
        return searchRecursive(path, searchFragment, filterMode, TreeScanMode.SKIP_HIDDEN, fileType);
    }

    /**
     * Kompatibilitäts-Methode.
     * FileType wird hier Defaultmäßig als ALL gesetzt.
     *
     * @param path
     * @param searchFragment
     * @param filterMode
     * @param treeScanMode
     * @return
     */
    public List<String> searchRecursive(String path, String searchFragment, FilterMode filterMode, TreeScanMode treeScanMode) {
        return searchRecursive(path, searchFragment, filterMode, treeScanMode, FileType.ONLY_FILES);
    }

    /**
     * @param path           Der Suchpfad.
     * @param searchFragment SearchFragment bezieht sich auf den Dateinamen ohne Pfad.
     * @param filterMode     Wie das Fragment im Dateinamen gesucht wird.
     * @param treeScanMode   Alle Ordner durchsuchen oder versteckte Ordner ausblenden.
     * @param fileType       Alle Dateien (egal ob Ordner oder Datei), nur Ordner, nur Dateien.
     * @return
     */
    public List<String> searchRecursive(String path, String searchFragment, FilterMode filterMode, TreeScanMode treeScanMode, FileType fileType) {

        List<String> findingsFilesList = new ArrayList<>();
        Map<String, String> findingsDirsMap = new HashMap<>();

        if (path == null || searchFragment == null || filterMode == null) {
            log.warn("At least one parameter is NULL path=>" + path + "< searchFragment=>" + searchFragment + "< filterMode=>" + filterMode + "<");
            return findingsFilesList;
        }

        FileVisitor<Path> simpleFileVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                log.debug("Recursive search: " + dir.toString());

                String completeDirName = dir.toString().replace("\\", "/");
                String dirName = completeDirName.substring(completeDirName.lastIndexOf("/") + 1);

                // Versteckte Verzeichnisse werden ignoriert
                if (treeScanMode == TreeScanMode.SKIP_HIDDEN && (completeDirName.matches("(.*)[/][.][a-zA-Z0-9](.*)") || completeDirName.matches("^[.][a-zA-Z0-9](.*)"))) {
                    log.info("Hidden folder skipping: " + completeDirName);
                    return FileVisitResult.SKIP_SUBTREE;
                }

                if (filterMode == FilterMode.ENDS) {
                    if (dirName.endsWith(searchFragment)) {
                        findingsDirsMap.put(completeDirName, completeDirName);
                    }
                } else if (filterMode == FilterMode.STARTS) {
                    if (dirName.startsWith(searchFragment)) {
                        findingsDirsMap.put(completeDirName, completeDirName);
                    }
                } else if (filterMode == FilterMode.EQUALS) {
                    if (dirName.equals(searchFragment)) {
                        findingsDirsMap.put(completeDirName, completeDirName);
                    }
                } else if (filterMode == FilterMode.CONTAINS) {
                    if (dirName.contains(searchFragment)) {
                        findingsDirsMap.put(completeDirName, completeDirName);
                    }
                } else {
                    findingsDirsMap.put(completeDirName, completeDirName);
                }


                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult visitFileFailed(Path file, IOException io) {
                return FileVisitResult.SKIP_SUBTREE;
            }

            @Override
            public FileVisitResult visitFile(Path visitedFile, BasicFileAttributes attrs) {
                String completeFileName = visitedFile.toString().replace("\\", "/");
                String fileName = visitedFile.getFileName().toString();

                if (filterMode == FilterMode.ENDS) {
                    if (fileName.endsWith(searchFragment)) {
                        findingsFilesList.add(completeFileName);
                    }
                } else if (filterMode == FilterMode.STARTS) {
                    if (fileName.startsWith(searchFragment)) {
                        findingsFilesList.add(completeFileName);
                    }
                } else if (filterMode == FilterMode.EQUALS) {
                    if (fileName.equals(searchFragment)) {
                        findingsFilesList.add(completeFileName);
                    }
                } else if (filterMode == FilterMode.CONTAINS) {
                    if (fileName.contains(searchFragment)) {
                        findingsFilesList.add(completeFileName);
                    }
                } else {
                    findingsFilesList.add(completeFileName);
                }

                return FileVisitResult.CONTINUE;
            }
        };

        FileSystem fileSystem = FileSystems.getDefault();

        Path rootPath = fileSystem.getPath(path);

        try {
            Files.walkFileTree(rootPath, simpleFileVisitor);
        } catch (IOException ioe) {
            log.warn("Error while walking through directory tree", ioe);
        }

        List<String> findingsDirsList = new ArrayList<>(findingsDirsMap.values());

        List<String> ret = new ArrayList<>();
        if (fileType == FileType.ONLY_FILES) {
            ret.addAll(findingsFilesList);
        } else if (fileType == FileType.ONLY_DIRS) {
            ret.addAll(findingsDirsList);
        } else {
            ret.addAll(findingsFilesList);
            ret.addAll(findingsDirsList);
        }

        return ret;
    }


}
