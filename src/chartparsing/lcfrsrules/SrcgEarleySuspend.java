package chartparsing.lcfrsrules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import chartparsing.DynamicDeductionRule;
import common.ArrayUtils;
import common.Item;
import common.lcfrs.Clause;
import common.lcfrs.Predicate;
import common.lcfrs.SrcgEarleyActiveItem;

/** Whenever we arrive at the end of an argumebt that is not the last argument,
 * we suspend the processing of this rule and we go back to the item that we
 * used to predict it. */
public class SrcgEarleySuspend implements DynamicDeductionRule {

  private List<Item> antecedences = new LinkedList<Item>();
  private List<Item> consequences = new LinkedList<Item>();
  private String name = "Suspend";

  private int antneeded = 2;

  @Override public void addAntecedence(Item item) {
    this.antecedences.add(item);
  }

  @Override public void addConsequence(Item item) {
    // ignore
  }

  @Override public List<Item> getAntecedences() {
    return this.antecedences;
  }

  @Override public void setAntecedences(List<Item> antecedences) {
    this.antecedences = antecedences;
  }

  @Override public List<Item> getConsequences() {
    if (antecedences.size() == antneeded) {
      String[] itemform1 = antecedences.get(0).getItemform();
      String[] itemform2 = antecedences.get(1).getItemform();

      if (itemform1[0].contains("->") && itemform2[0].contains("->")) {
        String clause1 = itemform1[0];
        Clause clause1parsed = new Clause(clause1);
        String pos1 = itemform1[1];
        int posint1 = Integer.parseInt(pos1);
        String i1 = itemform1[2];
        int iint1 = Integer.parseInt(i1);
        String j1 = itemform1[3];
        int jint1 = Integer.parseInt(j1);

        String clause2 = itemform2[0];
        Clause clause2parsed = new Clause(clause2);
        String pos2 = itemform2[1];
        int posint2 = Integer.parseInt(pos2);
        String i2 = itemform2[2];
        int iint2 = Integer.parseInt(i2);
        String j2 = itemform2[3];
        int jint2 = Integer.parseInt(j2);

        for (int n = 0; n < clause2parsed.getRhs().size(); n++) {
          Predicate rhspred = clause2parsed.getRhs().get(n);
          if (rhspred.getNonterminal().equals(clause1parsed.getLhsNonterminal())
            && itemform1.length > (iint1 - 1) * 2 + 5
            && itemform2.length > (iint1 - 1 + n) * 2 + 5) {
            boolean vectorsmatch = true;
            for (int m = 0; m < iint1 - 1; m++) {
              if (itemform1[m * 2 + 4].equals(itemform2[(n + m) * 2 + 4])
                && itemform1[m * 2 + 5].equals(itemform2[(n + m) * 2 + 5])) {
                vectorsmatch = false;
                break;
              }
            }
            if (vectorsmatch && itemform1.length > 2 * iint1 + 5
              && itemform1[2 * (iint1 - 1) + 4].equals(pos2)
              && itemform1[2 * (iint1 - 1) + 5].equals(pos1)
              && iint1 < clause1parsed.getLhs().getDim() && clause1parsed
                .getLhs().getArgumentByIndex(iint1).length == jint1) {
              ArrayList<String> newvector = new ArrayList<String>();
              newvector = new ArrayList<String>(Arrays.asList(ArrayUtils
                .getSubSequenceAsArray(itemform2, 4, itemform2.length)));
              newvector.set((iint2 - 1) * 2, pos2);
              newvector.set((iint2 - 1) * 2 + 1, pos1);
              consequences.add(new SrcgEarleyActiveItem(clause2, posint1, iint2,
                jint2 + 1, newvector.toArray(new String[newvector.size()])));
            }
          }
        }
        // the other way round

        for (int n = 0; n < clause1parsed.getRhs().size(); n++) {
          Predicate rhspred = clause1parsed.getRhs().get(n);
          if (rhspred.getNonterminal().equals(clause2parsed.getLhsNonterminal())
            && itemform2.length > (iint2 - 1) * 2 + 5
            && itemform1.length > (iint2 - 1 + n) * 2 + 5) {
            boolean vectorsmatch = true;
            for (int m = 0; m < iint2 - 1; m++) {
              if (itemform2[m * 2 + 4].equals(itemform1[(n + m) * 2 + 4])
                && itemform2[m * 2 + 5].equals(itemform1[(n + m) * 2 + 5])) {
                vectorsmatch = false;
                break;
              }
            }
            if (vectorsmatch && itemform2.length > 2 * iint2 + 5
              && itemform2[2 * iint2 + 4].equals(pos1)
              && itemform2[2 * iint2 + 5].equals(pos2)
              && iint2 < clause2parsed.getLhs().getDim() && clause2parsed
                .getLhs().getArgumentByIndex(iint2).length == jint2) {
              ArrayList<String> newvector = new ArrayList<String>();
              newvector = new ArrayList<String>(Arrays.asList(ArrayUtils
                .getSubSequenceAsArray(itemform1, 4, itemform1.length)));
              newvector.set(iint1 * 2, pos1);
              newvector.set(iint1 * 2 + 1, pos2);
              consequences.add(new SrcgEarleyActiveItem(clause1, posint2, iint1,
                jint1 + 1, newvector.toArray(new String[newvector.size()])));
            }
          }
        }
      }
    }
    return this.consequences;
  }

  @Override public void setConsequences(List<Item> consequences) {
    // ignore
  }

  @Override public String getName() {
    return this.name;
  }

  @Override public int getAntecedencesNeeded() {
    return this.antneeded;
  }

  @Override public void clearItems() {
    this.antecedences = new LinkedList<Item>();
    this.consequences = new LinkedList<Item>();
  }

  @Override public String toString() {
    StringBuilder representation = new StringBuilder();
    representation.append(
      "[B(ψ) -> Ψ,pos',<i,j>,ρ_B], [A(φ) -> ... B(ξ)...,pos,<k,l>,ρ_A]");
    representation.append("\n______ \n");
    representation.append("[A(φ) -> ... B(ξ)...,pos',<k,l+1>,ρ]");
    return representation.toString();
  }

}
