package team.vaevictis.subsystems

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.kinematics.ChassisSpeeds

interface DriveSubsystem {

    val pose: Pose2d;
    val currentSpeeds: ChassisSpeeds;

    fun resetPose(newPose: Pose2d);
    fun drive(x: Double, y: Double, rot: Double);
    fun driveChassisSpeeds(speeds: ChassisSpeeds);



}