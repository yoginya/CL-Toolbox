package gui;

import java.awt.MouseInfo;
import java.text.ParseException;

import common.tag.Tag;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

class ParsingTraceTableFx {

  private final String[][] rowData;
  private final Tag tag;

  private final TableView<ParsingStep> table = new TableView<ParsingStep>();

  private String[] columnNames =
    new String[] {"Id", "Item", "Rules", "Backpointers"};
  private final Timeline showTimer;
  private final Timeline disposeTimer;
  private TableRow<?> popupRow;
  private TableColumn<ParsingStep, String> popupColumn;
  private DisplayTreeFx popup;

  public ParsingTraceTableFx(String[][] rowData, String[] columnNames,
    Tag tag) {
    this.rowData = rowData;
    this.columnNames = columnNames;
    this.tag = tag;
    if (tag == null) {
      showTimer = null;
      disposeTimer = null;
    } else {
      showTimer =
        new Timeline(new KeyFrame(Duration.millis(500), ae -> showPopup()));
      showTimer.play();
      showTimer.setAutoReverse(false);
      disposeTimer =
        new Timeline(new KeyFrame(Duration.millis(2500), ae -> disposePopup()));
      showTimer.setAutoReverse(false);
    }
    displayTable();
  }

  private DisplayTreeFx disposePopup() {
    DisplayTreeFx popup = getTreePopup();
    popup.dispose();
    return popup;
  }

  private Object showPopup() {
    if (popupRow != null) {
      disposeTimer.stop();
      DisplayTreeFx popup = getTreePopup();
      if (popup != null) {
        disposeTimer.playFromStart();
      }
    }
    return popup;
  }

  private DisplayTreeFx getTreePopup() {
    if (popup != null) {
      popup.dispose();
      popup = null;
    }
    String value = popupColumn.getCellData(popupRow.getIndex());
    if (value.charAt(0) == '[') {
      String treeName = value.substring(1, value.indexOf(','));
      try {
        popup = new DisplayTreeFx(
          new String[] {tag.getTree(treeName).toString(), value});
        popup.setLocation(
          Math.round(
            MouseInfo.getPointerInfo().getLocation().getX() + table.getWidth()),
          Math.round(MouseInfo.getPointerInfo().getLocation().getY()));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return popup;
  }

  private void displayTable() {
    Stage stage = new Stage();
    final VBox vbox = new VBox();
    Scene scene = new Scene(vbox);
    stage.setTitle("Parsing Trace Table");
    stage.setWidth(850);
    stage.setHeight(750);

    for (String columnName : columnNames) {
      TableColumn<ParsingStep, String> col =
        new TableColumn<ParsingStep, String>(columnName);
      col.setCellValueFactory(
        new PropertyValueFactory<ParsingStep, String>(columnName));
      col.setSortable(false);
      if (tag != null) {
        col.setCellFactory(tc -> new HoverCell(this));
      }
      col.setMinWidth(200);
      table.getColumns().add(col);
    }
    ObservableList<ParsingStep> data = FXCollections.observableArrayList();
    for (String[] date : rowData) {
      data.add(new ParsingStep(date));
    }
    table.setItems(data);
    table.setPrefHeight(stage.getHeight() - 40);
    table.setPrefWidth(stage.getWidth() - 40);

    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 0, 0, 10));
    vbox.getChildren().addAll(table);

    VBox.setVgrow(table, Priority.ALWAYS);

    stage.setScene(scene);
    stage.show();
  }

  public static class HoverCell extends TableCell<ParsingStep, String> {

    public HoverCell(ParsingTraceTableFx pttf) {
      setOnMouseMoved(e -> showPopup(pttf));
    }

    void showPopup(ParsingTraceTableFx pttf) {
      TableRow<?> row = this.getTableRow();
      TableColumn<ParsingStep, String> col = this.getTableColumn();
      if ((row.getIndex() > -1) // && row < table.getRowCount()
        && (this.getIndex() > -1) // && col < table.getColumnCount()
        && (pttf.getPopupRow() == null || pttf.getPopupCol() != col
          || pttf.getPopupRow() != row)) {
        pttf.setPopupRow(row);
        pttf.setPopupCol(col);
        pttf.getRestartShowTimer();
      }
    }

    @Override protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);
      setText(empty ? null : item);
    }
  }

  private TableRow<?> getPopupRow() {
    return this.popupRow;
  }

  private TableColumn<ParsingStep, String> getPopupCol() {
    return this.popupColumn;
  }

  private void getRestartShowTimer() {
    this.showTimer.stop();
    this.showTimer.playFromStart();
  }

  private void setPopupCol(TableColumn<ParsingStep, String> col) {
    this.popupColumn = col;
  }

  private void setPopupRow(TableRow<?> row) {
    this.popupRow = row;
  }

}
