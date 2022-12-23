package list

import realization.types.usertypes.UserType

import java.io.{BufferedReader, FileNotFoundException, FileReader, PrintWriter}
import java.util
import java.util.{Arrays, Objects}

class SerializeList {
  private def parseString(list: MyListOfArrays, s: String, userType: UserType): Unit = {
    var str: String = s

    str = str.replace("[", "")
    str = str.replace("]", "")
    val arrayFromString: Array[String] = str.split(", ")
    for (elem <- arrayFromString) {
      if (!Objects.equals(elem, "null"))
        list.add(userType.parseValue(elem))
    }
  }

  def saveToFile(list: MyListOfArrays, filename: String, userType: UserType): Unit = {
    try {
      val writer = new PrintWriter(filename)
      try {
        writer.println(userType.typeName)
        writer.println(list.getSizeOfArrays)
        list.forEach((el: Any) => writer.println(util.Arrays.toString(el.asInstanceOf[Array[AnyRef]]))) // todo
      } catch {
        case e: FileNotFoundException =>
          throw new RuntimeException(e)
      } finally if (writer != null) writer.close()
    }
  }

  @throws[Exception]
  def loadFromFile(filename: String, userType: UserType): MyListOfArrays = try {
    val bufferedReader = new BufferedReader(new FileReader(filename))
    try {
      var line: String = null
      var sizeOfArrays = 0
      line = bufferedReader.readLine
      if (!(userType.typeName == line)) throw new Exception("Wrong file structure")
      line = bufferedReader.readLine
      sizeOfArrays = line.toInt

      val list = new MyListOfArrays(Math.pow(sizeOfArrays, 2).toInt)

      line = bufferedReader.readLine
      while (line != null) {
        parseString(list, line, userType)
        line = bufferedReader.readLine
      }

      bufferedReader.close()

      list
    } catch {
      case e: FileNotFoundException =>
        throw new FileNotFoundException
    }
  }
}
