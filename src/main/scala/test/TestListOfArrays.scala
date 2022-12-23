package test

import realization.list.{MyListOfArrays, SerializeList}
import realization.types.factory.FactoryUserType
import realization.types.usertypes.{IntegerUserType, Point2DUserType, UserType}

import java.io.FileNotFoundException

class TestListOfArrays {
  private var factoryUserType: FactoryUserType = null
  private var userType: UserType = null
  private var list: MyListOfArrays = null
  private var workWithFiles: SerializeList = new SerializeList();

  private val INTEGER_FILENAME = "testInteger.dat"
  private val POINT2D_FILENAME = "testPoint2D.dat"

  private def startTest(): Unit = {
    System.out.println("### FILL LIST (ADD METHOD)")
    for (i <- 0 until 120) {
      list.add(userType.create)
    }
    System.out.println("### PRINT LIST")
    list.show()
    System.out.println("TOTAL ELEMENTS = " + list.getTotalElements)
    System.out.println("\n### INSERT ELEMENT")
    System.out.println("ELEMENT ON 77 INDEX BEFORE INSERT: " + list.get(77))
    list.insert(userType.create, 77)
    System.out.println("ELEMENT ON 77 INDEX AFTER INSERT: " + list.get(77))
    System.out.println("\n### INSERT 20 ELEMENTS ON INDEX 100")
    for (i <- 0 until 20) {
      list.insert(userType.create, 100)
    }
    System.out.println("### PRINT LIST")
    list.show()
    System.out.println("TOTAL ELEMENTS = " + list.getTotalElements)
    System.out.println("\n### REMOVE ELEMENT ON INDEX")
    System.out.println("ELEMENT ON 77 INDEX BEFORE REMOVE: " + list.get(77))
    list.remove(77)
    System.out.println("ELEMENT ON 77 INDEX AFTER REMOVE: " + list.get(77))
    System.out.println("\n### REMOVE 20 ELEMENTS ON INDEX 50")
    for (i <- 0 until 20) {
      list.remove(50)
    }
    System.out.println("### PRINT LIST")
    list.show()
    System.out.println("TOTAL ELEMENTS = " + list.getTotalElements)
    if (userType.isInstanceOf[IntegerUserType]) {
      System.out.println("### SAVE TO FILE " + INTEGER_FILENAME)
      workWithFiles.saveToFile(list, INTEGER_FILENAME, userType)
    }
    else if (userType.isInstanceOf[Point2DUserType]) {
      System.out.println("### SAVE TO FILE " + POINT2D_FILENAME)
      workWithFiles.saveToFile(list, POINT2D_FILENAME, userType)
    }
    System.out.println("\n### SORT LIST (IMPERATIVE)")
//    list = list.sortFuncStyle(userType.getTypeComparator)
    list = list.sort(userType.getTypeComparator)
    System.out.println("### PRINT LIST")
    list.show()
    System.out.println("TOTAL ELEMENTS = " + list.getTotalElements)

    if (userType.isInstanceOf[IntegerUserType]) try {
      System.out.println("\n~~ LOAD LIST FROM FILE " + INTEGER_FILENAME + " ~~")
      list.clear()
      list = workWithFiles.loadFromFile(INTEGER_FILENAME, userType)
      System.out.println("### PRINT LIST")
      list.show()
      System.out.println("TOTAL ELEMENTS = " + list.getTotalElements)
    } catch {
      case ex: FileNotFoundException =>
        System.out.println("Ошибка. Файл не найден!")
      case ex: Exception =>
        System.out.println("Ошибка. Структура файла неверна или повреждена!")
    }
    else if (userType.isInstanceOf[Point2DUserType]) try {
      System.out.println("\n~~ LOAD LIST FROM FILE " + POINT2D_FILENAME + " ~~")
      list.clear()
      list = workWithFiles.loadFromFile(POINT2D_FILENAME, userType)
      System.out.println("### PRINT LIST")
      list.show()
      System.out.println("TOTAL ELEMENTS = " + list.getTotalElements)
    } catch {
      case ex: FileNotFoundException =>
        System.out.println("Ошибка. Файл не найден!")
      case ex: Exception =>
        System.out.println("Ошибка. Структура файла неверна или повреждена!")
    }

    System.out.println("\n### SORT LIST (FUNCTIONAL)")
    list = list.sortFuncStyle(userType.getTypeComparator)
    System.out.println("### PRINT LIST")
    list.show()
    System.out.println("TOTAL ELEMENTS = " + list.getTotalElements)
  }

  def testingInteger: Int = {
    System.out.println("#### TEST USER TYPE INTEGER\n")
    factoryUserType = new FactoryUserType
    userType = factoryUserType.getBuilderByTypeName("Integer")
    list = new MyListOfArrays(100)
    startTest()
    1
  }

  def testingPoint2D: Int = {
    System.out.println("#### TEST USER TYPE POINT2D\n")
    factoryUserType = new FactoryUserType
    userType = factoryUserType.getBuilderByTypeName("Point2D")
    list = new MyListOfArrays(100)
    startTest()
    1
  }
}
