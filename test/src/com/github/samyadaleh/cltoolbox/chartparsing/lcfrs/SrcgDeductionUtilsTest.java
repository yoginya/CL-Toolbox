package com.github.samyadaleh.cltoolbox.chartparsing.lcfrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.github.samyadaleh.cltoolbox.chartparsing.lcfrs.SrcgDeductionUtils;
import com.github.samyadaleh.cltoolbox.common.lcfrs.Clause;
import com.github.samyadaleh.cltoolbox.common.lcfrs.Predicate;

public class SrcgDeductionUtilsTest {
  @Test public void testVectorMatch() throws ParseException {
    // [S(•X1 X2) -> A(X1,X2), 0, <1,0>, (<?,?>)]]
    // [[A(a •,b) -> ε, 1, <1,1>, (<0,1>, <?,?>)]
    Clause clause11 = new Clause("S(X1 X2) -> A(X1,X2)");
    String[] itemform11 =
      new String[] {"S(X1 X2) -> A(X1,X2)", "0", "1", "0", "?", "?"};
    Predicate rhs11 = clause11.getRhs().get(0);
    int iinit11 = 1;
    Clause clause12 = new Clause("A(a ,b) -> ε");
    String[] itemform12 =
      new String[] {"A(a ,b) -> ε", "1", "1", "1", "0", "1", "?", "?"};

    assertTrue(SrcgDeductionUtils.ifRhsVectorMatchesLhsVector(clause11,
      itemform11, rhs11, iinit11, clause12, itemform12));

    // [S(X1 •X2) -> A(X1,X2), 1, <1,1>, (<0,1>)]]
    // [[A(a •,b) -> ε, 2, <1,1>, (<1,2>, <?,?>)]
    // occurs in resume
    Clause clause21 = new Clause("S(X1 X2) -> A(X1,X2)");
    String[] itemform21 =
      new String[] {"S(X1 X2) -> A(X1,X2)", "1", "1", "1", "0", "1"};
    Predicate rhs21 = clause21.getRhs().get(0);
    int iinit21 = 1;
    Clause clause22 = new Clause("A(a ,b) -> ε");
    String[] itemform22 =
      new String[] {"A(a ,b) -> ε", "2", "1", "1", "1", "2", "?", "?"};

    assertTrue(!SrcgDeductionUtils.ifRhsVectorMatchesLhsVectorResume(clause21,
      itemform21, rhs21, iinit21, clause22, itemform22));

    // [S(X1 •X2) -> A(X1,X2), 1, <1,1>, (<0,1>)]]
    // [[A(a •,b) -> ε, 1, <1,1>, (<0,1>, <?,?>)]
    Clause clause31 = new Clause("S(X1 X2) -> A(X1,X2)");
    String[] itemform31 =
      new String[] {"S(X1 X2) -> A(X1,X2)", "1", "1", "1", "0", "1"};
    Predicate rhs31 = clause31.getRhs().get(0);
    int iinit31 = 1;
    Clause clause32 = new Clause("A(a ,b) -> ε");
    String[] itemform32 =
      new String[] {"A(a ,b) -> ε", "1", "1", "1", "0", "1", "?", "?"};

    assertTrue(SrcgDeductionUtils.ifRhsVectorMatchesLhsVectorResume(clause31,
      itemform31, rhs31, iinit31, clause32, itemform32));

  }

  @SuppressWarnings({"serial", "unchecked"}) @Test public void
    testgetRangesForArguments() throws ParseException {
    Predicate lhs = new Predicate("A ( a b c, d, e f )");
    String[] ranges =
      new String[] {"0", "1", "1", "2", "2", "3", "3", "4", "4", "5", "5", "6"};
    ArrayList<String> rangesList = new ArrayList<String>() {
      {
        Collections.addAll(this, ranges);
      }
    };
    List<String> rangesoverarguments =
      (List<String>) SrcgDeductionUtils.getRangesForArguments(rangesList, lhs);
    assertEquals("0", rangesoverarguments.get(0));
    assertEquals("3", rangesoverarguments.get(1));
    assertEquals("3", rangesoverarguments.get(2));
    assertEquals("4", rangesoverarguments.get(3));
    assertEquals("4", rangesoverarguments.get(4));
    assertEquals("6", rangesoverarguments.get(5));
  }
}
