package io.github.rpiotrow.advent2022.day05

import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.*
import zio.stream.ZSink

object SupplyStacks:
  val solution: Solution =
    ZIO.scoped {
      Input
        .readLines("day05.input")
        .peel(ZSink.collectAllWhile(_.size > 0))
        .flatMap { case (crateStacksValue, instructionsStream) =>
          val initialState = CrateStacks.parse(crateStacksValue.toList)
          val instructions = instructionsStream.drop(1).mapZIO(MoveInstruction.parse)
          ZIO.scoped {
            instructions.broadcast(2, 10).flatMap {
              case Chunk(streamCopy1, streamCopy2) =>
                for
                  fiber9000 <- streamCopy1
                    .runFold(initialState) { (crateStacks, instructions) =>
                      crateStacks.moveAsCrateMover9000(instructions)
                    }
                    .fork
                  fiber9001 <- streamCopy2
                    .runFold(initialState) { (crateStacks, instructions) =>
                      crateStacks.moveAsCrateMover9001(instructions)
                    }
                    .fork
                  finalState9000 <- fiber9000.join
                  finalState9001 <- fiber9001.join
                  topCrates9000 = finalState9000.topCrates
                  topCrates9001 = finalState9001.topCrates
                  _ <- Console.printLine(s"Top crates for CrateMover 9000 is $topCrates9000")
                  _ <- Console.printLine(s"Top crates for CrateMover 9001 is $topCrates9001")
                yield (topCrates9000, topCrates9001)
              case _ => ZIO.dieMessage("impossible")
            }
          }
        }
    }
