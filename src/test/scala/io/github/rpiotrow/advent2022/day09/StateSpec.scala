package io.github.rpiotrow.advent2022.day09

import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

object StateSpec extends ZIOSpecDefault:
  def spec = suite("day09: StateSpec")(
    test("R 4") {
      for
        r4 <- BridgeState
          .make(0)
          .applyInstruction(Instruction(Direction.Right, 4))
      yield assert(r4)(equalTo(BridgeState(Position(4, 0), Seq.empty, Position(3, 0))))
    },
    test("U 4") {
      for
        r4 <- BridgeState.make(0).applyInstruction(Instruction(Direction.Right, 4))
        u4 <- r4.applyInstruction(Instruction(Direction.Up, 4))
      yield assert(u4)(equalTo(BridgeState(Position(4, 4), Seq.empty, Position(4, 3))))
    },
    test("L 4") {
      for
        r4 <- BridgeState.make(0).applyInstruction(Instruction(Direction.Right, 4))
        u4 <- r4.applyInstruction(Instruction(Direction.Up, 4))
        l4 <- u4.applyInstruction(Instruction(Direction.Left, 3))
      yield assert(l4)(equalTo(BridgeState(Position(1, 4), Seq.empty, Position(2, 4))))
    },
    test("D 1") {
      for
        r4 <- BridgeState.make(0).applyInstruction(Instruction(Direction.Right, 4))
        u4 <- r4.applyInstruction(Instruction(Direction.Up, 4))
        l4 <- u4.applyInstruction(Instruction(Direction.Left, 3))
        d1 <- l4.applyInstruction(Instruction(Direction.Down, 1))
      yield assert(d1)(equalTo(BridgeState(Position(1, 3), Seq.empty, Position(2, 4))))
    },
    test("R 4 (2)") {
      for
        r4 <- BridgeState.make(0).applyInstruction(Instruction(Direction.Right, 4))
        u4 <- r4.applyInstruction(Instruction(Direction.Up, 4))
        l4 <- u4.applyInstruction(Instruction(Direction.Left, 3))
        d1 <- l4.applyInstruction(Instruction(Direction.Down, 1))
        r4_2 <- d1.applyInstruction(Instruction(Direction.Right, 4))
      yield assert(r4_2)(equalTo(BridgeState(Position(5, 3), Seq.empty, Position(4, 3))))
    },
    test("D 1 (2)") {
      for
        r4 <- BridgeState.make(0).applyInstruction(Instruction(Direction.Right, 4))
        u4 <- r4.applyInstruction(Instruction(Direction.Up, 4))
        l4 <- u4.applyInstruction(Instruction(Direction.Left, 3))
        d1 <- l4.applyInstruction(Instruction(Direction.Down, 1))
        r4_2 <- d1.applyInstruction(Instruction(Direction.Right, 4))
        d1_2 <- r4_2.applyInstruction(Instruction(Direction.Down, 1))
      yield assert(d1_2)(equalTo(BridgeState(Position(5, 2), Seq.empty, Position(4, 3))))
    },
    test("L 5") {
      for
        r4 <- BridgeState.make(0).applyInstruction(Instruction(Direction.Right, 4))
        u4 <- r4.applyInstruction(Instruction(Direction.Up, 4))
        l4 <- u4.applyInstruction(Instruction(Direction.Left, 3))
        d1 <- l4.applyInstruction(Instruction(Direction.Down, 1))
        r4_2 <- d1.applyInstruction(Instruction(Direction.Right, 4))
        d1_2 <- r4_2.applyInstruction(Instruction(Direction.Down, 1))
        l5 <- d1_2.applyInstruction(Instruction(Direction.Left, 5))
      yield assert(l5)(equalTo(BridgeState(Position(0, 2), Seq.empty, Position(1, 2))))
    },
    test("R 2") {
      for
        r4 <- BridgeState.make(0).applyInstruction(Instruction(Direction.Right, 4))
        u4 <- r4.applyInstruction(Instruction(Direction.Up, 4))
        l4 <- u4.applyInstruction(Instruction(Direction.Left, 3))
        d1 <- l4.applyInstruction(Instruction(Direction.Down, 1))
        r4_2 <- d1.applyInstruction(Instruction(Direction.Right, 4))
        d1_2 <- r4_2.applyInstruction(Instruction(Direction.Down, 1))
        l5 <- d1_2.applyInstruction(Instruction(Direction.Left, 5))
        r2 <- l5.applyInstruction(Instruction(Direction.Right, 2))
      yield assert(r2)(equalTo(BridgeState(Position(2, 2), Seq.empty, Position(1, 2))))
    },
    test("R 4 with 10 knots") {
      for r4 <- BridgeState.make(8).applyInstruction(Instruction(Direction.Right, 4))
      yield assert(r4)(
        equalTo(
          BridgeState(
            head = Position(4, 0),
            body = Seq(
              Position(3, 0),
              Position(2, 0),
              Position(1, 0),
              Position(0, 0),
              Position(0, 0),
              Position(0, 0),
              Position(0, 0),
              Position(0, 0)
            ),
            tail = Position(0, 0)
          )
        )
      )
    }
  )
