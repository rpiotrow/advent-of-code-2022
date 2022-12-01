package io.github.rpiotrow.advent2022.day01

import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.stream.ZSink
import zio.{Console, ZIO}

object CalorieCounting:

  val solution: Solution =
    Input
      .readLines("day01.input")
      .split(_.isEmpty)
      .mapZIO(chunk => chunk.mapZIO(line => ZIO.attempt(line.toLong).orElseFail("Cannot parse input")))
      .map(chunk => chunk.toList.sum)
      .run(ZSink.collectAll)
      .map(_.toList.sorted.reverse.take(3))
      .flatMap { top3 =>
        for
          max <- ZIO.fromOption(top3.headOption).orElseFail("No max value!!!")
          sum = top3.sum
          _ <- Console.printLine(s"Elf carrying the most Calories has $max Calories")
          _ <- Console.printLine(s"Top three Elves are carrying $sum Calories")
        yield (max, sum)
      }
