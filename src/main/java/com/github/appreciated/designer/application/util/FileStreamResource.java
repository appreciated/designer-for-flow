package com.github.appreciated.designer.application.util;

import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

// ./images/web_hi_res_512.png
public class FileStreamResource extends StreamResource {
    private final File file;

    public FileStreamResource(File file) {
        super(file.getName(), () -> {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        });

        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
