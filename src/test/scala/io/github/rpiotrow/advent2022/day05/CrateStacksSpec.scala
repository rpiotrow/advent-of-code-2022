package io.github.rpiotrow.advent2022.day05

import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

object CrateStacksSpec extends ZIOSpecDefault:
  def spec = suite("day03: CrateStacksSpec")(
    test("top cretes") {
      /*
               [Z]
               [N]
               [D]
       [C] [M] [P]
        1   2   3
      */
      val stacks = CrateStacks.make(
        List(
          CrateStack.make(List(Crate.make('C'))),
          CrateStack.make(List(Crate.make('M'))),
          CrateStack.make(List(Crate.make('Z'), Crate.make('N'), Crate.make('D'), Crate.make('P'))),
        )
      )
      assert(stacks.topCrates)(equalTo("CMZ"))
    },
    test("moveAsCrateMover9000") {
      /*
           [D]
       [N] [C]
       [Z] [M] [P]
        1   2   3
      */
      val initial = CrateStacks.make(
        List(
          CrateStack.make(List(Crate.make('N'), Crate.make('Z'))),
          CrateStack.make(List(Crate.make('D'), Crate.make('C'), Crate.make('M'))),
          CrateStack.make(List(Crate.make('P'))),
        )
      )
      /* move 1 from 2 to 1 */
      val instruction = MoveInstruction(1, 2, 1)
      /*
       [D]
       [N] [C]
       [Z] [M] [P]
        1   2   3
      */
      val expected = CrateStacks.make(
        List(
          CrateStack.make(List(Crate.make('D'), Crate.make('N'), Crate.make('Z'))),
          CrateStack.make(List(Crate.make('C'), Crate.make('M'))),
          CrateStack.make(List(Crate.make('P'))),
        )
      )
      assert(initial.moveAsCrateMover9000(instruction))(equalTo(expected))
    }
  )
