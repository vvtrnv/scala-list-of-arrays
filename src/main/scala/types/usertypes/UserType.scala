package types.usertypes

import realization.types.comporators.Comporator

import java.io.{IOException, InputStreamReader}

trait UserType {

  /**
   * Получить название ТД
   *
   * @return
   */
  def typeName: String

  /**
   * Создать объект ТД
   *
   * @return
   */
  def create: AnyRef


  /**
   * Создать копию объекта
   *
   * @param object
   * @return
   */
  def clone(`object`: Any): Any

  /**
   * Чтение экземпляра с потока
   *
   * @param in
   * @return
   * @throws IOException
   */
  @throws[IOException]
  def readValue(in: InputStreamReader): Any

  /**
   * Парсить значение со строки
   *
   * @param ss
   * @return
   */
  def parseValue(ss: String): AnyRef

  /**
   * Сравнение
   *
   * @return
   */
  def getTypeComparator: Comporator

  def toString(`object`: Any): String
}
