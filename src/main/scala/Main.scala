import data_model.{IP, IpScope, IpScopeUnformatted}
import generator.DataGenerator
import org.apache.spark.sql.{SaveMode, SparkSession}
import processor.{Comparator, FileProcessor, IpIntersection, IpProcessor}

object Main {

  def main(args: Array[String]): Unit = {

    val scopes = DataGenerator.generateIpScopes(3, 3, 100)
    val sortedScopes = scopes.sortBy(ipScope => ipScope.start)

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("Albacross")
      .getOrCreate()

    val postgres = spark.read
      .format("jdbc")
      .option("url", "jdbc:postgresql://localhost:5432/ab-cross")
      .option("dbtable", "ip_scope")
      .option("user", "postgres")
      .option("password", "postgres")
      .load()

    val unformattedScopes = sortedScopes.map(scope => IpScopeUnformatted(scope.start.toString, scope.end.toString))
    val rdd = spark.sparkContext.parallelize(unformattedScopes)

    val df2 = spark.createDataFrame(rdd.collect()).toDF("start_ip", "end_ip")

    df2.write
      .mode(SaveMode.Append)
      .format("jdbc")
      .option("url", "jdbc:postgresql://localhost:5432/ab-cross")
      .option("dbtable", "ip_scope")
      .option("user", "postgres")
      .option("password", "postgres")
      .save()

    postgres.createOrReplaceTempView("ip_scope")
    val df3 = spark.sql("SELECT * FROM ip_scope")

    val rdd1 = df3.rdd.map(e => IpScopeUnformatted(e.getString(1), e.getString(2)))

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

    FileProcessor.writeScopes(System.getProperty("user.dir") + "/src/main/resources/output.txt", ipScopes)
  }

}
