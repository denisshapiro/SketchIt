package denis.SketchIt;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.w3c.dom.css.Rect;

import java.io.*;

public class Model {
    Boolean saved = true;
    final String DELIMITER = ",";
    final String ENDL = "\n";
    enum BUTTONS { SELECT, RECTANGLE, CIRCLE, LINE, ERASER, FILL};
    enum SHAPES {LINE, RECTANGLE, CIRCLE};
    public BUTTONS currBtn = null;
    public Shape currDrawing = null;
    public Shape currSelected = null;

    public SHAPES typeSelected = null;
    public Group canvasShapes;
    denis.SketchIt.FXMLController controller;

    public Model(){
        canvasShapes = new Group();
    }

    public void addController(denis.SketchIt.FXMLController controller){
        this.controller = controller;
        controller.canvas.getChildren().add(canvasShapes);
    }

    private void notifyController(){
        controller.update();
    }

    public void setSelect() {
        currBtn = BUTTONS.SELECT;
        notifyController();
    }

    public void setRectangle() {
        removeSelection();
        currBtn = BUTTONS.RECTANGLE;
        notifyController();
    }
    public void setCircle() {
        removeSelection();
        currBtn = BUTTONS.CIRCLE;
        notifyController();
    }
    public void setLine() {
        removeSelection();
        currBtn = BUTTONS.LINE;
        notifyController();
    }

    public void setEraser() {
        removeSelection();
        currBtn = BUTTONS.ERASER;
        notifyController();
    }

    public void setFill() {
        removeSelection();
        currBtn = BUTTONS.FILL;
        notifyController();
    }

    public void addLine(MouseEvent mouseEvent){
        if(controller.canvas.contains(mouseEvent.getX(), mouseEvent.getY())) {
            Line line = new Line(0, 0, 0, 0);
            currDrawing = line;
            line.setStroke(controller.lineColorPicker.getValue());
            line.setStartX(mouseEvent.getX());
            line.setStartY(mouseEvent.getY());
            line.setEndX(mouseEvent.getX());
            line.setEndY(mouseEvent.getY());
            setShapeStyle(line);
            setShapeWidth(line);
            canvasShapes.getChildren().add(line);
        }
    }

    public void addRectangle(MouseEvent mouseEvent) {
        if(controller.canvas.contains(mouseEvent.getX(), mouseEvent.getY())) {
            Rectangle rect = new Rectangle(0, 0, 0, 0);
            currDrawing = rect;
            rect.setStroke(controller.lineColorPicker.getValue());
            rect.setFill(controller.shapeColorPicker.getValue());
            rect.setTranslateX(mouseEvent.getX());
            rect.setTranslateY(mouseEvent.getY());
            setShapeStyle(rect);
            setShapeWidth(rect);
            canvasShapes.getChildren().add(rect);
        }
    }

    public void addCircle(MouseEvent mouseEvent) {
        if(controller.canvas.contains(mouseEvent.getX(), mouseEvent.getY())) {
            Circle circle = new Circle(0, 0, 0);
            currDrawing = circle;
            circle.setStroke(controller.lineColorPicker.getValue());
            circle.setFill(controller.shapeColorPicker.getValue());
            circle.setCenterX(mouseEvent.getX());
            circle.setCenterY(mouseEvent.getY());
            setShapeStyle(circle);
            setShapeWidth(circle);
            canvasShapes.getChildren().add(circle);
        }
    }

    public void drawLine(MouseEvent mouseEvent){
        Line line = (Line)currDrawing;
        line.setEndX(mouseEvent.getX());
        line.setEndY(mouseEvent.getY());
    }

    public void drawRectangle(MouseEvent mouseEvent) {
        Rectangle rect = (Rectangle) currDrawing;
        rect.setWidth(Math.abs(mouseEvent.getX() - rect.getTranslateX()));
        rect.setHeight(Math.abs(mouseEvent.getY() - rect.getTranslateY()));
    }

    public void drawCircle(MouseEvent mouseEvent) {
        Circle circle = (Circle) currDrawing;
        double newRadius = Math.sqrt(Math.pow((mouseEvent.getX()-circle.getCenterX()),2.0) + Math.pow((mouseEvent.getY()-circle.getCenterY()),2.0));
        circle.setRadius(newRadius);
    }

    public void release(){
        currDrawing = null;
    }

    public void setShapeStyle(Shape shape){
        if(controller.solidButton.isSelected()){
            shape.getStyleClass().add("solidStyle");
        }
        else if(controller.dashedButton.isSelected()){
            shape.getStyleClass().add("dashedStyle");
        }
        else if(controller.dottedButton.isSelected()){
            shape.getStyleClass().add("dottedStyle");
        }
    }

    public void setShapeWidth(Shape shape){
        if(controller.thinButton.isSelected()){
            shape.setStrokeWidth(1.0);
        }
        else if(controller.normalButton.isSelected()){
            shape.setStrokeWidth(3.0);
        }
        else if(controller.thickButton.isSelected()){
            shape.setStrokeWidth(7.0);
        }
    }

    public void erase(MouseEvent mouseEvent) {
        for(int i = 0; i < canvasShapes.getChildren().size(); ++i){
            if(mouseEvent.getTarget() == canvasShapes.getChildren().get(i)){
                canvasShapes.getChildren().remove(i);
                saved = false;
                break;
            }
        }
        notifyController();
    }

    public void eraseSelected(){
        for(int i = 0; i < canvasShapes.getChildren().size(); ++i){
            if(currSelected== canvasShapes.getChildren().get(i)){
                canvasShapes.getChildren().remove(i);
                removeSelection();
                saved = false;
                break;
            }
        }
    }

    public void fill(MouseEvent mouseEvent) {
        for(Node node: canvasShapes.getChildren()){
            if(mouseEvent.getTarget() == node){
                Shape shape = (Shape)node;
                shape.setFill(controller.shapeColorPicker.getValue());
                saved = false;
                break;
            }
        }
    }

    public void setOutline(Shape shape){
        shape.getStyleClass().add("selected");
    }

    public void removeSelection(){
        if(currSelected != null){
            currSelected.getStyleClass().remove("selected");;
            currSelected = null;
            typeSelected = null;
            delta = null;
            notifyController();
        }
    }

    public void setWidthButton(){
        if(currSelected.getStrokeWidth() == 1.0) controller.thinButton.setSelected(true);
        else if(currSelected.getStrokeWidth() == 3.0) controller.normalButton.setSelected(true);
        else if(currSelected.getStrokeWidth() == 7.0) controller.thickButton.setSelected(true);
    }

    public void setShapeStyleButton(){
        for(String style: currSelected.getStyleClass()){
            if(style.equals("solidStyle")) controller.solidButton.setSelected(true);
            else if(style.equals("dashedStyle")) controller.dashedButton.setSelected(true);
            else if(style.equals("dottedStyle")) controller.dottedButton.setSelected(true);
        }
    }

    public void select(MouseEvent mouseEvent) {
        removeSelection();
        if(mouseEvent.getTarget() instanceof Rectangle){
            typeSelected = SHAPES.RECTANGLE;
            currSelected = (Shape)mouseEvent.getTarget();
            controller.lineColorPicker.setValue((Color)currSelected.getStroke());
            controller.shapeColorPicker.setValue((Color)currSelected.getFill());
            setWidthButton();
            setShapeStyleButton();
            setOutline(currSelected);
        }
        else if(mouseEvent.getTarget() instanceof Circle){
            typeSelected = SHAPES.CIRCLE;
            currSelected = (Shape)mouseEvent.getTarget();
            controller.lineColorPicker.setValue((Color)currSelected.getStroke());
            controller.shapeColorPicker.setValue((Color)currSelected.getFill());
            setWidthButton();
            setShapeStyleButton();
            setOutline(currSelected);
        }
        else if(mouseEvent.getTarget() instanceof Line){
            typeSelected = SHAPES.LINE;
            currSelected = (Shape)mouseEvent.getTarget();
            controller.lineColorPicker.setValue((Color)currSelected.getStroke());
            setWidthButton();
            setShapeStyleButton();
            setOutline(currSelected);
        }
        else if(mouseEvent.getTarget() == controller.canvas){
            removeSelection();
        }
        notifyController();
    }
    Point2D delta = null;
    public void dragSelection(MouseEvent mouseEvent){
        if(currSelected != null){
            if(delta == null) delta = new Point2D(currSelected.getLayoutX()-mouseEvent.getSceneX(), currSelected.getLayoutY()-mouseEvent.getSceneY());
            currSelected.setLayoutX(mouseEvent.getSceneX()+ delta.getX());
            currSelected.setLayoutY(mouseEvent.getSceneY()+ delta.getY());
            saved = false;
        }
    }

    String prevFile = null;
    public void save() {
        FileWriter file = null;
        BufferedWriter writer = null;

        try {
            if(prevFile != null){
                File prev = new File(prevFile);
                prev.delete();
            }
            prevFile = "save" + System.currentTimeMillis() + ".txt";
            file = new FileWriter(prevFile);
            writer = new BufferedWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            int row = 0;
            for(Node shape: canvasShapes.getChildren()){
                if(shape instanceof Line){
                    Line line = (Line)shape;
                    writer.write(
                    ++row + DELIMITER +
                            "LINE" + DELIMITER +
                           line.getStroke() + DELIMITER +
                            line.getStrokeWidth() + DELIMITER +
                            line.getStartX() + DELIMITER +
                            line.getStartY() + DELIMITER +
                            line.getEndX() + DELIMITER +
                            line.getEndY() + DELIMITER +
                            line.getLayoutX() + DELIMITER +
                            line.getLayoutY() + DELIMITER +
                            line.getStyleClass() + ENDL
                    );
                }
                else if(shape instanceof Rectangle){
                    Rectangle rect = (Rectangle)shape;
                    writer.write(
                        ++row + DELIMITER +
                            "RECTANGLE" + DELIMITER +
                            rect.getStroke() + DELIMITER +
                            rect.getFill() + DELIMITER +
                            rect.getStrokeWidth() + DELIMITER +
                            rect.getTranslateX() + DELIMITER +
                            rect.getTranslateY() + DELIMITER +
                            rect.getWidth() + DELIMITER +
                            rect.getHeight() + DELIMITER +
                            rect.getLayoutX() + DELIMITER +
                            rect.getLayoutY() + DELIMITER +
                            rect.getStyleClass() + ENDL
                    );
                }
                else if(shape instanceof Circle){
                    Circle circle = (Circle) shape;
                    writer.write(
                        ++row + DELIMITER +
                            "CIRCLE" + DELIMITER +
                            circle.getStroke() + DELIMITER +
                            circle.getFill() + DELIMITER +
                            circle.getStrokeWidth() + DELIMITER +
                            circle.getCenterX() + DELIMITER +
                            circle.getCenterY() + DELIMITER +
                            circle.getRadius() + DELIMITER +
                            circle.getLayoutX() + DELIMITER +
                            circle.getLayoutY() + DELIMITER +
                            circle.getStyleClass() + ENDL
                    );
                }
            }
            saved = true;
            writer.close();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String fileName) {
        FileReader file = null;
        BufferedReader reader = null;
        String[] values;
        try {
            file = new FileReader(fileName);
            reader = new BufferedReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            canvasShapes.getChildren().clear();
            String line;
            while ((line = reader.readLine()) != null) {
                values = line.split(DELIMITER);
                if(values[1].equals("LINE")){
                    loadSavedLine(values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10]);
                }
                else if(values[1].equals("RECTANGLE")){
                    loadSavedRectangle(values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11]);
                }
                else if(values[1].equals("CIRCLE")){
                    loadSavedCircle(values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSavedLine(String stroke, String width, String startX, String startY, String endX, String endY, String layX, String layY, String style){
        Line line = new Line();
        line.setStroke(Paint.valueOf(stroke));
        line.setStrokeWidth(Double.parseDouble(width));
        line.setStartX(Double.parseDouble(startX));
        line.setStartY(Double.parseDouble(startY));
        line.setEndX(Double.parseDouble(endX));
        line.setEndY(Double.parseDouble(endY));
        line.setLayoutX(Double.parseDouble(layX));
        line.setLayoutY(Double.parseDouble(layY));
        for(String s: style.split(" ")) if(!s.equals("selected")) line.getStyleClass().add(s);
        canvasShapes.getChildren().add(line);
    }

    private void loadSavedRectangle(String stroke, String fill, String strokeWidth, String X, String Y, String width, String height, String layX, String layY, String style){
        Rectangle rect = new Rectangle();
        rect.setStroke(Paint.valueOf(stroke));
        rect.setFill(Paint.valueOf(fill));
        rect.setStrokeWidth(Double.parseDouble(strokeWidth));
        rect.setTranslateX(Double.parseDouble(X));
        rect.setTranslateY(Double.parseDouble(Y));
        rect.setWidth(Double.parseDouble(width));
        rect.setHeight(Double.parseDouble(height));
        rect.setLayoutX(Double.parseDouble(layX));
        rect.setLayoutY(Double.parseDouble(layY));
        for(String s: style.split(" ")) if(!s.equals("selected")) rect.getStyleClass().add(s);
        canvasShapes.getChildren().add(rect);
    }

    private void loadSavedCircle(String stroke, String fill, String strokeWidth, String X, String Y, String radius,  String layX, String layY, String style){
        Circle circle = new Circle();
        circle.setStroke(Paint.valueOf(stroke));
        circle.setFill(Paint.valueOf(fill));
        circle.setStrokeWidth(Double.parseDouble(strokeWidth));
        circle.setCenterX(Double.parseDouble(X));
        circle.setCenterY(Double.parseDouble(Y));
        circle.setRadius(Double.parseDouble(radius));
        circle.setLayoutX(Double.parseDouble(layX));
        circle.setLayoutY(Double.parseDouble(layY));
        for(String s: style.split(" ")) if(!s.equals("selected")) circle.getStyleClass().add(s);
        canvasShapes.getChildren().add(circle);
    }

    public void newDrawing(){
        canvasShapes.getChildren().clear();
    }

    Shape copied = null;
    public void copyShape(Shape shape){
        if(shape != null){
            if(shape instanceof Line){
                Line line = new Line();
                line.setStrokeWidth(shape.getStrokeWidth());
                line.setStroke(shape.getStroke());
                line.setStartX(((Line)shape).getStartX() + 10);
                line.setStartY(((Line)shape).getStartY() + 10);
                line.setEndX(((Line)shape).getEndX() + 10);
                line.setEndY(((Line)shape).getEndY() + 10);
                for(String style: shape.getStyleClass()) {
                    if(style.equals("selected")) continue;
                    line.getStyleClass().add(style);
                }
                copied = line;
            }
            else if(shape instanceof Rectangle){
                Rectangle rect = new Rectangle();
                rect.setStrokeWidth(shape.getStrokeWidth());
                rect.setStroke(shape.getStroke());
                rect.setFill(shape.getFill());
                rect.setTranslateX(shape.getTranslateX()+10);
                rect.setTranslateY(shape.getTranslateY()+10);
                rect.setWidth(((Rectangle)shape).getWidth());
                rect.setHeight(((Rectangle)shape).getHeight());
                rect.setLayoutX(shape.getLayoutX());
                rect.setLayoutY(shape.getLayoutY());
                for(String style: shape.getStyleClass()) {
                    if(style.equals("selected")) continue;
                    rect.getStyleClass().add(style);
                }
                copied = rect;
            }
            else if(shape instanceof Circle){
                Circle circle = new Circle();
                circle.setStrokeWidth(shape.getStrokeWidth());
                circle.setStroke(shape.getStroke());
                circle.setFill(shape.getFill());
                circle.setCenterX(((Circle) shape).getCenterX()+ 10);
                circle.setCenterY(((Circle) shape).getCenterY()+ 10);
                circle.setRadius(((Circle) shape).getRadius());
                circle.setLayoutX(shape.getLayoutX());
                circle.setLayoutY(shape.getLayoutY());
                for(String style: shape.getStyleClass()) {
                    if(style.equals("selected")) continue;
                    circle.getStyleClass().add(style);
                }
                copied = circle;
            }
        }
    }

    public void pasteCopy(){
        canvasShapes.getChildren().add(copied);
        copyShape(copied);
        if(currSelected != null) eraseSelected();
    }

}



