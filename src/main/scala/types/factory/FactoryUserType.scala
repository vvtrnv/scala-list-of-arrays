package types.factory

import realization.types.usertypes.{IntegerUserType, Point2DUserType, UserType}

class FactoryUserType {
  private val listOfAvailableTypes : List[UserType] = List(new IntegerUserType, new Point2DUserType)

  /**
   * Получить объект желаемого ТД
   *
   * @param typename название ТД
   * @return
   */
  def getBuilderByTypeName(typename: String): UserType = {
    for (userType <- listOfAvailableTypes) {
      if (typename == (userType.typeName)) {
        return userType
      }
    }
    throw new RuntimeException("Ошибка. Тип данных не найден!")
  }

  /**
   * Получить список типов данных
   *
   * @return
   */
  def getTypeNameList: List[String] = {
    var list: List[String] = List()
    for (t <- listOfAvailableTypes) {
      list = list ++: List(t.typeName)
    }
    list
  }

}
