package io.github.rpiotrow.advent2022

import io.github.rpiotrow.advent2022.day05.SupplyStacks
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

object Day05SupplyStacksSolutionSpec extends ZIOSpecDefault:

  def spec = suite("SupplyStacksSolutionSpec")(
    test("SupplyStacks solution") {
      for solution <- SupplyStacks.solution
      yield assert(solution)(equalTo(("FWSHSPJWM", "PWPWHGFZS")))
    }
  )
