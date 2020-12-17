package generator

import data_model.{IP, IpScope}
import processor.IpProcessor

import scala.collection.mutable.ListBuffer
import scala.util.Random

object DataGenerator {

  def generateIpScopes(scopes: Int, nestedScopes: Int, numberScope: Int): List[IpScope] = {
    var ipScopes = ListBuffer[IpScope]()
    val series = new Array[Int](4)

    for (c <- 0 until scopes) {
      series(0) = generateNumberInScope(197, 204)
      series(1) = generateNumberInScope(0, 255)
      series(2) = generateNumberInScope(0, 255)

      for (i <- 0 until nestedScopes) {
        val start = IP(series(0), series(1), series(2), generateNumberInScope(0, 255))
        val end = IpProcessor.increase(start, generateNumberInScope(1, numberScope))
        ipScopes += IpScope(start, end)
      }
    }

    ipScopes.toList
  }

  def generateNumberInScope(min: Int, max: Int): Int = {
    min + Random.nextInt(max - min + 1)
  }

}
