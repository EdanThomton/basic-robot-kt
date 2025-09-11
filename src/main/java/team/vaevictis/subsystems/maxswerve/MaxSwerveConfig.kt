package team.vaevictis.subsystems.maxswerve

import edu.wpi.first.math.kinematics.SwerveDriveKinematics

/**
 * Overall config for MaxSwerve chassis
 */
class MaxSwerveConfig(
    val frontLeft: MaxWheelConfig,
    val frontRight: MaxWheelConfig,
    val backLeft: MaxWheelConfig,
    val backRight: MaxWheelConfig,
    val driverConfig: MaxSwerveDriverConfig
){
    val kinematics: SwerveDriveKinematics = SwerveDriveKinematics(
        frontLeft.chassisLocation,
        frontRight.chassisLocation,
        backLeft.chassisLocation,
        backRight.chassisLocation
    )
}

/**
 * Driver config for MaxSwerve chassis
 */
data class MaxSwerveDriverConfig(
    val maxAccel: Double,
    val maxSpeedMPS: Double,
    val maxAngularAccel: Double,
    val maxAngularSpeed: Double,
    val maxCorrectiveAngularSpeed: Double,
    val correctiveFactor: Double,
    val baseCorrector: Double,
    val limitSlew: Boolean,
    val correctRot: Boolean,
    val fieldCentric: Boolean
)