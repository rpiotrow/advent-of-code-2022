package io.github.rpiotrow.advent2022.day06

import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.*
import zio.stream.ZStream

object TuningTrouble:
  val solution: Solution =
    for
      line <- Input.readStrings("day06.input").runHead
      dataStreamBuffer <- ZIO.fromOption(line).orElseFail("no input!!!")
      startOfPacketMarker <- dataStreamBuffer.findMarker(4)
      startOfMessageMarker <- dataStreamBuffer.findMarker(14)
      _ <- Console.printLine(s"The first start-of-packet marker is at index $startOfPacketMarker")
      _ <- Console.printLine(s"The first start-of-message marker is at index $startOfMessageMarker")
    yield (startOfPacketMarker, startOfMessageMarker)

  extension (dataStreamBuffer: String)
    private def findMarker(requiredDistinctChars: Int): ZIO[Any, String, Long] =
      for
        maybe <- ZStream
          .fromIterable(dataStreamBuffer)
          .sliding(requiredDistinctChars)
          .zipWithIndex
          .dropWhile { case (chunk, _) =>
            chunk.toSet.size < requiredDistinctChars
          }
          .runHead
        marker <- ZIO.fromOption(maybe).orElseFail("no answer!!!")
      yield marker._2 + requiredDistinctChars
