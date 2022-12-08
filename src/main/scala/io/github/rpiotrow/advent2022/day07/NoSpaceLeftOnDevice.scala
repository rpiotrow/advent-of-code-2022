package io.github.rpiotrow.advent2022.day07

import io.github.rpiotrow.advent2022.day07.Directories.*
import io.github.rpiotrow.advent2022.day07.File.*
import io.github.rpiotrow.advent2022.{Input, Solution}
import zio.*

object NoSpaceLeftOnDevice:

  val solution: Solution =
    for
      dictionarySizes <- Input
        .readLines("day07.input")
        .parseDirectories
        .map(_.sizes)

      smallDirectoriesSizeSum = dictionarySizes.filter { case (_, size) => size <= 100000L }.values.sum
      _ <- Console.printLine(s"Sum of total sizes of directories with size < 100000 is $smallDirectoriesSizeSum")

      rootDirectorySize <- ZIO
        .fromOption { dictionarySizes.get(Directories.root) }
        .orElseFail("no root directory found!!!")
      freeSpace = 70000000L - rootDirectorySize
      needToRemove = 30000000 - freeSpace
      sizeOfTheDirectoryToRemove <- ZIO
        .fromOption(
          dictionarySizes.values
            .filter(_ >= needToRemove)
            .toList
            .sorted
            .headOption
        )
        .orElseFail("no answer!!!")
      _ <- Console.printLine(
        s"Size of the smallest directory that, if deleted, would free up enough space is $sizeOfTheDirectoryToRemove"
      )
    yield (smallDirectoriesSizeSum, sizeOfTheDirectoryToRemove)
