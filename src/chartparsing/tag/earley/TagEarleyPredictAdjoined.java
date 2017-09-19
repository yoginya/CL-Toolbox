package chartparsing.tag.earley;

import java.util.List;

import chartparsing.AbstractDynamicDeductionRule;
import chartparsing.DeductionItem;
import chartparsing.Item;
import common.tag.Tag;

/** If a dot is at the foot node of an auxiliary tree, predict that it was
 * adjoined into another tree and move into that tree at the affected node. */
public class TagEarleyPredictAdjoined extends AbstractDynamicDeductionRule {

  private final String outTreeName;
  private final String outNode;
  private final Tag tag;

  /** Constructor takes a tree and a node where maybe is, was, will be adjoined,
   * also needs the grammar to retrieve information about the antecedence. */
  public TagEarleyPredictAdjoined(String outTreeName, String outNode, Tag tag) {
    this.outTreeName = outTreeName;
    this.outNode = outNode;
    this.tag = tag;
    this.name = "predict adjoined in " + outTreeName + "(" + outNode + ")";
    this.antNeeded = 1;
  }

  @Override public List<Item> getConsequences() {
    if (antecedences.size() == antNeeded) {
      String[] itemForm = antecedences.get(0).getItemform();
      String treeName = itemForm[0];
      String node = itemForm[1];
      String l = itemForm[6];
      boolean adjoinable = tag.isAdjoinable(treeName, outTreeName, outNode);
      boolean isFootNode = tag.getAuxiliaryTree(treeName) != null && tag
        .getAuxiliaryTree(treeName).getFoot().getGornAddress().equals(node);
      if (adjoinable && isFootNode && itemForm[2].equals("lb")
        && itemForm[7].equals("0") && itemForm[3].equals(l)
        && itemForm[4].equals("-") && itemForm[5].equals("-")) {
        consequences.add(
          new DeductionItem(outTreeName, outNode, "lb", l, "-", "-", l, "0"));
      }
    }
    return consequences;
  }

  @Override public String toString() {
    return "[β,pf,lb,l,-,-,l,0]"
      + "\n______ pf foot node address in β, β ∈ f_SA(" + outTreeName + ","
      + outNode + ")\n" + "[" + outTreeName + "," + outNode + ",lb,l,-,-,l,0]";
  }

}