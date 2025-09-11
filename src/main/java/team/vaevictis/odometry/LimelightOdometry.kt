package team.vaevictis.odometry

import edu.wpi.first.math.VecBuilder
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveModulePosition
import edu.wpi.first.math.util.Units
import edu.wpi.first.wpilibj.DriverStation
import team.vaevictis.odometry.limelight.LimelightHelpers

/**
 * Field-space odometry using limelight april-tag measurements
 */
class LimelightOdometry(
    override val gyro: Gyro,
    private var modulePositions: Array<SwerveModulePosition>,
    swerveKinematics: SwerveDriveKinematics
): SwerveDriveOdometry {

    private var internalPose: Pose2d = Pose2d()
    private val poseEstimator: SwerveDrivePoseEstimator = SwerveDrivePoseEstimator(
        swerveKinematics,
        Rotation2d(0.0),
        modulePositions,
        internalPose,
        VecBuilder.fill(0.05, 0.05, Units.degreesToRadians(5.0)),
        VecBuilder.fill(0.5, 0.5, Units.degreesToRadians(30.0))
    )

    init {
        gyro.zeroHeading()
    }

    /// field-space pose
    override val pose: Pose2d
        get() = poseEstimator.estimatedPosition

    /**
     * Reset pose to a given pose
     * @param newPose Updates pose
     */
    override fun resetPose(newPose: Pose2d) {
        gyro.setHeadingOffset(newPose.rotation)
        poseEstimator.resetPosition(gyro.heading, modulePositions, newPose)
    }

    /**
     * Update odometry with swerve data
     * @param modulePositions Array of updated module positions
     */
    override fun update(modulePositions: Array<SwerveModulePosition>?) {

        if(modulePositions != null) {
            this@LimelightOdometry.modulePositions = modulePositions
        }

        if(DriverStation.isEnabled()) {

            poseEstimator.update(gyro.heading, this@LimelightOdometry.modulePositions)

            LimelightHelpers.SetRobotOrientation(
                "limelight",
                gyro.heading.degrees,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0
            )

            val poseFromLLMT2April: LimelightHelpers.PoseEstimate =
                LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight") ?: return

            // ignore MT2 measurements when ambiguity is too high or there are no tags in sight
            if(poseFromLLMT2April.tagCount == 0) return
            if(poseFromLLMT2April.rawFiducials.size == 1 && poseFromLLMT2April.rawFiducials[0].ambiguity > 0.9) return

            poseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(0.7, 0.7, 9999999.9))
            poseEstimator.addVisionMeasurement(
                poseFromLLMT2April.pose,
                poseFromLLMT2April.timestampSeconds
            )

        }

    }

}