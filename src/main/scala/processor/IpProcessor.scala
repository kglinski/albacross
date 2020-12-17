package processor

import data_model.{IP, IpScope}
import scala.collection.mutable.ListBuffer

object IpProcessor {
  private val MIN_IP: IP = IP(0, 0, 0, 0)
  private val MAX_IP: IP = IP(255, 255, 255, 255)

  def increment(ip: IP): IP = {
    if (ip.equals(MAX_IP)) {
      return ip
    }

    var done: Boolean = false
    val series = new Array[Int](4)

    for (i <- 3 to 0 by -1) {
      if (done) {
        series(i) = ip.segments(i)
      }

      if (ip.segments(i) == 255 && !done) {
        series(i) = 0
      }

      if (ip.segments(i) < 255 && !done) {
        series(i) = ip.segments(i) + 1
        done = true
      }
    }
    IP(series(0), series(1), series(2), series(3))
  }

  def increase(ip: IP, addend:Int): IP = {
    if (ip.equals(MAX_IP)) {
      return ip
    }

    var current = ip
    for(i <- 0 until addend) {
       current = increment(current)
    }
    current
  }

  def decrement(ip: IP): IP = {
    if (ip.equals(MIN_IP)) {
      return ip
    }

    var done: Boolean = false
    val series = new Array[Int](4)

    for (i <- 3 to 0 by -1) {
      if (done) {
        series(i) = ip.segments(i)
      }

      if (ip.segments(i) == 0 && !done) {
        series(i) = 255
      }

      if (ip.segments(i) > 0 && !done) {
        series(i) = ip.segments(i) - 1
        done = true
      }
    }
    IP(series(0), series(1), series(2), series(3))
  }

  def generateIpRangeFromScope(ipScope: IpScope): List[IP] = {
    val ips: ListBuffer[IP] = ListBuffer[IP]()
    var current = ipScope.start

    while (Comparator.greaterOrEqual(ipScope.end, current)) {
      ips.+=(current)
      current = increment(current)
    }

    ips.toList
  }

  def assignScopes(ips: Array[IP]): List[IpScope] = {
    if (ips.isEmpty) {
      return List.empty
    }

    val ranges = ListBuffer[List[IP]]()
    val current = ListBuffer[IP]()

    current += ips(0)

    for (index <- 1 until ips.length) {

      if (Comparator.notEqual(increment(ips(index - 1)), ips(index))) {
        ranges += current.toList
        current.clear()
      }

      current += ips(index)
    }

    if (current.nonEmpty) {
      ranges += current.toList
      current.clear()
    }

    val ipScopes = ranges.map(range => IpScope(range.head, range.last))
    ipScopes.toList
  }

}
