package io.github.rpiotrow.advent2022.day10

import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.*
import zio.stream.ZSink

object CathodeRayTube:
  private val cycleToComputeSignalStrength = Set(20, 60, 100, 140, 180, 220)
  val solution: Solution =
    val cpuStates = Input
      .readLines("day10.input")
      .mapZIO(Instruction.parse)
      .mapAccum(CPUState.initial) { (cpuState, instruction) =>
        instruction match
          case Instruction.Noop =>
            val newCpuState = cpuState.tick()
            (newCpuState, List(newCpuState))
          case Instruction.Addx(value) =>
            val newCpuState1 = cpuState.tick()
            val newCpuState2 = newCpuState1.tick(value)
            (newCpuState2, List(newCpuState1, newCpuState2))
      }
      .flattenIterables
    ZIO.scoped {
      cpuStates.broadcast(2, 10).flatMap {
        case Chunk(streamCopy1, streamCopy2) =>
          for
            part1 <- streamCopy1
              .filter { cpuState => cycleToComputeSignalStrength.contains(cpuState.cycle) }
              .map { cpuState =>
                cpuState.cycle * cpuState.registerValueDuringCycle
              }
              .runSum
              .fork
            part2 <- streamCopy2
              .map { cpuState =>
                val rowIndex = (cpuState.cycle - 1) % 40 + 1
                val register = cpuState.registerValueDuringCycle
                if rowIndex >= register && rowIndex <= register + 2 then '#' else '.'
              }
              .grouped(40)
              .mapZIO(chunk => Console.printLine(chunk.mkString))
              .runDrain
              .mapError(_.toString)
              .fork
            signalStrengthsSum <- part1.join
            _ <- part2.join
            _ <- Console.printLine(s"The sum of signal strengths is $signalStrengthsSum")
          yield (signalStrengthsSum, 0L) // answer to second part is printed ASCII art
        case _ => ZIO.dieMessage("impossible")
      }
    }
