package io.github.rpiotrow.advent2022.day12

case class Position(y: Int, x: Int):
  def up = Position(y - 1, x)
  def down = Position(y + 1, x)
  def left = Position(y, x - 1)
  def right = Position(y, x + 1)
