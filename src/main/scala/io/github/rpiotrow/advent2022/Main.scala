package io.github.rpiotrow.advent2022

import io.github.rpiotrow.advent2022.day01.CalorieCounting
import io.github.rpiotrow.advent2022.day02.RockPaperScissors
import io.github.rpiotrow.advent2022.day03.RucksackReorganization
import io.github.rpiotrow.advent2022.day04.CampCleanup
import io.github.rpiotrow.advent2022.day05.SupplyStacks
import io.github.rpiotrow.advent2022.day06.TuningTrouble
import io.github.rpiotrow.advent2022.day07.NoSpaceLeftOnDevice

import scopt.OParser
import zio.*

type SolutionError = String | java.io.IOException
type SolutionResult = Long | String
type Solution = ZIO[Any, SolutionError, (SolutionResult, SolutionResult)]

private val days: Map[Int, Solution] = Map(
  1 -> CalorieCounting.solution,
  2 -> RockPaperScissors.solution,
  3 -> RucksackReorganization.solution,
  4 -> CampCleanup.solution,
  5 -> SupplyStacks.solution,
  6 -> TuningTrouble.solution,
  7 -> NoSpaceLeftOnDevice.solution
)

object Main extends ZIOAppDefault:

  private def parseArgs(args: Seq[String]) =
    OParser.parse(CliConfig.parser, args, CliConfig()) match
      case Some(config) =>
        ZIO.succeed(config)
      case _ =>
        ZIO.fail("Invalid parameters!!!")

  private def solution(day: Int) =
    for
      _ <- Console.printLine(s"Day: $day")
      _ <- days.getOrElse(day, ZIO.fail("There is no such day!!!"))
    yield ()

  def run =
    for
      args <- getArgs
      cli <- parseArgs(args)
      result <- cli.day.map(solution).getOrElse(ZIO.foreach(days.keys)(solution))
    yield result
