package processor

import data_model.IP
import scala.collection.mutable

object IpIntersection {

  val intersectedIps: mutable.Set[IP] = mutable.SortedSet[IP]()

  def add(ip: IP): Unit = {
    intersectedIps.+=(ip)
  }

  def intersected(): Seq[IP] = {
    intersectedIps.toSeq
  }

}
