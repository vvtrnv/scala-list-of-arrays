package realization.types.comporators

import realization.types.Point2DType

class Point2DComporator extends Comporator {
  private def getLengthOfVector(x: Double, y: Double) = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)).toDouble

  override def compare(object1: Any, object2: Any): Double = {
    val obj1_x = object1.asInstanceOf[Point2DType].getX
    val obj2_x = object2.asInstanceOf[Point2DType].getX
    val obj1_y = object1.asInstanceOf[Point2DType].getY
    val obj2_y = object2.asInstanceOf[Point2DType].getY
    getLengthOfVector(obj1_x, obj1_y) - getLengthOfVector(obj2_x, obj2_y)
  }
}
