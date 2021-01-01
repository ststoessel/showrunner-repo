package org.showrunner.fileutils;

import java.util.Optional;

public class FileUtilsException extends RuntimeException {

    private String additionalInfo = "";
    private transient Optional<Exception> exceptionOptional;

    public FileUtilsException(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }


    public FileUtilsException(String additionalInfo, Exception ex) {
        this.additionalInfo = additionalInfo;
        this.exceptionOptional = Optional.of(ex);
    }

    public String getErrorMessage() {
        return toString();
    }

    public String getDetailedErrorMessage() {
        return toString() + ": " + getStacktraceAsString();
    }

    @Override
    public String toString() {
        String msg = this.getMessage();
        String ret = "";

        if (msg == null) {
            ret = additionalInfo;
        } else {
            ret = msg + ": " + additionalInfo;
        }

        return ret;
    }

    public String getStacktraceAsString() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTraceElements = getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            sb.append(stackTraceElement.toString());
        }
        return sb.toString();
    }

    public Optional<Exception> getExceptionOptional() {
        return exceptionOptional;
    }


}
