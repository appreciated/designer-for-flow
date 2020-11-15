package com.github.appreciated.designer.application.model;

import javax.persistence.*;
import java.io.File;

@Entity
@Table(name = "PROJECT")
public class ProjectPath {

    @Id
    @GeneratedValue
    Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "path")
    String path;

    public ProjectPath() {
    }

    public ProjectPath(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public ProjectPath(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            name = file.getName();
            this.path = path;
        } else {
            throw new IllegalStateException("");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
