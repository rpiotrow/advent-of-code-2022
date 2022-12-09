package io.github.rpiotrow.advent2022.day09

import io.github.rpiotrow.advent2022.day09.Direction.*
import zio.ZIO
import zio.stream.ZStream

enum Direction:
  case Left, Right, Up, Down

object Direction:
  def fromString(s: String): ZIO[Any, String, Direction] =
    s match
      case "L" => ZIO.succeed(Left)
      case "R" => ZIO.succeed(Right)
      case "U" => ZIO.succeed(Up)
      case "D" => ZIO.succeed(Down)
      case _   => ZIO.fail(s"invalid direction ($s)!!!")

case class Position(x: Int, y: Int):
  def move(direction: Direction): Position =
    direction match
      case Direction.Left  => Position(x - 1, y)
      case Direction.Right => Position(x + 1, y)
      case Direction.Up    => Position(x, y + 1)
      case Direction.Down  => Position(x, y - 1)

case class BridgeState(head: Position, body: Seq[Position], tail: Position):
  def applyInstruction(instruction: Instruction): ZIO[Any, Nothing, BridgeState] =
    ZStream
      .range(0, instruction.steps)
      .runFoldZIO(this) { case (s, _) => s.move(instruction.direction) }

  def move(direction: Direction): ZIO[Any, Nothing, BridgeState] =
    for
      newHead <- ZIO.succeed(head.move(direction))
      newBody <- ZStream
        .fromIterable(body)
        .mapAccum(newHead) { (previous, position) =>
          val newPosition = BridgeState.adjustPosition(previous, position)
          (newPosition, newPosition)
        }
        .runCollect
        .map(_.toSeq)
      newTail = BridgeState.adjustPosition(newBody.lastOption.getOrElse(newHead), tail)
    yield BridgeState(newHead, newBody, newTail)

object BridgeState:
  def make(bodySize: Int): BridgeState =
    BridgeState(Position(0, 0), Seq.fill(bodySize)(Position(0, 0)), Position(0, 0))

  private def adjustPosition(previous: Position, position: Position): Position =
    (previous.x - position.x, previous.y - position.y) match
      /*
        opabc
        n...d
        m.H.e
        l...f
        kjihg
       */
      case (0, -2)  => position.move(Down) //a
      case (-1, -2) => position.move(Left).move(Down) //b
      case (-2, -2) => position.move(Left).move(Down) //c
      case (-2, -1) => position.move(Left).move(Down) //d
      case (-2, 0)  => position.move(Left) //e
      case (-2, 1)  => position.move(Left).move(Up) //f
      case (-2, 2)  => position.move(Left).move(Up) //g
      case (-1, 2)  => position.move(Left).move(Up) //h
      case (0, 2)   => position.move(Up) //i
      case (1, 2)   => position.move(Right).move(Up) //j
      case (2, 2)   => position.move(Right).move(Up) //k
      case (2, 1)   => position.move(Right).move(Up) //l
      case (2, 0)   => position.move(Right) //m
      case (2, -1)  => position.move(Right).move(Down) //n
      case (2, -2)  => position.move(Right).move(Down) //p
      case (1, -2)  => position.move(Right).move(Down) //o
      case _        => position
