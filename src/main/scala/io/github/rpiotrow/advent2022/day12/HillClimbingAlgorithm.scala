package io.github.rpiotrow.advent2022.day12

import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.*

object HillClimbingAlgorithm:
  val solution: Solution =
    for
      lines <- Input.readLines("day12.input").runCollect.map(_.toIndexedSeq)
      hill <- Hill.parse(lines)
      distances <- hill.shortestPaths
      fewestStepsFromS <- ZIO.fromOption(distances.get(hill.start)).orElseFail("no answer!!!")
      fewestStepsFromAnyA = hill.lowestNodes.map(distances.get).collect { case Some(v) => v }.min
      _ <- Console.printLine(s"There is $fewestStepsFromS fewest steps from 'S' to 'E'")
      _ <- Console.printLine(s"There is $fewestStepsFromAnyA fewest steps from any 'a' to 'E'")
    yield (fewestStepsFromS.toLong, fewestStepsFromAnyA.toLong)
