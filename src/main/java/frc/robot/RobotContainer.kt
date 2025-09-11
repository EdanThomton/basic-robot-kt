package frc.robot

import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.kinematics.SwerveModulePosition
import edu.wpi.first.wpilibj2.command.RunCommand
import edu.wpi.first.wpilibj2.command.button.CommandJoystick
import frc.robot.configs.SwerveDriveConfigs
import team.vaevictis.odometry.LimelightOdometry
import team.vaevictis.odometry.gyros.NavXGyro
import team.vaevictis.pathplanner.PathPlanner
import team.vaevictis.subsystems.maxswerve.MaxSwerveDrive
import kotlin.math.abs

/**
 * Overall robot container
 */
object RobotContainer {

    val navX: NavXGyro = NavXGyro()
    val odometry: LimelightOdometry = LimelightOdometry(
        navX,
        arrayOf(
            SwerveModulePosition(0.0, Rotation2d(0.0)),
            SwerveModulePosition(0.0, Rotation2d(0.0)),
            SwerveModulePosition(0.0, Rotation2d(0.0)),
            SwerveModulePosition(0.0, Rotation2d(0.0))
        ),
        SwerveDriveConfigs.SWERVE_CONFIG.kinematics
    )
    val maxSwerveDrive: MaxSwerveDrive = MaxSwerveDrive(SwerveDriveConfigs.SWERVE_CONFIG, odometry)

    val leftJoystick: CommandJoystick = CommandJoystick(0)
    val rightJoystick: CommandJoystick = CommandJoystick(1)
    val altJoystick: CommandJoystick = CommandJoystick(2)

    /**
     * Configure joystick bindings
     */
    init {

        // configure pathplanner with MaxSwerve
        PathPlanner.configureHolonomic(maxSwerveDrive)

        maxSwerveDrive.defaultCommand = RunCommand({
            maxSwerveDrive.drive(
                applyDeadband(leftJoystick.y, 0.05),
                applyDeadband(leftJoystick.x, 0.05),
                applyDeadband(-rightJoystick.x, 0.05)
            )
        },maxSwerveDrive)

        leftJoystick.button(1).onTrue(maxSwerveDrive.toggleFieldcentric)
        leftJoystick.button(2).onTrue(maxSwerveDrive.toggleRotCorrection)
        leftJoystick.button(3).onTrue(maxSwerveDrive.toggleSlewLimiter)
        rightJoystick.button(4).onTrue(maxSwerveDrive.resetFieldcentric)
    }

    /**
     * Apply a deadband zone to an input
     * @param input Input to apply deadband to
     * @param deadband deadband zone
     */
    private fun applyDeadband(input: Double, deadband: Double): Double {
        if(abs(input) < deadband) return 0.0
        return input
    }
}