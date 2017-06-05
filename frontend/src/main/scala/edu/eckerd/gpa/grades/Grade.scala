package edu.eckerd.gpa
package grades


final case class Grade(letterGrade: LetterGrade, mod: Option[GradeModification]) {
  lazy val gpa = Grade.gpa(this)

}

object Grade {

  def gpa(grade: Grade): Double = {
    import LetterGrade._
    import GradeModification._

    (grade.letterGrade, grade.mod) match {
      case (A, Some(`+`)) => 4.0
      case (A, None) => 4.0
      case (A, Some(`-`)) => 3.7
      case (B, Some(`+`)) => 3.3
      case (B, None) => 3.0
      case (B, Some(`-`)) => 2.7
      case (C, Some(`+`)) => 2.3
      case (C, None) => 2.0
      case (C, Some(`-`)) => 1.7
      case (D, Some(`+`)) => 1.3
      case (D, None) => 1.0
      case (D, Some(`-`)) => 0.7
      case (F, _) => 0.0
    }
  }
}
