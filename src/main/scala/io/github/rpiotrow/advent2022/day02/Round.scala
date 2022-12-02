package io.github.rpiotrow.advent2022.day02

import io.github.rpiotrow.advent2022.day02.Outcome.*
import zio.*

trait Shape:
  def score: Long
  def winningWith: Shape
  def loosingWith: Shape

case object Rock extends Shape:
  override val score: Long = 1L
  override val winningWith: Shape = Scissors
  override val loosingWith: Shape = Paper

case object Paper extends Shape:
  override val score: Long = 2L
  override val winningWith: Shape = Rock
  override val loosingWith: Shape = Scissors

case object Scissors extends Shape:
  override val score: Long = 3L
  override val winningWith: Shape = Paper
  override val loosingWith: Shape = Rock

enum Outcome:
  case Lose, Draw, Win

case class Round(opponent: Shape, player: Shape):
  def score: Long = player.score + outcomeScore
  private def outcomeScore =
    (player, opponent) match
      case (s1, s2) if s1.winningWith == s2 => 6
      case (s1, s2) if s1 == s2             => 3
      case _                                => 0

object Round:
  def parseWithShape(line: String): ZIO[Any, String, Round] =
    line match
      case s"$opponentShapeValue $playerShapeValue" =>
        for
          opponent <- opponentShape(opponentShapeValue)
          player <- playerShape(playerShapeValue)
        yield Round(opponent, player)
      case _ => ZIO.fail("cannot parse input")

  def parseWithOutcome(line: String): ZIO[Any, String, Round] =
    line match
      case s"$opponentShapeValue $outcomeValue" =>
        for
          opponent <- opponentShape(opponentShapeValue)
          outcome <- roundOutcome(outcomeValue)
          player = playerShape(opponent, outcome)
        yield Round(opponent, player)
      case _ => ZIO.fail("cannot parse input")

  private def opponentShape(s: String) = s match
    case "A" => ZIO.succeed(Rock)
    case "B" => ZIO.succeed(Paper)
    case "C" => ZIO.succeed(Scissors)
    case _   => ZIO.fail("cannot parse input")
  private def playerShape(s: String) = s match
    case "X" => ZIO.succeed(Rock)
    case "Y" => ZIO.succeed(Paper)
    case "Z" => ZIO.succeed(Scissors)
    case _   => ZIO.fail("cannot parse input")
  private def roundOutcome(s: String) = s match
    case "X" => ZIO.succeed(Lose)
    case "Y" => ZIO.succeed(Draw)
    case "Z" => ZIO.succeed(Win)
    case _   => ZIO.fail("cannot parse input")
  private def playerShape(opponent: Shape, outcome: Outcome): Shape =
    outcome match
      case Lose => opponent.winningWith
      case Draw => opponent
      case Win  => opponent.loosingWith
