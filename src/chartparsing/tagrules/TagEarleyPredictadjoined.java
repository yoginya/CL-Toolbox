package chartparsing.tagrules;

import java.util.List;

import chartparsing.AbstractDynamicDeductionRule;
import common.Item;
import common.tag.Tag;
import common.tag.TagEarleyItem;

/** If a dot is at the foot node of an auxiliary tree, predict that it was
 * adjoined into another tree and move into that tree at the affected node. */
public class TagEarleyPredictadjoined extends AbstractDynamicDeductionRule {

  private final String outtreename;
  private final String outnode;
  private final Tag tag;

  /** Constructor takes a tree and a node where maybe is, was, will be adjoined,
   * also needs the grammar to retrieve information about the antecedence. */
  public TagEarleyPredictadjoined(String outtreename, String outnode, Tag tag) {
    this.outtreename = outtreename;
    this.outnode = outnode;
    this.tag = tag;
    this.name = "predict adjoined in " + outtreename + "(" + outnode + ")";
    this.antneeded = 1;
  }

  @Override public List<Item> getConsequences() {
    if (antecedences.size() == antneeded) {
      String[] itemform = antecedences.get(0).getItemform();
      String treename = itemform[0];
      String node = itemform[1];
      int l = Integer.parseInt(itemform[6]);
      boolean adjoinable = tag.isAdjoinable(treename, outtreename, outnode);
      boolean isFootNode = tag.getAuxiliaryTree(treename) != null && tag
        .getAuxiliaryTree(treename).getFoot().getGornaddress().equals(node);
      if (adjoinable && isFootNode && itemform[2].equals("lb")
        && itemform[7].equals("0") && itemform[3].equals(itemform[6])
        && itemform[4].equals("-") && itemform[5].equals("-")) {
        consequences.add(new TagEarleyItem(outtreename, outnode, "lb", l,
          (Integer) null, null, l, false));
      }
    }
    return consequences;
  }

  @Override public String toString() {
    return "[β,pf,lb,l,-,-,l,0]" + "\n______ pf foot node address in β, β ∈ f_SA("
        + outtreename + "," + outnode + ")\n" + "[" + outtreename + ","
        + outnode + ",lb,l,-,-,l,0]";
  }

}
