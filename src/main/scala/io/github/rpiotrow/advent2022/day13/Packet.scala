package io.github.rpiotrow.advent2022.day13

import scala.annotation.tailrec

enum Packet:
  case Number(value: Int) extends Packet
  case Packets(value: List[Packet]) extends Packet

object Packet:
  enum Comparison:
    case RightOrder, WrongOrder, NotDetermined

  val dividerPackets = List(
    Packet.Packets(
      List(
        Packet.Packets(
          List(
            Packet.Number(2)
          )
        )
      )
    ),
    Packet.Packets(
      List(
        Packet.Packets(
          List(
            Packet.Number(6)
          )
        )
      )
    )
  )

  import Comparison.*

  @tailrec
  def inOrder(left: Packet, right: Packet): Comparison =
    (left, right) match
      case (Packet.Number(l), Packet.Number(r)) =>
        if l < r then RightOrder else if l > r then WrongOrder else NotDetermined
      case (Packet.Packets(l), Packet.Packets(r)) =>
        inOrder(l, r)
      case (Packet.Number(l), Packet.Packets(r)) =>
        inOrder(Packet.Packets(List(Packet.Number(l))), Packet.Packets(r))
      case (Packet.Packets(l), Packet.Number(r)) =>
        inOrder(Packet.Packets(l), Packet.Packets(List(Packet.Number(r))))

  @tailrec
  private def inOrder(left: List[Packet], right: List[Packet]): Comparison =
    (left, right) match
      case (Nil, Nil)    => NotDetermined
      case (Nil, _ :: _) => RightOrder
      case (_ :: _, Nil) => WrongOrder
      case (l :: ls, r :: rs) =>
        inOrder(l, r) match
          case RightOrder    => RightOrder
          case WrongOrder    => WrongOrder
          case NotDetermined => inOrder(ls, rs)
