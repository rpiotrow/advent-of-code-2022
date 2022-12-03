package io.github.rpiotrow.advent2022.day03

import zio.*

opaque type RucksackItemType = Char

object RucksackItemType:
  def make(c: Char): RucksackItemType = c

extension (itemType: RucksackItemType)
  def priority: Long =
    if itemType.isLower then itemType.toLong - 97 + 1 else itemType.toLong - 65 + 27

case class Rucksack(compartment1: List[RucksackItemType], compartment2: List[RucksackItemType]):
  def sumItemPrioritiesInBothCompartments: Long =
    inBothCompartments.toList.map(_.priority).sum
  def itemTypes: Set[RucksackItemType] =
    compartment1.toSet ++ compartment2.toSet
  private def inBothCompartments: Set[RucksackItemType] =
    compartment1.toSet.intersect(compartment2.toSet)

object Rucksack:
  def parse(s: String): ZIO[Any, String, Rucksack] =
    s.length match
      case n if n % 2 == 0 =>
        val (c1, c2) = s.toList.splitAt(n / 2)
        ZIO.succeed(
          Rucksack(c1.map(RucksackItemType.make), c2.map(RucksackItemType.make))
        )
      case _ => ZIO.fail("invalid input")

  def groupBadge(group: List[Rucksack]): ZIO[Any, String, RucksackItemType] =
    group match
      case r1 :: r2 :: r3 :: Nil =>
        r1.itemTypes.intersect(r2.itemTypes).intersect(r3.itemTypes).toList match
          case Nil             => ZIO.fail(s"group has no common item type (badge)")
          case itemType :: Nil => ZIO.succeed(itemType)
          case l               => ZIO.fail(s"group has non unique badge ($l)")
      case _ => ZIO.fail("group need to have size 3")
