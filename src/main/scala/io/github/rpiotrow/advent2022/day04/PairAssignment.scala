package io.github.rpiotrow.advent2022.day04

import io.github.rpiotrow.advent2022.Input.parseInt
import zio.ZIO

case class SectionAssignment(from: Int, to: Int):
  def contains(other: SectionAssignment): Boolean =
    other.from >= this.from && other.to <= this.to
  def overlaps(other: SectionAssignment): Boolean =
    this.from <= other.to && other.from <= this.to

case class PairAssignment(first: SectionAssignment, second: SectionAssignment):
  def oneFullyContained: Boolean =
    first.contains(second) || second.contains(first)
  def overlapping: Boolean =
    first.overlaps(second)

object PairAssignment:
  def parse(line: String): ZIO[Any, String, PairAssignment] =
    line match
      case s"$firstFromValue-$firstToValue,$secondFromValue-$secondToValue" =>
        for
          firstFrom <- parseInt(firstFromValue)
          firstTo <- parseInt(firstToValue)
          secondFrom <- parseInt(secondFromValue)
          secondTo <- parseInt(secondToValue)
        yield PairAssignment(
          SectionAssignment(firstFrom, firstTo),
          SectionAssignment(secondFrom, secondTo)
        )
