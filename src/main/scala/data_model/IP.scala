package data_model

case class IP(val segments: Int*) extends Ordered[IP] {
  require(segments.size == 4, "IP contains 4 segments")
  require(segments.forall(s => (0 <= s && s <= 255)), "segment from 0 to 255")

  override def compare(that: IP): Int = {
    var result = 0
    for (i <- 0 until 4) {
      result = this.segments(i).compareTo(that.segments(i))
      if (result != 0) return result
    }
    result
  }

  override def toString: String = {
    segments.mkString(".")
  }
}
