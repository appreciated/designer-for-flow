package com.github.appreciated.designer.integrationtest.parser;

import com.github.appreciated.designer.integrationtest.IntegrationTestBase;
import com.vaadin.flow.component.Component;
import org.junit.Test;

import java.io.File;
import java.text.ParseException;

public class ComponentTreeCompilerIntegrationTest extends IntegrationTestBase {

    @Test
    public void testComponentTreeCompiler() throws ParseException, ClassNotFoundException {

        //
        openPath("");


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*File file =
        Component component = getComponentFromFile(file);

        File saveFile = new File(file.getParentFile().getPath() + File.separator + "TestDesign2.java");
        DesignCompilerInformation info = new DesignCompilerInformation();
        info.setDesign(saveFile);
        info.setComponent(component);
        info.setProject(new TestProject());

        DesignerComponentTreeCompiler compiler = new DesignerComponentTreeCompiler(info);
        compiler.save();

        Component compiledComponent = getComponentFromFile(saveFile);
        Assert.assertTrue(compiledComponent instanceof DesignerComponentWrapper);
        ComponentComparer.compare(compiledComponent,component);*/
    }

    private Component getComponentFromFile(File file) throws ParseException, ClassNotFoundException {
        /*
        TestProject project = new TestProject();
        DesignerComponentTreeParser parser = new DesignerComponentTreeParser(file, project);
        DesignCompilerInformation info = parser.getDesignCompilerInformation();
        return info.getComponent();*/
        return null;
    }

    public static void main(String[] args) {

        // System.out.println(f.getPath());
        // System.out.println(f.getPath());
    }


}