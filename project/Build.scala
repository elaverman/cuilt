import sbt._
import Keys._

object OptimizersBuild extends Build {
   val scCore = ProjectRef(file("../signal-collect"), id = "signal-collect")
   val scDcopAlgs = Project(id = "dcop-algorithms", base = file(".")) dependsOn(scCore)
}
