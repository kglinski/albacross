package data_model

case class IpScope(val start: IP, val end: IP) {
  override def toString: String = {
    start + ", " + end
  }
}
