package io.github.rpiotrow.advent2022.day12

import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.*

object HillClimbingAlgorithm:
  val solution: Solution =
    for
      lines <- Input.readLines("day12.input").runCollect.map(_.toIndexedSeq)
      hill <- Hill.parse(lines)
      distances <- hill.shortestPaths
      steps1 <- ZIO.fromOption(distances.get(hill.end)).orElseFail("no answer!!!")
      _ <- Console.printLine(s"There is $steps1 steps required to move from S to the E")
    yield (steps1.toLong, 0L)
