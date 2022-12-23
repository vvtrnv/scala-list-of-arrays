package realization.types.usertypes

import realization.types.Point2DType
import realization.types.comporators.{Comporator, Point2DComporator}

import java.io.{IOException, InputStreamReader}
import java.util.Random
import java.util.regex.{Matcher, Pattern}

class Point2DUserType extends UserType {
  private val REG_EXPR = "\\(([-]?[0-9]+(?:[.,][0-9]+){0,1});([-]?[0-9]+(?:[.,][0-9]+){0,1})\\)"

  override def typeName = "Point2D"

  override def create: AnyRef = {
    val rand = new Random
    new Point2DType(rand.nextFloat(1000.0F), rand.nextFloat(1000.0F))
  }

  override def clone(`object`: Any): Any = {
    val newObj = new Point2DType(`object`.asInstanceOf[Point2DType].getX, `object`.asInstanceOf[Point2DType].getY)
    newObj
  }


  @throws[IOException]
  override def readValue(in: InputStreamReader): Any = in.read

  override def parseValue(ss: String): AnyRef = {
    val patternString = Pattern.compile(REG_EXPR)
    val matcher = patternString.matcher(ss)
    if (matcher.find) {
      val pointType = new Point2DType((matcher.group(1)).toDouble, (matcher.group(2)).toDouble)
      return pointType
    }
    else {
      return null
    }
  }

  override def getTypeComparator = new Point2DComporator

  override def toString(`object`: Any): String = `object`.toString
}
