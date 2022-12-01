package io.github.rpiotrow.advent2022

import scopt.OParser

case class CliConfig(day: Option[Int] = None)

object CliConfig:
  private val configBuilder = OParser.builder[CliConfig]
  val parser: OParser[Unit, CliConfig] =
    import configBuilder._
    OParser.sequence(
      programName("advent-of-code-2022"),
      head("Run Advent of Code 2022 solutions for selected day or for all days"),
      help('h', "help"),
      opt[Int]('d', "day")
        .action((x, c) => c.copy(day = Some(x)))
        .text("select day"),
    )
