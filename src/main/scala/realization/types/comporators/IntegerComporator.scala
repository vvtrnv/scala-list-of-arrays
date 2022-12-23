package realization.types.comporators

import realization.types.IntegerType

class IntegerComporator extends Comporator {
  override def compare(object1: Any, object2: Any): Double = object1.asInstanceOf[IntegerType].getIntValue - object2.asInstanceOf[IntegerType].getIntValue
}
