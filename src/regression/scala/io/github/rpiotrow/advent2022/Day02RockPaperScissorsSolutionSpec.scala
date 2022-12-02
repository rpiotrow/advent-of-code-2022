package io.github.rpiotrow.advent2022

import io.github.rpiotrow.advent2022.day02.RockPaperScissors
import zio.test.*
import zio.test.Assertion.*

object Day02RockPaperScissorsSolutionSpec extends ZIOSpecDefault:

  def spec = suite("RockPaperScissorsSolutionSpec")(
    test("RockPaperScissors solution") {
      for solution <- RockPaperScissors.solution
      yield assert(solution)(equalTo((12679L, 14470L)))
    }
  )
