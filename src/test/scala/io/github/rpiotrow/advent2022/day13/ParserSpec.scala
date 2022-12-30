package io.github.rpiotrow.advent2022.day13

import zio.test.Assertion.equalTo
import zio.test.{ZIOSpecDefault, assert}

object ParserSpec extends ZIOSpecDefault:

  def spec = suite("day13: ParserSpec")(
    test("empty") {
      for
        packet <- Parser.parse("[]")
        expected = Packet.Packets(List.empty)
      yield assert(packet)(equalTo(expected))
    },
    test("numbers") {
      for
        packet <- Parser.parse("[1,1,3,1,1]")
        expected = Packet.Packets(List(Packet.Number(1),Packet.Number(1),Packet.Number(3),Packet.Number(1),Packet.Number(1)))
      yield assert(packet)(equalTo(expected))
    },
    test("number sublists") {
      for
        packet <- Parser.parse("[[1],[2,3,4]]")
        expected = Packet.Packets(List(
          Packet.Packets(List(Packet.Number(1))),
          Packet.Packets(List(Packet.Number(2),Packet.Number(3),Packet.Number(4)))
        ))
      yield assert(packet)(equalTo(expected))
    },
    test("number sublist and numbers") {
      for
        packet <- Parser.parse("[[4,4],4,4]")
        expected = Packet.Packets(List(
          Packet.Packets(List(Packet.Number(4),Packet.Number(4))),
          Packet.Number(4),
          Packet.Number(4)
        ))
      yield assert(packet)(equalTo(expected))
    },
    test("empty sublist") {
      for
        packet <- Parser.parse("[[[]]]")
        expected = Packet.Packets(List(
          Packet.Packets(List(
            Packet.Packets(List())
          ))
        ))
      yield assert(packet)(equalTo(expected))
    }
  )

