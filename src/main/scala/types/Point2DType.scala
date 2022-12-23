package types

class Point2DType {
  private[types] var x: Double = .0
  private[types] var y: Double = .0

  def this(x: Double, y: Double) {
    this()
    this.x = x
    this.y = y
  }

  def getX: Double = this.x

  def getY: Double = this.y

  def setPoint2DValue(x: Double, y: Double): Unit = {
    this.x = x
    this.y = y
  }

  override def toString: String = "(" + String.valueOf(this.x) + ";" + String.valueOf(this.y) + ")"
}
