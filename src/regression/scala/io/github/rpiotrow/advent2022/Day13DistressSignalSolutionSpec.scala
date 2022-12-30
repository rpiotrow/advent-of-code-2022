package io.github.rpiotrow.advent2022

import io.github.rpiotrow.advent2022.day13.DistressSignal
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

object Day13DistressSignalSolutionSpec extends ZIOSpecDefault:

  def spec = suite("DistressSignalSpec")(
    test("DistressSignal solution") {
      for solution <- DistressSignal.solution
      yield assert(solution)(equalTo((5717L, 25935L)))
    }
  )
