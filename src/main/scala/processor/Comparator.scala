package processor

import data_model.IP

object Comparator {

  def greaterOrEqual(ip: IP, ipCompared: IP): Boolean = {
    ip.compare(ipCompared) >= 0
  }

  def lessOrEqual(ip: IP, ipCompared: IP): Boolean = {
    ip.compare(ipCompared) <= 0
  }

  def notEqual(ip: IP, ipCompared: IP): Boolean = {
    ip.compare(ipCompared) != 0
  }
}
