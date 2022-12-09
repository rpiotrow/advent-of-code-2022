package io.github.rpiotrow.advent2022.day09

import io.github.rpiotrow.advent2022.Input.parseInt
import zio.*

case class Instruction(direction: Direction, steps: Int)

object Instruction:
  def parse(line: String): ZIO[Any, String, Instruction] =
    line match
      case s"$directionValue $stepsValue" =>
        for
          direction <- Direction.fromString(directionValue)
          steps <- parseInt(stepsValue)
        yield Instruction(direction, steps)
      case _ => ZIO.fail(s"cannot parse instruction ($line)")
