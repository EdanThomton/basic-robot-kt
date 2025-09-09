package team.vaevictis.odometry;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

interface SwerveDriveOdometry {

    val gyro: Gyro;

    val pose: Pose2d;

    fun resetPose(newPose: Pose2d);

    fun update(modulePositions: Array<SwerveModulePosition>?);

}