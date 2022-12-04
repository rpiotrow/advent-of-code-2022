package io.github.rpiotrow.advent2022

import io.github.rpiotrow.advent2022.day04.CampCleanup
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

object Day04CampCleanupSolutionSpec extends ZIOSpecDefault:

  def spec = suite("CampCleanupSolutionSpec")(
    test("CampCleanup solution") {
      for solution <- CampCleanup.solution
      yield assert(solution)(equalTo((444L, 801L)))
    }
  )
