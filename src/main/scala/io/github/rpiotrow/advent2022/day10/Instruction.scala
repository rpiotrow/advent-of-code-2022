package io.github.rpiotrow.advent2022.day10

import io.github.rpiotrow.advent2022.Input.parseInt
import zio.*

enum Instruction:
  case Noop
  case Addx(value: Int)

object Instruction:
  def parse(line: String): ZIO[Any, String, Instruction] =
    line match
      case s"noop" => ZIO.succeed(Instruction.Noop)
      case s"addx $addValue" =>
        for value <- parseInt(addValue)
        yield Instruction.Addx(value)
      case _ => ZIO.fail(s"cannot parse line '$line' as instruction")
