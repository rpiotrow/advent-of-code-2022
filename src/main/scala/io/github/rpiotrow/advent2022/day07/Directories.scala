package io.github.rpiotrow.advent2022.day07

import io.github.rpiotrow.advent2022.Input.parseInt
import io.github.rpiotrow.advent2022.day07.File.*
import zio.ZIO
import zio.stream.ZStream

enum File:
  case Directory(path: List[String]) extends File
  case PlainData(size: Long, name: String) extends File

opaque type Directories = Map[Directory, List[File]]

object Directories {
  val root: Directory = Directory(List("/"))
  private type ParsingState = (Directory, Directories)
  extension (inputStream: ZStream[Any, String, String])
    def parseDirectories: ZIO[Any, String, Directories] =
      inputStream
        .runFoldZIO[Any, String, ParsingState]((Directory(List.empty), Map.empty)) {
          case ((Directory(path), dictionary), line) =>
            line match
              case "$ cd .." =>
                ZIO.succeed {
                  (Directory(path.tail), dictionary)
                }
              case s"$$ cd $directoryName" =>
                ZIO.succeed {
                  (Directory(directoryName :: path), dictionary)
                }
              case "$ ls" =>
                ZIO.succeed {
                  (Directory(path), dictionary)
                }
              case s"dir $directoryName" =>
                val currentDirectory: Directory = Directory(path)
                val currentDirectoryFiles = dictionary.getOrElse(currentDirectory, List.empty)
                val updatedDictionary =
                  dictionary.updated(currentDirectory, Directory(directoryName :: path) :: currentDirectoryFiles)
                ZIO.succeed {
                  (Directory(path), updatedDictionary)
                }
              case s"$sizeValue $fileName" =>
                val currentDirectory: Directory = Directory(path)
                val currentDirectoryFiles = dictionary.getOrElse(currentDirectory, List.empty)
                parseInt(sizeValue).orElseFail("invalid size").map { size =>
                  val updatedDictionary = dictionary.updated(currentDirectory, PlainData(size, fileName) :: currentDirectoryFiles)
                  (Directory(path), updatedDictionary)
                }
        }.map(_._2)

  extension (directories: Directories)
    def sizes = directories.map { case (directory, files) =>
      (directory, directories.computeSize(files))
    }
    private def computeSize(files: List[File]): Long =
      files.map {
        case directory: Directory =>
          computeSize(directories.getOrElse(directory, List.empty))
        case PlainData(size, _) => size
      }.sum

}
