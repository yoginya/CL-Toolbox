package astar;

import java.util.HashMap;
import java.util.Map;

import common.cfg.Pcfg;
import common.cfg.PcfgProductionRule;

/** Calculates the SX estimates to be used for astar parsing. */
public class SxCalc {

  /** Calculates all inside probabilities (as absolute logs) for all
   * nonterminals in Pcfg up until nmax length. */
  public static Map<String, Double> getInsides(Pcfg cfg, int nmax) {
    Map<String, Double> insides = new HashMap<String, Double>();
    for (int n = 1; n <= nmax; n++) {
      for (String nt : cfg.getNonterminals()) {
        insides.put(getInsideKey(nt, n), Double.MAX_VALUE);
      }
      for (String nt : cfg.getNonterminals()) {
        for (int l = 1; l <= n; l++) {
          if (l == 1) {
            for (PcfgProductionRule rule : cfg.getProductionRules()) {
              String[] vars = rule.getRhs();
              Double logp = -Math.log(rule.getP());
              if (rule.getLhs().equals(nt) && vars.length == 1
                && logp < insides.get(getInsideKey(nt, l))) {
                insides.put(getInsideKey(nt, l), logp);
              }
            }
          } else {
            for (int l1 = 1; l1 <= l - 1; l1++) {
              for (PcfgProductionRule rule : cfg.getProductionRules()) {
                String[] vars = rule.getRhs();
                if (nt.equals(rule.getLhs()) && vars.length == 2) {
                  Double newp = -Math.log(rule.getP());
                  newp += insides.get(getInsideKey(vars[0], l1));
                  newp += insides.get(getInsideKey(vars[1], l - l1));
                  if (newp < insides.get(getInsideKey(nt, l))) {
                    insides.replace(getInsideKey(nt, l), newp);
                  }
                }
              }
            }
          }
        }
      }
    }
    return insides;
  }

  /** Generates a string that is used as key for inside probabilities in a human
   * readable form. */
  private static String getInsideKey(String nt, int length) {
    return "in(" + nt + "," + String.valueOf(length) + ")";
  }

  /** Calculates all outside probabilities as absolute logs for all nonterminals
   * in Pcfg for length n. Needs Insides to be calculated first. */
  public static Map<String, Double> getOutsides(Map<String, Double> insides,
    int n, Pcfg pcfg) {
    Map<String, Double> outsides = new HashMap<String, Double>();

    for (int l = n; l >= 1; l--) {
      for (int nl = 0; nl <= n - l; nl++) {
        int nr = n - nl - l;
        for (String nt : pcfg.getNonterminals()) {
          outsides.put(getOutsideKey(nt, nl, l, nr), Double.MAX_VALUE);
          if (nl == 0 && nr == 0 && nt.equals(pcfg.getStartSymbol())) {
            outsides.put(getOutsideKey(nt, nl, l, nr), 0.0);
          } else {
            for (int lc = 1; lc <= nr; lc++) {
              for (PcfgProductionRule rule : pcfg.getProductionRules()) {
                String[] vars = rule.getRhs();
                if (vars.length == 2 && vars[0].equals(nt)) {
                  Double newp = -Math.log(rule.getP());
                  newp += outsides
                    .get(getOutsideKey(rule.getLhs(), nl, l + lc, nr - lc));
                  newp += insides.get(getInsideKey(vars[1], lc));
                  outsides.put(getOutsideKey(nt, nl, l, nr),
                    Math.min(newp, outsides.get(getOutsideKey(nt, nl, l, nr))));
                }
              }
            }
            for (int lc = 1; lc <= nl; lc++) {
              for (PcfgProductionRule rule : pcfg.getProductionRules()) {
                String[] vars = rule.getRhs();
                if (vars.length == 2 && vars[1].equals(nt)) {
                  Double newp = -Math.log(rule.getP());
                  newp += outsides
                    .get(getOutsideKey(rule.getLhs(), nl - lc, l + lc, nr));
                  newp += insides.get(getInsideKey(vars[0], lc));
                  outsides.put(getOutsideKey(nt, nl, l, nr),
                    Math.min(newp, outsides.get(getOutsideKey(nt, nl, l, nr))));

                }
              }
            }
          }
        }
      }
    }
    return outsides;
  }

  /** Generates a string that is used as key for outside probabilities in a
   * human readable form. */
  public static String getOutsideKey(String nt, int nl, int l, int nr) {
    return "out(" + nt + "," + String.valueOf(nl) + "," + String.valueOf(l)
      + "," + String.valueOf(nr) + ")";
  }
}
