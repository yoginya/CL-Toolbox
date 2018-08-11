package com.github.samyadaleh.cltoolbox.chartparsing.tag.earley;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.github.samyadaleh.cltoolbox.chartparsing.AbstractDynamicDeductionRule;
import com.github.samyadaleh.cltoolbox.chartparsing.DeductionChartItem;
import com.github.samyadaleh.cltoolbox.chartparsing.ChartItemInterface;
import com.github.samyadaleh.cltoolbox.common.TreeUtils;
import com.github.samyadaleh.cltoolbox.common.tag.Tag;
import com.github.samyadaleh.cltoolbox.common.tag.Tree;

/** If a potential initial tree is complete, substitute it if possible. */
public class TagEarleySubstitute extends AbstractDynamicDeductionRule {

  private final String outTreeName;
  private final String outNode;
  private final Tag tag;

  /**
   * Constructor takes a tree and a node where maybe is, was, will be adjoined,
   * also needs the grammar to retrieve information about the antecedence.
   */
  public TagEarleySubstitute(String outTreeName, String outNode, Tag tag) {
    this.outTreeName = outTreeName;
    this.outNode = outNode;
    this.tag = tag;
    this.name = "substitute in " + outTreeName + "(" + outNode + ")";
    this.antNeeded = 1;
  }

  @Override public List<ChartItemInterface> getConsequences() throws ParseException {
    if (antecedences.size() == antNeeded) {
      String[] itemForm = antecedences.get(0).getItemform();
      String treeName = itemForm[0];
      String node = itemForm[1];
      String pos = itemForm[2];
      String i = itemForm[3];
      String f1 = itemForm[4];
      String f2 = itemForm[5];
      String j = itemForm[6];
      String adj = itemForm[7];
      String iniTreeRootLabel = tag.getTree(treeName).getRoot().getLabel();
      String substNodeLabel =
        tag.getTree(outTreeName).getNodeByGornAdress(outNode).getLabel();
      if (tag.getInitialTree(treeName) != null && node.equals("")
        && f1.equals("-") && f2.equals("-") && adj.equals("0")
        && pos.equals("ra") && iniTreeRootLabel.equals(substNodeLabel)) {
        ChartItemInterface consequence =
          new DeductionChartItem(outTreeName, outNode, "rb", i, "-", "-", j, "0");
        Tree derivedTreeBase = tag.getTree(outTreeName);
        List<Tree> derivedTrees = new ArrayList<Tree>();
        for (Tree tree : antecedences.get(0).getTrees()) {
          derivedTrees
            .add(TreeUtils.performLeftmostSubstitution(derivedTreeBase, tree));
        }
        consequence.setTrees(derivedTrees);
        consequences.add(consequence);
        // imagine a tree with 1 node where you would substitute into the root
        // ...
        String outNodeName = outNode.length() == 0 ? "ε" : outNode;
        this.name = "substitute " + outTreeName + "[" + outNodeName + ","
          + treeName + "]";
      }
    }
    return consequences;
  }

  @Override public String toString() {
    return "[α,ε,ra,i,-,-,j,0]" + "\n______ " + outTreeName + "(" + outNode
      + ") a substitution node, α ∈ I, l(" + outTreeName + "," + outNode
      + ") = l(α,ε)\n" + "[" + outTreeName + "," + outNode + ",rb,i,-,-,j,0]";
  }

}