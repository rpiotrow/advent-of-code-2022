package io.github.rpiotrow.advent2022.day03

import zio.stream.ZStream
import zio.test.*
import zio.test.Assertion.*

object RucksackItemTypeSpec extends ZIOSpecDefault:
  def spec = suite("day03: RucksackItemTypeSpec")(
    test("a is 1") {
      val itemType = RucksackItemType.make('a')
      assert(itemType.priority)(equalTo(1L))
    },
    test("b is 2") {
      val itemType = RucksackItemType.make('b')
      assert(itemType.priority)(equalTo(2L))
    },
    test("z is 26") {
      val itemType = RucksackItemType.make('z')
      assert(itemType.priority)(equalTo(26L))
    },
    test("A is 27") {
      val itemType = RucksackItemType.make('A')
      assert(itemType.priority)(equalTo(27L))
    },
    test("Z is 52") {
      val itemType = RucksackItemType.make('Z')
      assert(itemType.priority)(equalTo(52L))
    }
  )
