package io.github.rpiotrow.advent2022.day12

import zio.ZIO

import scala.collection.immutable.SortedMap

opaque type PriorityQueue = SortedMap[Distance, Set[Position]]

object PriorityQueue:
  def empty: PriorityQueue = SortedMap.empty
  extension (priorityQueue: PriorityQueue)
    def isEmpty: Boolean = priorityQueue.isEmpty
    def takeFirstMin: ZIO[Any, String, ((Distance, Position), PriorityQueue)] =
      if priorityQueue.isEmpty then ZIO.fail("cannot get min from empty")
      else
        ZIO.succeed {
          val distance = priorityQueue.firstKey
          val positions = priorityQueue(distance)
          val position = positions.head
          val updatedQueue =
            if positions.size == 1 then priorityQueue.removed(distance)
            else priorityQueue.updated(distance, positions.excl(position))
          ((distance, position), updatedQueue)
        }
    def added(position: Position, distance: Distance): PriorityQueue =
      val positions = priorityQueue.getOrElse(distance, Set.empty)
      priorityQueue.updated(distance, positions.incl(position))
    def updated(position: Position, oldDistance: Distance, newDistance: Distance): ZIO[Any, String, PriorityQueue] =
      for
        oldPositions <- ZIO.fromOption(priorityQueue.get(oldDistance)).orElseFail("priority queue update failed!!!")
        newPositions <- ZIO.fromOption(priorityQueue.get(newDistance)).orElseFail("priority queue update failed!!!")
      yield priorityQueue
        .updated(oldDistance, oldPositions.excl(position))
        .updated(oldDistance, newPositions.incl(position))
