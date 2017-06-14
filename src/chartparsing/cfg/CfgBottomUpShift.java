package chartparsing.cfg;

import java.util.List;

import chartparsing.AbstractDynamicDeductionRule;
import chartparsing.Item;

/** Moves the next input symbol onto the stack */
public class CfgBottomUpShift extends AbstractDynamicDeductionRule {

  private final String[] wsplit;

  public CfgBottomUpShift(String[] wsplit) {
    this.wsplit = wsplit;
    this.name = "shift";
    this.antNeeded = 1;
  }

  @Override public List<Item> getConsequences() {
    if (antecedences.size() == antNeeded) {
      String[] itemForm = antecedences.get(0).getItemform();
      String stack = itemForm[0];
      int i = Integer.parseInt(itemForm[1]);
      if (i < wsplit.length) {
        if (stack.length() == 0) {
          consequences.add(new CfgItem(wsplit[i], i + 1));
        } else {
          consequences.add(new CfgItem(stack + " " + wsplit[i], i + 1));
        }
      }
    }
    return consequences;
  }

  @Override public String toString() {
    return "[Γ,i]" + "\n______ w_i = a\n" + "[Γa,i+1]";
  }

}