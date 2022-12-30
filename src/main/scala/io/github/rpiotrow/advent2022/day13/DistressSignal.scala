package io.github.rpiotrow.advent2022.day13

import io.github.rpiotrow.advent2022.day13.Packet.Comparison
import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.*
import zio.stream.ZStream

import scala.annotation.tailrec

object DistressSignal:
  val solution: Solution =
    val packets = Input
      .readLines("day13.input")
      //add extra line to simplify grouping
      .concat(ZStream.fromZIO(ZIO.succeed("")))
      .grouped(3)
      .mapZIO {
        case Chunk(line1, line2, _) =>
          for
            left <- Parser.parse(line1)
            right <- Parser.parse(line2)
          yield (left, right)
        case _ => ZIO.fail("invalid input")
      }
    ZIO.scoped {
      packets.broadcast(2, 10).flatMap {
        case Chunk(streamCopy1, streamCopy2) =>
          for
            fiber1 <- streamCopy1
              .zipWithIndex
              .filter { case ((left, right), _) => Packet.inOrder(left, right) == Packet.Comparison.RightOrder }
              .map { (_, index) => index + 1 }
              .runSum
              .fork
            fiber2 <-
              streamCopy2
                .flatMap { (left, right) => ZStream.fromIterable(List(left, right)) }
                .runCollect
                .map(_.toList)
                .map { list =>
                  given Ordering[Packet] = (l: Packet, r: Packet) => Packet.inOrder(l, r) match
                    case Comparison.RightOrder => -1
                    case Comparison.NotDetermined => 0
                    case Comparison.WrongOrder => 1
                  (list ++ Packet.dividerPackets)
                    .sorted
                    .zipWithIndex
                    .filter { (p, _) => Packet.dividerPackets.contains(p) }
                    .map { (_, index) => index }
                    .map(_ + 1)
                    .product
                }.fork
            sum <- fiber1.join
            product <- fiber2.join
            _ <- Console.printLine(s"Sum of the indices of pairs in right order is $sum")
            _ <- Console.printLine(s"The decoder key for the distress signal is $product")
          yield (sum, product)
        case _ => ZIO.dieMessage("impossible")
      }
    }
