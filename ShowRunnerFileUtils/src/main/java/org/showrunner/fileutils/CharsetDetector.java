package org.showrunner.fileutils;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;


/**
 * Created by us61246 on 24.01.2017.
 */
@Slf4j
public class CharsetDetector {


    /**
     * Check if a file can be read without an error with one of the given charset names
     * Returns "" in case of error or no valid charset found
     */
    public String detectCharset(
            String absoluteFileName,
            List<String> charsets
    ) {
        charsets = charsets;
        Charset charset = null;
        byte[] fileBuffer = this.getFileBuffer(absoluteFileName);
        if (fileBuffer.length == 0) {
            log.warn("File " + absoluteFileName + " not found. FilebufferLen=0");
            return "";
        }

        for (String charsetName : charsets) {
            charset = findCharset(fileBuffer, Charset.forName(charsetName));
            if (charset != null) {
                break;
            }
        }
        if (charset == null) {
            log.warn("File does not contain any of the given charSets");
            return "";
        }
        return charset.name();
    }

    public String detectCharset(byte[] fileBuffer, String[] charsets) {
        Charset charset = null;
        for (String charsetName : charsets) {
            charset = findCharset(fileBuffer, Charset.forName(charsetName));
            if (charset != null) {
                break;
            }
        }
        return (charset == null) ? null : charset.name();
    }


    /**
     * This method can be used to get the bytes of a file
     * and further on to read from the bytes like reading data from a file
     * In the moment this method is used by detect charset only
     */
    public byte[] getFileBuffer(String absoluteFileName) {
        File f = new File(absoluteFileName);

        if (f == null || f.length() == 0) {
            log.error("File is null or file length is 0 " + absoluteFileName);
            return null;
        }

        long fileLength = f.length();
        // Take care that you are not using long values bigger than max int values
        // cause byte is using int as Max buffer length is round about 2 GB
        if (fileLength > Integer.MAX_VALUE) {
            fileLength = Integer.MAX_VALUE;
        }
        byte[] buffer = new byte[(int) fileLength];

        // Read the file
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));
            long readLength = input.read(buffer);
            input.close();

            if (readLength != fileLength) {
                log.error("Could not read all bytes " + readLength + " from file " + absoluteFileName + " (" + fileLength + ")");
                return null;
            }

        } catch (Exception ex) {
            log.error("Cannot read file into buffer " + absoluteFileName);
            return null;
        }

        return buffer;
    }


    private Charset findCharset(byte[] fileBuffer, Charset charset) {
        CharsetDecoder decoder = charset.newDecoder();
        boolean identified = false;

        decoder.reset();
        identified = identify(fileBuffer, decoder);

        if (identified) {
            return charset;
        } else {
            return null;
        }
    }

    private boolean identify(byte[] bytes, CharsetDecoder decoder) {
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
        } catch (CharacterCodingException e) {
            return false;
        }
        return true;
    }
}
