package chartparsing.cfgrules;

import java.util.List;

import chartparsing.AbstractDynamicDeductionRule;
import common.ArrayUtils;
import common.Item;
import common.cfg.CfgDottedItem;

/** If in one item a dot is before a nonterminal and the other item is a rule
 * with that nonterminal as lhs and the dot at the end, move the dot over the
 * nonterminal. */
public class CfgEarleyComplete extends AbstractDynamicDeductionRule {

  public CfgEarleyComplete(){
    this.name = "complete";
    this.antneeded = 2;
  }

  @Override public List<Item> getConsequences() {
    if (antecedences.size() == antneeded) {
      String[] itemform1 = antecedences.get(0).getItemform();
      String[] itemform2 = antecedences.get(1).getItemform();
      calculateConsequences(itemform1, itemform2);
      calculateConsequences(itemform2, itemform1);
    }
    return consequences;
  }
  
  private void calculateConsequences(String[] itemform1, String[] itemform2) {
    
    String stack1 = itemform1[0];
    String[] stacksplit1 = stack1.split(" ");
    int i1 = Integer.parseInt(itemform1[1]);
    int j1 = Integer.parseInt(itemform1[2]);
    String stack2 = itemform2[0];
    String[] stacksplit2 = stack2.split(" ");
    int j2 = Integer.parseInt(itemform2[1]);
    int k2 = Integer.parseInt(itemform2[2]);

    if (j1 == j2 && stack2.endsWith("•")) {
      for (int l = 0; l < stacksplit1.length; l++) {
        if (stacksplit1[l].startsWith("•") && stacksplit1[l]
          .substring(1, stacksplit1[l].length()).equals(stacksplit2[0])) {
          String newstack;

          if (l == stacksplit1.length - 1) {
            newstack = ArrayUtils.getSubSequenceAsString(stacksplit1, 0, l)
              + " " + stacksplit2[0] + " •";
          } else {
            newstack =
              ArrayUtils.getSubSequenceAsString(stacksplit1, 0, l) + " "
                + stacksplit2[0] + " •" + ArrayUtils.getSubSequenceAsString(
                  stacksplit1, l + 1, stacksplit1.length);
          }
          consequences.add(new CfgDottedItem(newstack, i1, k2));
          break;
        }
      }
    }
  }

  @Override public String toString() {
    return
        "[A -> α •B β,i,j] [B -> ɣ •,j,k]" + "\n______\n" + "[A -> α B •β,i,k]";
  }

}
