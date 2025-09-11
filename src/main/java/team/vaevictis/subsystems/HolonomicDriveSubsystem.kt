package team.vaevictis.subsystems

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.wpilibj2.command.Subsystem

interface HolonomicDriveSubsystem: Subsystem {

    val pose: Pose2d
    val currentSpeeds: ChassisSpeeds

    fun resetPose(newPose: Pose2d)
    fun drive(x: Double, y: Double, rot: Double)
    fun driveChassisSpeeds(speeds: ChassisSpeeds)

}