package edu.eckerd.gpa
package grades

sealed trait GradeModification

object GradeModification {
  object `+` extends GradeModification
  object `-` extends GradeModification
}
