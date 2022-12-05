package io.github.rpiotrow.advent2022.day05

import io.github.rpiotrow.advent2022.Input.parseInt
import zio.*

case class MoveInstruction(amount: Int, from: Int, to: Int)

object MoveInstruction:
  def parse(line: String): ZIO[Any, String, MoveInstruction] =
    line match
      case s"move $amountValue from $fromValue to $toValue" =>
        for
          amount <- parseInt(amountValue)
          from <- parseInt(fromValue)
          to <- parseInt(toValue)
        yield MoveInstruction(amount, from, to)
      case s => ZIO.fail(s"cannot parse instruction '$s'")
