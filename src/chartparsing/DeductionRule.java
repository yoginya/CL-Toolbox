package chartparsing;

import java.util.List;

import common.Item;

/** Interface that defines any kind of rule used for deduction. */
public interface DeductionRule {

  public void addAntecedence(Item item);

  public List<Item> getAntecedences();

  public void setAntecedences(List<Item> antecedences);

  public List<Item> getConsequences();

  public String getName();

  @Override public String toString();
}
