package com.github.appreciated.designer.application.dialog.file.preconditions;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JavaFile implements HasPreconditions {
    public static String SOURCE_FILE = "source-file";
    public static String PACKAGE = "package";

    String[] preconditionNames = new String[]{"name", "class", SOURCE_FILE, PACKAGE};
    private Map<String, Object> preconditions;

    public JavaFile() {
    }

    @Override
    public Map<String, Object> getPreconditions() {
        return preconditions;
    }

    @Override
    public void setPreconditions(Map<String, Object> preconditions) {

        this.preconditions = preconditions;
    }

    public File create() {
        if (preconditions != null) {
            for (Map.Entry<String, Class> entry : getPreconditionNames().entrySet()) {
                String s = entry.getKey();
                Class aClass = entry.getValue();
                if (preconditions.get(s).getClass() != aClass) {
                    throw new IllegalArgumentException("this must not happen! " + preconditions.get(s).getClass().getSimpleName() + "!=" + aClass.getSimpleName());
                }
            }

            File sourceRootFile = (File) preconditions.get(SOURCE_FILE);
            File sourceFile = new File(sourceRootFile.getPath() + File.separator + ((String) preconditions.get(PACKAGE)).replace(".", File.separator) + File.separator + preconditions.get(preconditionNames[0]) + ".java");
            if (!sourceFile.exists()) {
                try {
                    sourceFile.createNewFile();
                    SourceRoot sourceRoot = new SourceRoot(sourceRootFile.toPath());
                    CompilationUnit compilationUnit = new CompilationUnit();
                    compilationUnit.setStorage(sourceFile.toPath());
                    compilationUnit.setPackageDeclaration((String) preconditions.get(preconditionNames[3]));
                    ClassOrInterfaceDeclaration componentClass = compilationUnit.addClass((String) preconditions.get(preconditionNames[0]));
                    componentClass.addExtendedType((Class<?>) preconditions.get(preconditionNames[1]));
                    sourceRoot.add(compilationUnit);
                    sourceRoot.saveAll(sourceRootFile.toPath());
                    return sourceFile;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new IllegalArgumentException("this must not happen!");
    }

    @Override
    public Map<String, Class> getPreconditionNames() {
        HashMap<String, Class> preconditions = new HashMap<>();
        preconditions.put(preconditionNames[0], String.class);
        preconditions.put(preconditionNames[1], Class.class);
        preconditions.put(preconditionNames[2], File.class);
        preconditions.put(preconditionNames[3], String.class);
        return preconditions;
    }
}
