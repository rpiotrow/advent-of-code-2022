package io.github.rpiotrow.advent2022.day12

opaque type Distance = Int

object Distance:
  val zero: Distance = 0
  val maxValue: Distance = Integer.MAX_VALUE
  given Ordering[Distance] = Ordering.Int
  def make(v: Int): Distance = v
  def min(d1: Distance, d2: Distance): Distance =
    Math.min(d1, d2)
  extension (h: Distance)
    def increased: Distance = h + 1
    def toLong: Long = h
