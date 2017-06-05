package edu.eckerd.gpa
package grades

sealed trait LetterGrade

object LetterGrade {
  object A extends LetterGrade
  object B extends LetterGrade
  object C extends LetterGrade
  object D extends LetterGrade
  object F extends LetterGrade
}
