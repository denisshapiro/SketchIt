package denis.SketchIt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class FXMLController {
    public Menu editMenu;
    Stage stage = null;
    public ColorPicker lineColorPicker;
    public ColorPicker shapeColorPicker;
    public Pane canvas;
    public VBox toolBox;
    private denis.SketchIt.Model model;
    denis.SketchIt.Model.BUTTONS currHover = null;
    Image tapSelected = new Image("tapSelected.png");
    Image tapUnselected = new Image("tapUnselected.png");
    Image rectangleSelected = new Image("rectangleSelected.png");
    Image rectangleUnselected = new Image("rectangleUnselected.png");
    Image circleSelected = new Image("circleSelected.png");
    Image circleUnselected = new Image("circleUnselected.png");
    Image lineSelected = new Image("lineSelected.png");
    Image lineUnselected = new Image("lineUnselected.png");
    Image eraserSelected = new Image("eraserSelected.png");
    Image eraserUnselected = new Image("eraserUnselected.png");
    Image fillSelected = new Image("paintSelected.png");
    Image fillUnselected = new Image("paintUnselected.png");

    Image lineSolid = new Image("solidLine.png", 80, 20, true, true, true);
    Image lineDashed = new Image("dashedLine.png", 80, 22, true, true, true);
    Image lineDotted = new Image("dottedLine.png",80, 20, true, true, true);

    Image lineThin = new Image("solidLine.png", 80, 3, false, true, true);
    Image lineNormal = new Image("solidLine.png", 80, 20, true, true, true);
    Image lineThick = new Image("solidLine.png", 80, 10, false, true, true);

    Dialog<String> dialog = null;

    public ImageView tap;
    public ImageView rect;
    public ImageView circle;
    public ImageView line;
    public ImageView eraser;
    public ImageView fill;
    public ImageView currImgView = null;

    @FXML
    public RadioButton solidButton;
    public RadioButton dashedButton;
    public RadioButton dottedButton;
    final ToggleGroup style = new ToggleGroup();

    public RadioButton thinButton;
    public RadioButton normalButton;
    public RadioButton thickButton;
    final ToggleGroup width = new ToggleGroup();

    public void disableStyleSelection(Boolean b){
        style.getToggles().forEach(toggle -> {
            Node node = (Node) toggle ;
            node.setDisable(b);
        });
    }

    public void disableWidthSelection(Boolean b){
        width.getToggles().forEach(toggle -> {
            Node node = (Node) toggle ;
            node.setDisable(b);
        });
    }


    public void deselect(ImageView view){
        if(view == tap) tap.setImage(tapUnselected);
        else if(view == rect) rect.setImage(rectangleUnselected);
        else if(view == circle) circle.setImage(circleUnselected);
        else if(view == line) line.setImage(lineUnselected);
        else if(view == eraser) eraser.setImage(eraserUnselected);
        else if(view == fill) fill.setImage(fillUnselected);
    }

    public void select(ImageView view){
        if(view == tap) tap.setImage(tapSelected);
        else if(view == rect) rect.setImage(rectangleSelected);
        else if(view == circle) circle.setImage(circleSelected);
        else if(view == line) line.setImage(lineSelected);
        else if(view == eraser) eraser.setImage(eraserSelected);
        else if(view == fill) fill.setImage(fillSelected);
    }

    @FXML

    public void initialize() {
        solidButton.setToggleGroup(style);
        solidButton.setSelected(true);
        dashedButton.setToggleGroup(style);
        dottedButton.setToggleGroup(style);
        solidButton.getStyleClass().remove("radio-button");
        solidButton.getStyleClass().add("toggle-button");

        solidButton.setGraphic(new ImageView(lineSolid));
        dashedButton.getStyleClass().remove("radio-button");
        dashedButton.getStyleClass().add("toggle-button");
        dashedButton.setGraphic(new ImageView(lineDashed));
        dottedButton.getStyleClass().remove("radio-button");
        dottedButton.getStyleClass().add("toggle-button");
        dottedButton.setGraphic(new ImageView(lineDotted));

        thinButton.setToggleGroup(width);
        normalButton.setSelected(true);
        normalButton.setToggleGroup(width);
        thickButton.setToggleGroup(width);
        thinButton.getStyleClass().remove("radio-button");
        thinButton.getStyleClass().add("toggle-button");
        normalButton.getStyleClass().remove("radio-button");
        normalButton.getStyleClass().add("toggle-button");
        thickButton.getStyleClass().remove("radio-button");
        thickButton.getStyleClass().add("toggle-button");
        thinButton.setGraphic(new ImageView(lineThin));
        normalButton.setGraphic(new ImageView(lineNormal));
        thickButton.setGraphic(new ImageView(lineThick));

        lineColorPicker.setValue(Color.BLACK);
        shapeColorPicker.setValue(Color.RED);

        disableStyleSelection(true);
        disableWidthSelection(true);
        lineColorPicker.setDisable(true);
        shapeColorPicker.setDisable(true);
        editMenu.setDisable(true);

        dialog = new Dialog<String>();
        dialog.setTitle("SketchIt");
        dialog.setContentText("SketchIt\nDenis Shapiro\n20784758");
        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(close);

        Rectangle clip = new Rectangle();
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        canvas.setClip(clip);
        canvas.layoutBoundsProperty().addListener((a, b, newValue) -> {
            clip.setWidth(newValue.getWidth());
            clip.setHeight(newValue.getHeight());
        });
    }

    public void setModel(denis.SketchIt.Model model) {
        this.model = model;
    }

    @FXML

    public void mousePressed(MouseEvent mouseEvent) {
        deselect(currImgView);
        switch(mouseEvent.getPickResult().getIntersectedNode().getId()){
            case "tap":
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.SELECT){
                    model.setSelect();
                    currImgView = tap;
                }
                break;
            case "rect":
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.RECTANGLE){
                    model.setRectangle();
                    currImgView = rect;
                }
                break;
            case "circle":
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.CIRCLE){
                    model.setCircle();
                    currImgView = circle;
                }
                break;
            case "line":
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.LINE){
                    model.setLine();
                    currImgView = line;
                }
                break;
            case "eraser":
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.ERASER){
                    model.setEraser();
                    currImgView = eraser;
                }
                break;
            case "fill":
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.FILL){
                    model.setFill();
                    currImgView = fill;
                }
                break;
        }
        currImgView.setStyle("");
        select(currImgView);
    }

    public void mouseEntered(MouseEvent mouseEvent) {
        String shadow = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);";
        switch(mouseEvent.getPickResult().getIntersectedNode().getId()){
            case "tap":
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.SELECT) tap.setStyle(shadow); currHover = denis.SketchIt.Model.BUTTONS.SELECT;
                break;
            case "rect":
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.RECTANGLE) rect.setStyle(shadow); currHover = denis.SketchIt.Model.BUTTONS.RECTANGLE;
                break;
            case "circle":
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.CIRCLE) circle.setStyle(shadow); currHover = denis.SketchIt.Model.BUTTONS.CIRCLE;
                break;
            case "line":
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.LINE) line.setStyle(shadow); currHover = denis.SketchIt.Model.BUTTONS.LINE;
                break;
            case "eraser":
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.ERASER) eraser.setStyle(shadow); currHover = denis.SketchIt.Model.BUTTONS.ERASER;
                break;
            case "fill":
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.FILL) fill.setStyle(shadow); currHover = denis.SketchIt.Model.BUTTONS.FILL;
                break;
        }
    }

    public void mouseExited(MouseEvent mouseEvent) {
        switch(currHover){
            case SELECT:
                tap.setStyle("");
                break;
            case RECTANGLE:
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.RECTANGLE) rect.setStyle("");
                break;
            case CIRCLE:
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.CIRCLE) circle.setStyle("");
                break;
            case LINE:
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.LINE) line.setStyle("");
                break;
            case ERASER:
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.ERASER) eraser.setStyle("");
                break;
            case FILL:
                if(model.currBtn != denis.SketchIt.Model.BUTTONS.FILL) fill.setStyle("");
                break;
        }
        currHover = null;
    }

    public void update(){
        switch(model.currBtn){
            case SELECT:
                if(model.typeSelected == denis.SketchIt.Model.SHAPES.LINE){
                    lineColorPicker.setDisable(false);
                    shapeColorPicker.setDisable(true);
                    disableStyleSelection(false);
                    disableWidthSelection(false);
                    editMenu.setDisable(false);
                }
                else if(model.typeSelected == denis.SketchIt.Model.SHAPES.RECTANGLE || model.typeSelected == denis.SketchIt.Model.SHAPES.CIRCLE){
                    lineColorPicker.setDisable(false);
                    shapeColorPicker.setDisable(false);
                    disableStyleSelection(false);
                    disableWidthSelection(false);
                    editMenu.setDisable(false);
                    editMenu.setDisable(false);
                }
                else{
                    lineColorPicker.setDisable(true);
                    shapeColorPicker.setDisable(true);
                    disableStyleSelection(true);
                    disableWidthSelection(true);
                    editMenu.setDisable(false);
                }
                break;
            case CIRCLE:
            case RECTANGLE:
                lineColorPicker.setDisable(false);
                shapeColorPicker.setDisable(false);
                disableStyleSelection(false);
                disableWidthSelection(false);
                editMenu.setDisable(false);
                break;
            case LINE:
                lineColorPicker.setDisable(false);
                shapeColorPicker.setDisable(true);
                disableStyleSelection(false);
                disableWidthSelection(false);
                editMenu.setDisable(false);
                break;
            case ERASER:
                lineColorPicker.setDisable(true);
                shapeColorPicker.setDisable(true);
                disableStyleSelection(true);
                disableWidthSelection(true);
                editMenu.setDisable(false);
                break;
            case FILL:
                lineColorPicker.setDisable(true);
                shapeColorPicker.setDisable(false);
                disableStyleSelection(true);
                disableWidthSelection(true);
                editMenu.setDisable(false);
                break;
        }
    }

    public void canvasClick(MouseEvent mouseEvent) {
        if(model.currBtn == null) return;
        switch(model.currBtn){
            case LINE:
                model.addLine(mouseEvent);
                model.saved = false;
                break;
            case RECTANGLE:
                model.addRectangle(mouseEvent);
                model.saved = false;
                break;
            case CIRCLE:
                model.addCircle(mouseEvent);
                model.saved = false;
                break;
            case ERASER:
                model.erase(mouseEvent);
                model.saved = false;
                break;
            case FILL:
                model.fill(mouseEvent);
                model.saved = false;
                break;
            case SELECT:
                model.select(mouseEvent);
                break;
        }
    }

    public void canvasMove(MouseEvent mouseEvent) {
        if(model.currDrawing == null && model.currSelected == null) return;
        switch(model.currBtn){
            case LINE:
                model.drawLine(mouseEvent);
                model.saved = false;
                break;
            case RECTANGLE:
                model.drawRectangle(mouseEvent);
                model.saved = false;
                break;
            case CIRCLE:
                model.drawCircle(mouseEvent);
                model.saved = false;
                break;
            case SELECT:
                model.dragSelection(mouseEvent);
                break;
        }
    }

    public void canvasRelease(MouseEvent mouseEvent) {
        model.release();
    }

    public void setSolidEvt(MouseEvent mouseEvent) {
        if(model.currSelected != null){
            model.currSelected.getStyleClass().remove("dashedStyle");
            model.currSelected.getStyleClass().remove("dottedStyle");
            model.currSelected.getStyleClass().add("solidStyle");
        }
    }

    public void setDashedEvt(MouseEvent mouseEvent) {
        if(model.currSelected != null){
            model.currSelected.getStyleClass().add("dashedStyle");
            model.currSelected.getStyleClass().remove("dottedStyle");
            model.currSelected.getStyleClass().remove("solidStyle");
        }
    }

    public void setDottedEvt(MouseEvent mouseEvent) {
        if(model.currSelected != null){
            model.currSelected.getStyleClass().remove("dashedStyle");
            model.currSelected.getStyleClass().add("dottedStyle");
            model.currSelected.getStyleClass().remove("solidStyle");
        }
    }

    public void setThick(MouseEvent mouseEvent) {
        if(model.currSelected != null) model.currSelected.setStrokeWidth(7.0); model.saved = false;
    }

    public void setNormal(MouseEvent mouseEvent) {
        if(model.currSelected != null) model.currSelected.setStrokeWidth(3.0); model.saved = false;
    }

    public void setThin(MouseEvent mouseEvent) {
        if(model.currSelected != null) model.currSelected.setStrokeWidth(1.0); model.saved = false;
    }

    public void setLineColEvt() {
        if(model.currSelected != null) model.currSelected.setStroke(lineColorPicker.getValue()); model.saved = false;
    }

    public void setShapeColEvt() {
        if(model.currSelected != null) model.currSelected.setFill(shapeColorPicker.getValue()); model.saved = false;
    }

    public void launchAbout(ActionEvent actionEvent) {
            dialog.showAndWait();
    }

    public void saveEvt() {
        model.save();
    }

    public void saveAlert(){
        if(model.saved == false){
            Alert alert = new Alert(Alert.AlertType.NONE, "Save your work?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Save");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.YES) model.save();
        }
    }

    public void loadEvt() {
        saveAlert();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String fileName = file.getName();
            model.load(fileName);
        }
    }

    public void addStage(Stage stage) {
        this.stage = stage;
    }

    public void newEvt() {
        saveAlert();
        model.newDrawing();
    }

    public void quitEvt(ActionEvent actionEvent) {
        saveAlert();
        System.exit(0);
    }

    public void copyEvt(ActionEvent actionEvent) {
        model.copyShape(model.currSelected);
    }

    public void pasteEvt(ActionEvent actionEvent) {
        model.pasteCopy();
    }

    public void cutEvt(ActionEvent actionEvent) {
        if(model.currSelected != null){
            model.copyShape(model.currSelected);
            model.eraseSelected();
        }
    }
}
