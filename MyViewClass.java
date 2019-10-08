import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

public class MyViewClass extends HorizontalLayout {

    public MyViewClass() {
        component0.setWidth("150px");
        component0.setHeight("20%");
        component0.setText("This is aButton");
        component0.add();
        this.add(component0, component1, component2);
        this.setWidth("600px");
        this.setHeight("100%");
        this.setAlignItems(FlexComponent.Alignment.START);
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    NativeButton component0 = new NativeButton();

    public NativeButton getComponent0() {
        return component0;
    }

    public void setComponent0(NativeButton component0) {
        this.component0 = component0;
    }

    Grid component1 = new Grid();

    public Grid getComponent1() {
        return component1;
    }

    public void setComponent1(Grid component1) {
        this.component1 = component1;
    }

    TreeGrid component2 = new TreeGrid();

    public TreeGrid getComponent2() {
        return component2;
    }

    public void setComponent2(TreeGrid component2) {
        this.component2 = component2;
    }
}
