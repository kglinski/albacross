import java.io.{BufferedWriter, FileWriter}

import data_model.{IP, IpScope, IpScopeUnformatted}
import org.apache.spark.sql.SparkSession
import processor.{Comparator, IpIntersection, IpProcessor}

object Main {

  def main(args: Array[String]): Unit = {

    val filePath = System.getProperty("user.dir") + "/src/main/resources/ip_script.txt"

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("Albacross")
      .getOrCreate()

    val rdd = spark.sparkContext.textFile(filePath)

    val rdd1 = rdd.map(text => {
      val extracted = text.split(",")
      IpScopeUnformatted(extracted(0), extracted(1).trim)
    })

    val rdd2 = rdd1.map(ipScopeUnformatted =>
      IpScope(
        IP(ipScopeUnformatted.start.split("\\.").map(_.toInt): _*),
        IP(ipScopeUnformatted.end.split("\\.").map(_.toInt): _*)
      ))

    val rdd3 = rdd2.sortBy(ipScope => ipScope.start)
    val rdd4 = rdd3.map(ipScope => IpProcessor.generateIpRangeFromScope(ipScope))

    rdd4.collect().foreach(ipRange => {
      rdd4
        .filter(ips => !ips.equals(ipRange))
        .filter(ips => Comparator.greaterOrEqual(ips.last, ipRange.head))
        .filter(ips => Comparator.lessOrEqual(ips.head, ipRange.last))
        .flatMap(ips => ipRange.intersect(ips))
        .foreach(ip => IpIntersection.add(ip))
    })

    val rdd5 = spark.sparkContext.parallelize(IpIntersection.intersected())
    val rdd6 = rdd4.flatMap(x => x).subtract(rdd5)

    val rdd7 = rdd6.sortBy(ip => ip)

    val ipScopes = IpProcessor.assignScopes(rdd7.collect())

    val writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/src/main/resources/output.txt"))
    for (scope <- ipScopes) {
      writer.write(scope.toString)
      writer.newLine()
    }
    writer.close()
  }

}
