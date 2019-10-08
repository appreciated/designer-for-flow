package com.github.appreciated.designer.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TempFile {

    public static File get(File file) throws IOException {
        File tmp = File.createTempFile("temp", "temp");
        FileUtils.copyFile(file, tmp);
        return tmp;
    }
}
