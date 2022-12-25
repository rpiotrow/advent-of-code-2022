package io.github.rpiotrow.advent2022.day12

opaque type Height = Int

object Height:
  def make(v: Int): Height = v
  extension (height: Height) def diff(other: Height): Int = height - other
