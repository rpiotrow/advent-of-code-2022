package io.github.rpiotrow.advent2022.day05

import io.github.rpiotrow.advent2022.day05.CrateStack.{append, takeAndDrop}
import zio.*
import zio.stream.ZSink

opaque type Crate = Char
object Crate:
  def make(c: Char): Crate = c

opaque type CrateStack = List[Crate]
object CrateStack:
  def make(l: List[Crate]): CrateStack = l
  extension (crateStack: CrateStack)
    def takeAndDrop(amount: Int): (List[Crate], CrateStack) =
      (crateStack.take(amount), crateStack.drop(amount))
    def append(crates: List[Crate]): CrateStack =
      crateStack ++ crates

opaque type CrateStacks = IndexedSeq[CrateStack]
object CrateStacks:
  def parse(lines: List[String]): CrateStacks =
    val tab = Vector.from(lines.map(_.toVector))
    val rotated = Vector
      .tabulate(tab.last.size + 1, tab.size) { (x, y) =>
        tab(y).applyOrElse(x, _ => ' ')
      }
      .filter(_.last.isDigit)
    rotated.map { v =>
      CrateStack.make(v.filter(_.isLetter).map(Crate.make).toList)
    }
  def make(l: List[CrateStack]): CrateStacks = IndexedSeq.from(l)
  extension (crateStacks: CrateStacks)
    def topCrates: String =
      crateStacks.toList.map(_.headOption).collect { case Some(c) => c }.mkString
    def moveAsCrateMover9000(instruction: MoveInstruction): CrateStacks =
      move(instruction, _.reverse)
    def moveAsCrateMover9001(instruction: MoveInstruction): CrateStacks =
      move(instruction, identity)
    private def move(instruction: MoveInstruction, crateTransform: List[Crate] => List[Crate]): CrateStacks =
      val (crates, remainingStack) = crateStacks(instruction.from - 1).takeAndDrop(instruction.amount)
      crateStacks
        .updated(instruction.from - 1, remainingStack)
        .updated(instruction.to - 1, crateStacks(instruction.to - 1).prependedAll(crateTransform(crates)))
