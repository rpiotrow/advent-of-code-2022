package io.github.rpiotrow.advent2022

import io.github.rpiotrow.advent2022.day01.CalorieCounting
import zio.test.*
import zio.test.Assertion.*

object Day01CalorieCountingSpec extends ZIOSpecDefault:

  def spec = suite("CalorieCountingSolutionSpec")(
    test("CalorieCounting solution") {
      for solution <- CalorieCounting.solution
        yield assert(solution)(equalTo((69836L, 207968L)))
    }
  )
