import sbt._
import Keys._

object OptimizersBuild extends Build {
   val scCore = ProjectRef(file("../signal-collect"), id = "signal-collect")
   val scSlurm = ProjectRef(file("../signal-collect-slurm"), id = "signal-collect-slurm")
   val scTorque = ProjectRef(file("../signal-collect-torque"), id = "signal-collect-torque")
   val scDcopAlgs = Project(id = "dcop-algorithms", base = file(".")) dependsOn(scCore, scSlurm, scTorque)
}
