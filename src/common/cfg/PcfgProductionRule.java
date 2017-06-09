package common.cfg;

/** Representation of a context-free rule where the lhs is only allowed to
 * contain one symbol and the rule has a probability. */
public class PcfgProductionRule {
  private Double p = 0.0;
  private final String lhs;
  private final String[] rhs;

  /** Construction of an array of length 3 where the elements are lhs, rhs and
   * probability. */
  PcfgProductionRule(String[] rule) {
    this.lhs = rule[0];
    this.rhs = rule[1].split(" ");
    this.p = Double.parseDouble(rule[2]);
  }

  PcfgProductionRule(String lhs, String[] rhs, double p) {
    this.lhs = lhs;
    this.rhs = rhs;
    this.p = p;
  }

  public String getLhs() {
    return this.lhs;
  }

  public String[] getRhs() {
    return this.rhs;
  }

  public Double getP() {
    return this.p;
  }

  @Override public String toString() {
    if (rhs[0].equals("")) {
      return String.valueOf(p) + " : " + lhs + " -> ε";
    } else {
      return String.valueOf(p) + " : " + lhs + " -> " + String.join(" ", rhs);
    }
  }
}
