package io.github.rpiotrow.advent2022.day13

import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.*
import zio.stream.ZStream

import scala.annotation.tailrec

object DistressSignal:
  val solution: Solution =
    for
      sum <- Input
        .readLines("day13.input")
        //add extra line to simplify grouping
        .concat(ZStream.fromZIO(ZIO.succeed("")))
        .grouped(3)
        .mapZIO {
          case Chunk(line1, line2, _) =>
            for
              left <- Parser.parse(line1)
              right <- Parser.parse(line2)
            yield Packet.inOrder(left, right)
          case _ => ZIO.fail("invalid input")
        }
        .zipWithIndex
        .filter { (b, _) => b == Packet.Comparison.RightOrder }
        .map { (_, index) => index + 1 }
        .runSum
      _ <- Console.printLine(s"Sum of the indices of pairs in right order is $sum")
    yield (sum, 0L)
