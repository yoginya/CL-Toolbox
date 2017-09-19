package chartparsing.tag.earleyprefixvalid;

import java.util.List;

import chartparsing.AbstractDynamicDeductionRule;
import chartparsing.DeductionItem;
import chartparsing.DynamicDeductionRule;
import chartparsing.Item;
import common.tag.Tag;

public class TagEarleyPrefixValidPredictAdjoinable
  extends AbstractDynamicDeductionRule implements DynamicDeductionRule {

  private final Tag tag;
  private final String auxTreeName;

  public TagEarleyPrefixValidPredictAdjoinable(String auxTreeName, Tag tag) {
    this.tag = tag;
    this.name = "predict adjoinable";
    this.auxTreeName = auxTreeName;
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
      if (pos.equals("la") && adj.equals("0") && iGamma.equals("~")
        && i.equals("~") && j.equals("~") && k.equals("~") && !l.equals("~")
        && tag.isAdjoinable(auxTreeName, treeName, node)) {
        consequences.add(
          new DeductionItem(auxTreeName, "", "la", l, l, "-", "-", l, "0"));
      }
    }
    return consequences;
  }

  @Override public String toString() {
    return "[ɣ,p,la,~,~,~,~,l,0]" + "\n______ " + auxTreeName + " ∈ f_SA(ɣ,p)\n"
      + "[" + auxTreeName + ",ε,la,l,l,-,-,l,0]";
  }

}