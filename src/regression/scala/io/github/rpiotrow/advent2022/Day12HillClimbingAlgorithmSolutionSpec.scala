package io.github.rpiotrow.advent2022

import io.github.rpiotrow.advent2022.day12.HillClimbingAlgorithm
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

object Day12HillClimbingAlgorithmSolutionSpec extends ZIOSpecDefault:

  def spec = suite("HillClimbingAlgorithmSpec")(
    test("HillClimbingAlgorithm solution") {
      for solution <- HillClimbingAlgorithm.solution
      yield assert(solution)(equalTo((330L, 321L)))
    }
  )
