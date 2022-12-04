package io.github.rpiotrow.advent2022.day04

import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.*
import zio.stream.ZSink

object CampCleanup:
  val solution: Solution =
    val inputStream = Input.readLines("day04.input").mapZIO(PairAssignment.parse)
    ZIO.scoped {
      inputStream.broadcast(2, 10).flatMap {
        case Chunk(streamCopy1, streamCopy2) =>
          for
            fiber1 <- streamCopy1.filter(_.oneFullyContained).run(ZSink.count).fork
            fiber2 <- streamCopy2.filter(_.overlapping).run(ZSink.count).fork
            fullyContains <- fiber1.join
            overlaps <- fiber2.join
            _ <- Console.printLine(s"There are $fullyContains pairs with one range fully contain the other")
            _ <- Console.printLine(s"There are $overlaps pairs with overlapping ranges")
          yield (fullyContains, overlaps)
        case _ => ZIO.dieMessage("impossible")
      }
    }
