package types.usertypes

import realization.types.IntegerType
import realization.types.comporators.{Comporator, IntegerComporator}

import java.io.{IOException, InputStreamReader}
import java.util.Random

class IntegerUserType extends UserType {
  override def typeName = "Integer"

  override def create: AnyRef = {
    val rand = new Random
    new IntegerType(rand.nextInt(1000))
  }

  override def clone(`object`: Any) = new IntegerType(`object`.asInstanceOf[IntegerType].getIntValue)

  @throws[IOException]
  override def readValue(in: InputStreamReader): Any = in.read

  override def parseValue(ss: String) = new IntegerType(ss.toInt)

  override def getTypeComparator = new IntegerComporator

  override def toString(`object`: Any): String = `object`.toString
}
