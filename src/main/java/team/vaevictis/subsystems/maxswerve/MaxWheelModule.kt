package team.vaevictis.subsystems.maxswerve;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase

/**
 * Single MaxSwerve Module
 */
class MaxWheelModule(
    val config: MaxWheelConfig
): SubsystemBase() {

    val speedMotor: SparkMax = SparkMax(config.speedID, MotorType.kBrushless);
    val angleMotor: SparkMax = SparkMax(config.angleID, MotorType.kBrushless);

    val speedEncoder: RelativeEncoder = speedMotor.encoder;
    val angleEncoder: AbsoluteEncoder = angleMotor.absoluteEncoder;

    val speedController: SparkClosedLoopController = speedMotor.closedLoopController;
    val angleController: SparkClosedLoopController = angleMotor.closedLoopController;

    val speedFeedforward: SimpleMotorFeedforward = SimpleMotorFeedforward(config.ks, config.kv, config.ka);

    init {
        val speedConfig: SparkMaxConfig = MaxSwerveMotorConfigs.speedConfig;
        val angleConfig: SparkMaxConfig = MaxSwerveMotorConfigs.angleConfig;

        speedConfig.closedLoop.pid(config.p, config.i, config.d);

        speedMotor.configure(speedConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        angleMotor.configure(angleConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        speedEncoder.position = 0.0;
    }

    /// SwerveModulePosition getter. Nullable in case of motor errors
    val position: SwerveModulePosition?
        get() {
            // enforce motor safety (don't try to drive if the motors aren't working)
            if(!checkErr()) {
                return null;
            }

            val pos: Double = speedEncoder.position;
            val angle: Rotation2d = Rotation2d(angleEncoder.position - config.chassisAngleOffset);

            return SwerveModulePosition(pos, angle);
        }

    /**
     * Set and get the current state of the wheel
     */
    var state: SwerveModuleState
        get() = SwerveModuleState(
            speedEncoder.velocity,
            Rotation2d(angleEncoder.position - config.chassisAngleOffset)
        )
        set(value) {
            value.angle = value.angle.plus(Rotation2d.fromRadians(config.chassisAngleOffset));
            value.optimize(Rotation2d(angleEncoder.position));

            setWheelPosition(value.speedMetersPerSecond, value.angle.radians);
        }

    private fun setWheelPosition(speed: Double, angle: Double) {
        var realSpeed: Double = speed;
        if(Math.abs(speed) < 0.1) {
            realSpeed = 0.1;
        }

        speedController.setReference(
            realSpeed,
            ControlType.kVelocity,
            ClosedLoopSlot.kSlot0,
            speedFeedforward.calculate(realSpeed)
        );
        angleController.setReference(
            angle,
            ControlType.kPosition
        );
    }

    /// Check for motor errors (could be connection issues/motor issues)
    private fun checkErr(): Boolean {
        if (speedMotor.getLastError() != REVLibError.kOk) {
            println("Error with speed motor: " + speedMotor.getDeviceId());
            println("Error: " + speedMotor.getLastError());
            speedMotor.clearFaults();
            return false;
        } else if (angleMotor.getLastError() != REVLibError.kOk) {
            println("Error with angle motor: " + angleMotor.getDeviceId());
            println("Error: " + angleMotor.getLastError());
            angleMotor.clearFaults();
            return false;
        }
        return true;
    }

}