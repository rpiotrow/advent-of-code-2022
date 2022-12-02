package io.github.rpiotrow.advent2022.day02

import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.*
import zio.stream.ZSink

object RockPaperScissors:
  val solution: Solution =
    val inputStream = Input.readLines("day02.input")
    ZIO.scoped {
      inputStream.broadcast(2, 10).flatMap {
        case Chunk(streamCopy1, streamCopy2) =>
          for
            fiber1 <- streamCopy1.mapZIO(Round.parseWithShape).map(_.score).run(ZSink.sum).fork
            fiber2 <- streamCopy2.mapZIO(Round.parseWithOutcome).map(_.score).run(ZSink.sum).fork
            sum1 <- fiber1.join
            sum2 <- fiber2.join
            _ <- Console.printLine(s"Total score for part 1 is $sum1")
            _ <- Console.printLine(s"Total score for part 2 is $sum2")
          yield (sum1, sum2)
        case _ => ZIO.dieMessage("impossible")
      }
    }
