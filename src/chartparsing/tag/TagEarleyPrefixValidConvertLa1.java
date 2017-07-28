package chartparsing.tag;

import java.util.List;

import chartparsing.AbstractDynamicDeductionRule;
import chartparsing.DynamicDeductionRule;
import chartparsing.Item;

public class TagEarleyPrefixValidConvertLa1 extends AbstractDynamicDeductionRule
  implements DynamicDeductionRule {

  public TagEarleyPrefixValidConvertLa1() {
    this.name = "convert la1";
    this.antNeeded = 1;
  }

  @Override public List<Item> getConsequences() {
    if (antecedences.size() == antNeeded) {
      String[] itemForm = antecedences.get(0).getItemform();
      String treeName = itemForm[0];
      String node = itemForm[1];
      String pos = itemForm[2];
      String iGamma = itemForm[3];
      String i = itemForm[4];
      String j = itemForm[5];
      String k = itemForm[6];
      String l = itemForm[7];
      String adj = itemForm[8];
      if (pos.equals("la") && adj.equals("0") && !iGamma.equals("~")
        && !i.equals("~") && !j.equals("~") && !k.equals("~")) {
        consequences.add(new TagEarleyPrefixValidItem(treeName, node, "la",
          iGamma, "~", "~", "~", l, false));
      }
    }
    return consequences;
  }

  @Override public String toString() {
    return "[ɣ,p,la,i_ɣ,i,j,k,l,0]" + "\n______ \n" + "[ɣ,p,la,i_ɣ,~,~,~,l,0]";
  }

}
