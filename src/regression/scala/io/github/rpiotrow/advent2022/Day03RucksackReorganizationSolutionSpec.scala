package io.github.rpiotrow.advent2022

import io.github.rpiotrow.advent2022.day03.RucksackReorganization
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

object Day03RucksackReorganizationSolutionSpec extends ZIOSpecDefault:

  def spec = suite("RucksackReorganizationSolutionSpec")(
    test("RucksackReorganization solution") {
      for solution <- RucksackReorganization.solution
        yield assert(solution)(equalTo((7737L, 2697L)))
    }
  )
