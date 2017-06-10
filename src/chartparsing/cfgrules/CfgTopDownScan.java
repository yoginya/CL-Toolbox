package chartparsing.cfgrules;

import java.util.List;

import chartparsing.AbstractDynamicDeductionRule;
import common.ArrayUtils;
import common.Item;
import common.cfg.CfgItem;

/** The scan rule for topdown removes a terminal if it is the next input
 * symbol. */
public class CfgTopDownScan extends AbstractDynamicDeductionRule {

  private final String[] wsplit;

  public CfgTopDownScan(String[] wsplit) {
    this.wsplit = wsplit;
    this.name = "scan";
    this.antNeeded = 1;
  }

  @Override public List<Item> getConsequences() {
    if (antecedences.size() == antNeeded) {
      String[] itemForm = antecedences.get(0).getItemform();
      String stack = itemForm[0];
      String[] stackSplit = stack.split(" ");
      int i = Integer.parseInt(itemForm[1]);
      if (i < wsplit.length && stackSplit[0].equals(wsplit[i])) {
        consequences.add(new CfgItem(
          ArrayUtils.getSubSequenceAsString(stackSplit, 1, stackSplit.length),
          i + 1));
      }
    }
    return consequences;
  }

  @Override public String toString() {
    return "[aα,i]" + "\n______ w_i = a\n" + "[α,i+1]";
  }

}