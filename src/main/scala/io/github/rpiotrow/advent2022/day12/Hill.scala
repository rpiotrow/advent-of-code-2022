package io.github.rpiotrow.advent2022.day12

import zio.ZIO

case class Hill(
    start: Position,
    end: Position,
    heights: IndexedSeq[IndexedSeq[Height]]
):

  def shortestPaths: ZIO[Any, String, Map[Position, Distance]] =
    computeShortestPaths(end, Distance.zero, Set.empty, PriorityQueue.empty, Map.empty)

  def lowestNodes: List[Position] =
    heights.zipWithIndex.map { (line, y) =>
      line.zipWithIndex.filter { (c, _) => c == Height.zero }.map { (_, x) => Position(y, x) }
    }.flatten.toList

  private def computeShortestPaths(
      currentNode: Position,
      currentDistance: Distance,
      visited: Set[Position],
      queue: PriorityQueue,
      distances: Map[Position, Distance]
  ): ZIO[Any, String, Map[Position, Distance]] =
    val toUpdate = neighbours(currentNode).filterNot(visited.contains).map { p =>
      val distance = distances.getOrElse(p, Distance.maxValue)
      (p, distance, Distance.min(distance, currentDistance.increased))
    }
    for
      newQueue <- ZIO.foldLeft(toUpdate)(queue) { case (q, (position, oldDistance, newDistance)) =>
        if oldDistance == Distance.maxValue then ZIO.succeed { q.added(position, newDistance) }
        else q.updated(position, oldDistance, newDistance)
      }
      newDistances = toUpdate.foldLeft(distances) { case (distances, (position, _, newDistance)) =>
        distances.updated(position, newDistance)
      }
      result <-
        if newQueue.isEmpty then ZIO.succeed(newDistances)
        else
          for
            min <- newQueue.takeFirstMin
            ((newDistance, newCurrent), queueUpdated) = min
            computeContinuation <- computeShortestPaths(
              newCurrent,
              newDistance,
              visited + currentNode,
              queueUpdated,
              newDistances
            )
          yield computeContinuation
    yield result

  private def height(position: Position): Option[Height] =
    if position.y >= 0 && position.y < heights.size && position.x >= 0 && position.x < heights(0).size then
      Some(heights(position.y)(position.x))
    else None

  private def neighbours(position: Position): List[Position] =
    val currentHeight = heights(position.y)(position.x)
    List(
      height(position.up).map(h => (position.up, h)),
      height(position.down).map(h => (position.down, h)),
      height(position.left).map(h => (position.left, h)),
      height(position.right).map(h => (position.right, h))
    )
      .filter { _.exists { case (_, h) => currentHeight.diff(h) <= 1 } }
      .collect { case Some((p, _)) => p }

object Hill:
  def parse(lines: IndexedSeq[String]): ZIO[Any, String, Hill] =
    val heights = lines.map(_.map {
      case 'S' => Height.make('a' - 'a')
      case 'E' => Height.make('z' - 'a')
      case t   => Height.make(t - 'a')
    }.toIndexedSeq)
    val linesWithIndex = lines.zipWithIndex
    def findCharPosition(c: Char) = ZIO.fromOption {
      linesWithIndex.find(_._1.contains(c)).map { case (line, y) => Position(y, line.indexOf(c)) }
    }
    val hill = for
      start <- findCharPosition('S')
      end <- findCharPosition('E')
    yield Hill(start, end, heights)
    hill.orElseFail("cannot parse input")
