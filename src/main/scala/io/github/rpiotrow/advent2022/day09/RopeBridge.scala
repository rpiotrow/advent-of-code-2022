package io.github.rpiotrow.advent2022.day09

import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.*
import zio.stream.ZStream

object RopeBridge:
  val solution: Solution =
    type FoldAcc = (BridgeState, Set[Position])
    object FoldAcc:
      def initial(bodySize: Int) = (BridgeState.make(bodySize), Set(Position(0, 0)))

    def run(instructions: ZStream[Any, String, Instruction], bodySize: Int) =
      instructions
        .runFoldZIO(FoldAcc.initial(bodySize)) { (acc: FoldAcc, instruction: Instruction) =>
          ZStream
            .range(0, instruction.steps)
            .runFoldZIO(acc) { case ((state, tailVisited), _) =>
              for newState <- state.move(instruction.direction)
              yield (newState, tailVisited + newState.tail)
            }
        }
        .map { (_, tailVisited) => tailVisited.size }

    ZIO.scoped {
      Input.readLines("day09.input").mapZIO(Instruction.parse).broadcast(2, 10).flatMap {
        case Chunk(streamCopy1, streamCopy2) =>
          for
            fiber1 <- run(streamCopy1, bodySize = 0).fork
            fiber2 <- run(streamCopy2, bodySize = 8).fork
            tailVisitedCountSmallerRope <- fiber1.join
            tailVisitedCountLargerRope <- fiber2.join
            _ <- Console.printLine(s"Tail visited $tailVisitedCountSmallerRope positions for smaller rope")
            _ <- Console.printLine(s"Tail visited $tailVisitedCountLargerRope positions for larger rope")
          yield (tailVisitedCountSmallerRope, tailVisitedCountLargerRope)
        case _ => ZIO.dieMessage("impossible")
      }
    }
