package medical.app.medicalappfx.dto;

import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * ChartAnnotationNode - это класс, представляющий аннотацию на диаграмме. У каждой аннотации есть координаты (x, y)
 * и соответствующий узел Node, который будет отображаться на графике.
 */
public class ChartAnnotationNode {
    private final Label _node;
    private double _x;
    private double _y;

    public ChartAnnotationNode(final Label node, final double x, final double y) {
        _node = node;
        _x = x;
        _y = y;
    }

    public Label getNode() {
        return _node;
    }

    public double getX() {
        return _x;
    }

    public double getY() {
        return _y;
    }

    public void setX(final double x) {
        _x = x;
    }

    public void setY(final double y) {
        _y = y;
    }
}
