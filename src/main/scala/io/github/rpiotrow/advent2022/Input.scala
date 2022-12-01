package io.github.rpiotrow.advent2022

import zio.ZIO
import zio.stream.{ZPipeline, ZStream}

import java.io.IOException

object Input:
  def readStrings(inputFileName: String): ZStream[Any, String, String] =
    val inputStream = Option(this.getClass.getClassLoader.getResourceAsStream(inputFileName))
    val zioInputStream = ZIO.fromOption(inputStream).orElseFail(new IOException("input file not found"))
    ZStream
      .fromInputStreamZIO(zioInputStream)
      .via(ZPipeline.utf8Decode)
      .mapError(_.getMessage)

  def readLines(inputFileName: String): ZStream[Any, String, String] =
    readStrings(inputFileName).via(ZPipeline.splitLines)

  def parseInt(s: String): ZIO[Any, String, Int] =
    ZIO.attempt(s.toInt).mapError(ex => s"Cannot parse '$s' as Int: ${ex.getMessage}")
