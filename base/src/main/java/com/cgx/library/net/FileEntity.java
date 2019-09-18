package com.cgx.library.net;

import java.io.File;

public class FileEntity {
    private String name;
    private File file;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
