package processor

import java.io.{BufferedWriter, FileWriter}
import data_model.IpScope

object FileProcessor {

  def writeScopes(filePath: String, ipScopes: List[IpScope]): Unit = {
    val writer = new BufferedWriter(new FileWriter(filePath))
    for (scope <- ipScopes) {
      writer.write(scope.toString)
      writer.newLine()
    }
    writer.close()
  }

}
