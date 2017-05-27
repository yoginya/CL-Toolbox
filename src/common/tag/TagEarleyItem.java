package common.tag;

import common.AbstractItem;
import common.Item;

/** Item of length 8 used by TAG Earley parsing. */
public class TagEarleyItem extends AbstractItem implements Item {

  /** Constructor that replaces the foot node indices by '-' if they are
   * null. */
  public TagEarleyItem(String tree, String p, String pos, int i, Integer j,
    Integer k, int l, boolean adj) {
    String footfrom = (j == null) ? "-" : String.valueOf(j);
    String footto = (k == null) ? "-" : String.valueOf(k);
    String adjrep = (adj) ? "1" : "0";
    itemform = new String[] {tree, p, pos, String.valueOf(i), footfrom, footto,
      String.valueOf(l), adjrep};
  }

  /**
   * Constructor that takes Strings instead of Integers, so I can pass in '?'.
   */
  public TagEarleyItem(String treename, String gornaddress, String pos, int i,
    String j, String k, int l, boolean adj) {
    String footfrom = (j == null) ? "-" : String.valueOf(j);
    String footto = (k == null) ? "-" : String.valueOf(k);
    String adjrep = (adj) ? "1" : "0";
    itemform = new String[] {treename, gornaddress, pos, String.valueOf(i), footfrom, footto,
      String.valueOf(l), adjrep};
  }

}
