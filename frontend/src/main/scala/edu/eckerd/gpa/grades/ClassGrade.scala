package edu.eckerd.gpa
package grades

final case class ClassGrade(name: String, grade: Grade, creditHours: Double) {

  lazy val qualityPoints: Double = ClassGrade.calculateQualityPoints(grade, creditHours)

}

object ClassGrade {

  def calculateQualityPoints(grade: Grade, creditHours: Double): Double =
    grade.gpa * creditHours

}
