package com.github.appreciated.designer.template.java.parser;

public class ComponentTreeCompilerTest {
/*
    @Test
    public void testComponentTreeCompiler() throws ParseException, ClassNotFoundException {

        File file = new File(getClass().getClassLoader().getResource("com/designer/test/TestDesign.java").getFile());
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
        ComponentComparer.compare(compiledComponent,component);
    }

    private Component getComponentFromFile(File file) throws ParseException, ClassNotFoundException {
        TestProject project = new TestProject();
        DesignerComponentTreeParser parser = new DesignerComponentTreeParser(file, project);
        DesignCompilerInformation info = parser.getDesignCompilerInformation();
        return info.getComponent();
    }*/

}