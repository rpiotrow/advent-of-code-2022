package io.github.rpiotrow.advent2022.day12

import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

import scala.collection.SortedSet

/*
heights:
a 0
b 1
c 2
d 3
e 4
f 5
g 6
h 7
i 8
j 9
k 10
l 11
m 12
n 13
o 14
p 15
q 16
r 17
s 18
t 19
u 20
w 21
v 22
x 23
y 24
z 25
*/

object HillSpec extends ZIOSpecDefault:
  private val sampleInput = IndexedSeq(
    //x:01234567  //y
    "Sabqponm",//0
    "abcryxxl",//1
    "accszExk",//2
    "acctuvwj",//3
    "abdefghi" //4
  )
  def spec = suite("day12: HillSpec")(
    test("parse") {
      for
        hill <- Hill.parse(sampleInput)
        expected = Hill(Position(0,0), Position(2,5), IndexedSeq(
          IndexedSeq(0, 0, 1, 16, 15, 14, 13, 12).map(Height.make),
          IndexedSeq(0, 1, 2, 17, 24, 23, 23, 11).map(Height.make),
          IndexedSeq(0, 2, 2, 18, 25, 25, 23, 10).map(Height.make),
          IndexedSeq(0, 2, 2, 19, 20, 21, 22, 9).map(Height.make),
          IndexedSeq(0, 1, 3, 4,  5,  6,  7,  8).map(Height.make)
        ))
      yield assert(hill)(equalTo(expected))
    },
    test("shortestPath") {
      for
        hill <- Hill.parse(sampleInput)
        distances <- hill.shortestPaths
      yield assert(distances.get(hill.start))(equalTo(Some(31)))
    }
  )
