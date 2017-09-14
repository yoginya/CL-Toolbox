package gui;

import javafx.beans.property.SimpleStringProperty;

public class ParsingStep {
  private final SimpleStringProperty id;
  private final SimpleStringProperty item;
  private final SimpleStringProperty rules;
  private final SimpleStringProperty backpointers;

  ParsingStep(String id, String item, String rules,
    String backpointers) {
    this.id = new SimpleStringProperty(id);
    this.item = new SimpleStringProperty(item);
    this.rules = new SimpleStringProperty(rules);
    this.backpointers = new SimpleStringProperty(backpointers);
  }

  public ParsingStep(String[] date) {
    this.id = new SimpleStringProperty(date[0]);
    this.item = new SimpleStringProperty(date[1]);
    this.rules = new SimpleStringProperty(date[2]);
    this.backpointers = new SimpleStringProperty(date[3]);
  }

  public String getId() {
    return id.get();
  }

  public String getItem() {
    return item.get();
  }

  public String getRules() {
    return rules.get();
  }

  public String getBackpointers() {
    return backpointers.get();
  }
}
