package team.vaevictis.subsystems.maxswerve

import com.revrobotics.spark.config.ClosedLoopConfig
import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig

/**
 * SparkMaxConfigs for angle and speed motors
 */
object MaxSwerveMotorConfigs {

    val speedConfig: SparkMaxConfig = SparkMaxConfig();
    val angleConfig: SparkMaxConfig = SparkMaxConfig();

    init {

        speedConfig
            .idleMode(SparkBaseConfig.IdleMode.kBrake)
            .smartCurrentLimit(50);
        speedConfig.encoder
            .positionConversionFactor(MaxSwerveConstants.WheelConstants.ROTATION_TO_METERS)
            .velocityConversionFactor(MaxSwerveConstants.WheelConstants.ROTATION_TO_METERS / 60.0);
        speedConfig.closedLoop
            .feedbackSensor(ClosedLoopConfig.FeedbackSensor.kPrimaryEncoder);

        angleConfig
            .idleMode(SparkBaseConfig.IdleMode.kBrake)
            .smartCurrentLimit(50);
        angleConfig.absoluteEncoder
            .inverted(true)
            .positionConversionFactor(MaxSwerveConstants.WheelConstants.TURNING_FACTOR)
            .velocityConversionFactor(MaxSwerveConstants.WheelConstants.TURNING_FACTOR / 60.0);
        angleConfig.closedLoop
            .feedbackSensor(ClosedLoopConfig.FeedbackSensor.kAbsoluteEncoder)
            .pid(1.75, 0.0, 0.0)
            .outputRange(-1.0, 1.0)
            .positionWrappingEnabled(true)
            .positionWrappingInputRange(0.0, MaxSwerveConstants.WheelConstants.TURNING_FACTOR);

    }

}