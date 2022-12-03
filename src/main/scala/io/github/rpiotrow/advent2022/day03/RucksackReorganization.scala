package io.github.rpiotrow.advent2022.day03

import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.*
import zio.stream.ZSink

object RucksackReorganization:
  val solution: Solution =
    val inputStream = Input.readLines("day03.input").mapZIO(Rucksack.parse)
    ZIO.scoped {
      inputStream.broadcast(2, 10).flatMap {
        case Chunk(streamCopy1, streamCopy2) =>
          for
            fiber1 <- streamCopy1.map(_.sumItemPrioritiesInBothCompartments).run(ZSink.sum).fork
            fiber2 <- streamCopy2.grouped(3).map(_.toList).mapZIO(Rucksack.groupBadge).map(_.priority).run(ZSink.sum).fork
            sum1 <- fiber1.join
            sum2 <- fiber2.join
            _ <- Console.printLine(s"Sum of the priorities of items in both rucksack compartments is $sum1")
            _ <- Console.printLine(s"Sum of the priorities of badges is $sum2")
          yield (sum1, sum2)
        case _ => ZIO.dieMessage("impossible")
      }
    }
