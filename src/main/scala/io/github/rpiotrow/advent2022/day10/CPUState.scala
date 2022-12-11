package io.github.rpiotrow.advent2022.day10

case class CPUState(cycle: Int, registerValueDuringCycle: Int, registerValueAfterCycle: Int):
  def tick(addValue: Int = 0): CPUState =
    CPUState(cycle + 1, registerValueAfterCycle, registerValueAfterCycle + addValue)

object CPUState:
  val initial: CPUState = CPUState(0, 1, 1)
