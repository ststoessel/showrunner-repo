package org.showrunner.fileutils;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSearchTest {

    @Test
    public void searchRecursive() {
        FileSearch search = new FileSearch();

        assertTrue(search.searchRecursive("./testdata", ".log", FilterMode.ENDS).size() == 1);
        assertTrue(search.searchRecursive("./testdata", "text", FilterMode.STARTS).size() == 5);
        assertTrue(search.searchRecursive("./testdata", "dummy.log", FilterMode.EQUALS).size() == 1);
        assertTrue(search.searchRecursive("./testdata", "hello", FilterMode.CONTAINS).size() == 1);
        assertTrue(search.searchRecursive("./testdata", "hello", FilterMode.NONE).size() >= 6);
        assertTrue(search.searchRecursive("./testdata", "hello", FilterMode.EQUALS).size() == 0);

        assertTrue(search.searchRecursive("./testdata", null, FilterMode.EQUALS).size() == 0);
        assertTrue(search.searchRecursive(null, "", FilterMode.EQUALS).size() == 0);
        assertTrue(search.searchRecursive(null, null, FilterMode.EQUALS).size() == 0);

        assertTrue(search.searchRecursive("./testdata", "dummy", FilterMode.STARTS, TreeScanMode.SKIP_HIDDEN, FileType.ALL).size() == 2);
        assertTrue(search.searchRecursive("./testdata", "dummy", FilterMode.STARTS, TreeScanMode.SKIP_HIDDEN, FileType.ONLY_DIRS).size() == 1);
        assertTrue(search.searchRecursive("./testdata", "dummy", FilterMode.STARTS, TreeScanMode.SKIP_HIDDEN, FileType.ONLY_FILES).size() == 1);
    }

}