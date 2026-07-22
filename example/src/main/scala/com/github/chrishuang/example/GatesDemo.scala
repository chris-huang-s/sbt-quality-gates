package com.github.chrishuang.example

/** Tiny pure helpers used to exercise the quality-gate test step. */
object GatesDemo {
  def clamp(value: Int, min: Int, max: Int): Int =
    if (value < min) min
    else if (value > max) max
    else value

  def average(values: Seq[Int]): Option[Double] =
    if (values.isEmpty) None
    else Some(values.sum.toDouble / values.size)
}
