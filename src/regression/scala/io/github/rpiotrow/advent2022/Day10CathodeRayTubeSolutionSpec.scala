package io.github.rpiotrow.advent2022

import io.github.rpiotrow.advent2022.day10.CathodeRayTube
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

object Day10CathodeRayTubeSolutionSpec extends ZIOSpecDefault:

  def spec = suite("CathodeRayTubeSpec")(
    test("CathodeRayTube solution") {
      for solution <- CathodeRayTube.solution
      //part 2 print letters on console: PLEFULPB
      yield assert(solution)(equalTo((16480L, 0L)))
    }
  )
