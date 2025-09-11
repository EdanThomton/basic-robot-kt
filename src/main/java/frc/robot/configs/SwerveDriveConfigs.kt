package frc.robot.configs

import edu.wpi.first.math.geometry.Translation2d
import team.vaevictis.subsystems.maxswerve.MaxSwerveConfig
import team.vaevictis.subsystems.maxswerve.MaxSwerveDriverConfig
import team.vaevictis.subsystems.maxswerve.MaxWheelConfig

/**
 * Active configs for a MaxSwerve chassis
 */
object SwerveDriveConfigs {

    const val WHEEL_POS = 0.33655

    val SWERVE_CONFIG = MaxSwerveConfig(
        MaxWheelConfig(
            4, 3,
            0.00040465, 0.0, 0.0,
            0.21461, 2.8012, 0.7612,
            3.43 + Math.PI,
            Translation2d(WHEEL_POS, WHEEL_POS)
        ),
        MaxWheelConfig(
            6, 5,
            0.00091514, 0.0, 0.0,
            0.34839, 2.7542, 0.55833,
            0.7968 + Math.PI,
            Translation2d(WHEEL_POS, -WHEEL_POS)
        ),
        MaxWheelConfig(
            2, 1,
            0.0015266, 0.0, 0.0,
            0.19566, 2.6945, 0.57784,
            2.1945 + Math.PI,
            Translation2d(-WHEEL_POS, WHEEL_POS)
        ),
        MaxWheelConfig(
            8, 7,
            0.0010266, 0.0, 0.0,
            0.23609, 2.7405, 0.56268,
            1.042 + Math.PI,
            Translation2d(-WHEEL_POS, -WHEEL_POS)
        ),
        MaxSwerveDriverConfig(
            2.5,
            4.8,
            0.075,
            2.0 * Math.PI,
            Math.PI,
            0.16,
            0.04,
            limitSlew = false,
            correctRot = false,
            fieldCentric = false
        )
    )

}