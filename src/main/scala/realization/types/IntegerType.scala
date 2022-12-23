package realization.types

class IntegerType {
  private var value = 0

  def this(value: Int) {
    this()
    this.value = value
  }

  def getIntValue: Int = this.value

  def setIntValue(value: Int): Unit = {
    this.value = value
  }

  override def toString: String = String.valueOf(value)
}
