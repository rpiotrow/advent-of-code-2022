package io.github.rpiotrow.advent2022

import io.github.rpiotrow.advent2022.day09.RopeBridge
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

object Day09RopeBridgeSolutionSpec extends ZIOSpecDefault:

  def spec = suite("RopeBridgeSpec")(
    test("RopeBridge solution") {
      for solution <- RopeBridge.solution
      yield assert(solution)(equalTo((6098L, 2597L)))
    }
  )
