package io.github.rpiotrow.advent2022.day13

import io.github.rpiotrow.advent2022.Input.parseInt
import zio.ZIO

object Parser:
  private enum ParseItem:
    case ListStart
    case ParsedPacket(packet: Packet)

  private val digits = "([0-9]+)(.*)".r

  def parse(string: String): ZIO[Any, String, Packet] =
    ZIO
      .iterate { (string, List.empty[ParseItem]) } { (string, _) => string != "" } { (string, stack) =>
        string match
          case s",$rest" =>
            ZIO.succeed { (rest, stack) }
          case s"[$rest" =>
            ZIO.succeed { (rest, ParseItem.ListStart :: stack) }
          case digits(ds, rest) =>
            for number <- parseInt(ds)
            yield (rest, ParseItem.ParsedPacket(Packet.Number(number)) :: stack)
          case s"]$rest" =>
            def isParsedPacket = (item: ParseItem) =>
              item match
                case _: ParseItem.ParsedPacket => true
                case _                         => false
            val list = stack
              .takeWhile(isParsedPacket)
              .collect { case ParseItem.ParsedPacket(p) => p }
              .reverse
            val newPacket = ParseItem.ParsedPacket(Packet.Packets(list))
            val stackWithoutList = stack.dropWhile(isParsedPacket)
            stackWithoutList match
              case ParseItem.ListStart :: newStack => ZIO.succeed { (rest, newPacket :: newStack) }
              case _                               => ZIO.fail("invalid input!!!")
      }
      .flatMap { (_, stack) =>
        stack match
          case ParseItem.ParsedPacket(p) :: Nil => ZIO.succeed(p)
          case _                                => ZIO.fail("cannot parse!!!")
      }
