package io.github.rpiotrow.advent2022

import io.github.rpiotrow.advent2022.day07.NoSpaceLeftOnDevice
import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

object Day07NoSpaceLeftOnDeviceSolutionSpec extends ZIOSpecDefault:

  def spec = suite("NoSpaceLeftOnDeviceSpec")(
    test("NoSpaceLeftOnDevice solution") {
      for solution <- NoSpaceLeftOnDevice.solution
        yield assert(solution)(equalTo((1084134L, 6183184L)))
    }
  )
