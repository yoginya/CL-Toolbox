package com.github.samyadaleh.cltoolbox.common.tag;

import org.junit.Test;

import com.github.samyadaleh.cltoolbox.common.tag.Vertex;

import static org.junit.Assert.*;

public class VertexTest {
  @Test public void testSiblings() {
    Vertex v1 = new Vertex("S");
    v1.setGornaddress("");
    assertNull(v1.getGornAddressOfPotentialRightSibling());

    Vertex v2 = new Vertex("A");
    v2.setGornaddress(".1");
    assertEquals(".2", v2.getGornAddressOfPotentialRightSibling());

    Vertex v3 = new Vertex("B");
    v3.setGornaddress(".1.1");
    assertEquals(".1.2", v3.getGornAddressOfPotentialRightSibling());
  }

}
