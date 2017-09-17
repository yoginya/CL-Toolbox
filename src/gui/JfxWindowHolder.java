package gui;

import java.text.ParseException;
import java.util.concurrent.FutureTask;

import javax.swing.SwingUtilities;

import common.tag.Tag;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

/** Main point for the JFX Application Thread, because launch() can only run
 * once. Calls the initialization of the other JFX windows. */
public class JfxWindowHolder {
  private JFXPanel jfxPanel = null;
  private final String[] colNames =
    new String[] {"Id", "Item", "Rules", "Backpointers"};
  private String[][] rowData;
  private Tag tag = null;
  private String[] args = null;

  public void setArgs(String[] args) {
    this.args = args;
  }

  public void setTag(Tag tag) {
    this.tag = tag;
  }

  public void showParsingTraceTableFx() throws Exception {
    ensureFXApplicationThreadRunning();
    Platform.runLater(this::_callParsingTraceTableFx);
  }

  public void showDisplayTreeFx() throws Exception {
    ensureFXApplicationThreadRunning();
    Platform.runLater(() -> {
      try {
        _callDisplayTreeFx();
      } catch (ParseException e) {
        e.printStackTrace();
      }
    });
  }

  private void _callDisplayTreeFx() throws ParseException {
    new DisplayTreeFx(args);
  }

  private void _callParsingTraceTableFx() {
    new ParsingTraceTableFx(rowData, colNames, tag);
  }

  private void ensureFXApplicationThreadRunning() throws Exception {
    if (jfxPanel != null)
      return;
    Platform.setImplicitExit(false);
    FutureTask<JFXPanel> fxThreadStarter = new FutureTask<>(JFXPanel::new);
    SwingUtilities.invokeLater(fxThreadStarter);
    jfxPanel = fxThreadStarter.get();
  }

  public void setRowData(String[][] rowData) {
    this.rowData = rowData;
  }
}
