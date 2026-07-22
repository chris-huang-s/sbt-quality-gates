package com.github.chrishuang.example

class GatesDemoSuite extends munit.FunSuite {
  test("clamp keeps values inside bounds") {
    assertEquals(GatesDemo.clamp(5, 0, 10), 5)
    assertEquals(GatesDemo.clamp(-1, 0, 10), 0)
    assertEquals(GatesDemo.clamp(99, 0, 10), 10)
  }

  test("clamp returns the bound when min equals max") {
    assertEquals(GatesDemo.clamp(3, 7, 7), 7)
    assertEquals(GatesDemo.clamp(7, 7, 7), 7)
  }

  test("average is empty for empty input") {
    assertEquals(GatesDemo.average(Seq.empty), None)
    assertEquals(GatesDemo.average(Seq(2, 4, 6)), Some(4.0))
  }

  test("average handles a single value and negatives") {
    assertEquals(GatesDemo.average(Seq(9)), Some(9.0))
    assertEquals(GatesDemo.average(Seq(-2, -4, -6)), Some(-4.0))
  }
}
