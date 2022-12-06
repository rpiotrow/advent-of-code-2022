package io.github.rpiotrow.advent2022

import io.github.rpiotrow.advent2022.day06.TuningTrouble
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

object Day06TuningTroubleSolutionSpec extends ZIOSpecDefault:

  def spec = suite("TuningTroubleSolutionSpec")(
    test("TuningTrouble solution") {
      for solution <- TuningTrouble.solution
      yield assert(solution)(equalTo((1640L, 3613L)))
    }
  )
