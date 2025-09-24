package team.vaevictis.subsystems.maxswerve

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.filter.SlewRateLimiter
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveModulePosition
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import team.vaevictis.networktables.NetworkTableInstanceKT
import team.vaevictis.networktables.NetworkTableKT
import team.vaevictis.odometry.SwerveDriveOdometry
import team.vaevictis.subsystems.HolonomicDriveSubsystem
import kotlin.math.abs
import kotlin.math.sign

/**
 * Swerve drive subsystem for a MaxSwerve chassis
 * Features:
 * - field-centric and robot-centric driving modes
 * - drive slew limiting
 * - automatic rotation correction
 * - efficient swerve updating
 */
class MaxSwerveDrive(
    private val config: MaxSwerveConfig,
    /// field-space odometry
    val odometry: SwerveDriveOdometry
): SubsystemBase(), HolonomicDriveSubsystem {

    private val table: NetworkTableKT = NetworkTableInstanceKT.DEFAULT["subsystems/MaxSwerveDrive"]
    private var swerveStatesNT    by table["swerveStates"].swerveModuleStateArray
    private var headingNT         by table["heading"].double
    private var poseNT            by table["pose"].pose2d
    private var targetedRotNT     by table["targetedRot"].double
    private var isFieldCentricNT  by table["isFieldCentric"].boolean
    private var isCorrectingRotNT by table["isCorrectingRot"].boolean
    private var isLimitingSlewNT  by table["isLimitingSlew"].boolean

    private val frontLeftWheel: MaxWheelModule = MaxWheelModule(config.frontLeft)
    private val frontRightWheel: MaxWheelModule = MaxWheelModule(config.frontRight)
    private val backLeftWheel: MaxWheelModule = MaxWheelModule(config.backLeft)
    private val backRightWheel: MaxWheelModule = MaxWheelModule(config.backRight)

    private var speeds: ChassisSpeeds = ChassisSpeeds(0.0, 0.0, 0.0)

    private var limitX: SlewRateLimiter = SlewRateLimiter(config.driverConfig.maxAccel)
    private var limitY: SlewRateLimiter = SlewRateLimiter(config.driverConfig.maxAccel)
    private var previousAngularVelocity: Double = 0.0
    private var limitSlew: Boolean = config.driverConfig.limitSlew

    private var targetRot: Double = 0.0
    private var rotController: PIDController = PIDController(config.driverConfig.baseCorrector, 0.0, 0.0)
    private var correctRot: Boolean = config.driverConfig.correctRot

    private var fieldCentric: Boolean = config.driverConfig.fieldCentric

    /**
     * Current Chassis Pose
     */
    override val pose: Pose2d
        get() = odometry.pose

    /**
     * Current Chassis Speeds
     */
    override val currentSpeeds: ChassisSpeeds
        get() = speeds

    // get module positions, if any are null returns null
    private val positions: Array<SwerveModulePosition>?
        get() {
            val flp = frontLeftWheel.position  ?: return null
            val frp = frontRightWheel.position ?: return null
            val blp = backLeftWheel.position   ?: return null
            val brp = backRightWheel.position  ?: return null

            return arrayOf(
                flp, frp, blp, brp
            )
        }

    /// Toggle Slew Limiting
    val toggleSlewLimiter: Command get() = runOnce {
        limitSlew = !limitSlew
        limitX.reset(0.0)
        limitY.reset(0.0)
    }
    /// Toggle Rotation Correction
    val toggleRotCorrection: Command get() = runOnce {
        correctRot = !correctRot
    }
    /// Toggle Fieldcentric Driving
    val toggleFieldcentric: Command get() = runOnce {
        fieldCentric = !fieldCentric
    }
    /// Set Fieldcentrism
    val resetFieldcentric: Command get() = runOnce {
        odometry.gyro.zeroHeading()
        targetRot = odometry.gyro.heading.radians
    }

    /**
     * Reset current pose to a known pose
     * @param newPose New active robot pose
     */
    override fun resetPose(newPose: Pose2d) {
        odometry.resetPose(newPose)
    }

    /**
     * Explicit drive MaxSwerve
     * @param x Normalized horizontal velocity
     * @param y Normalized vertical velocity
     * @param rot Normalized rotational velocity
     */
    override fun drive(x: Double, y: Double, rot: Double) {
        var x = x
        var y = y
        var rot = rot
        if(limitSlew) {
            x = limitX.calculate(x)
            y = limitY.calculate(y)
        }

        // cap out rotational acceleration at a specific acceleration
        val rotAccel = rot - previousAngularVelocity
        if(abs(rotAccel) > config.driverConfig.maxAngularAccel) {
            rot = previousAngularVelocity + config.driverConfig.maxAngularAccel * sign(rotAccel)
        }
        previousAngularVelocity = rot

        val xSpeed = x * config.driverConfig.maxSpeedMPS
        val ySpeed = y * config.driverConfig.maxSpeedMPS
        var rotSpeed = rot * config.driverConfig.maxAngularSpeed

        val ally = DriverStation.getAlliance()

        // if correctRot is enabled, use rotation speed to modify a set rotation that the robot tries to match
        // otherwise, directly update the robot's rotation
        if(correctRot) {
            targetRot += rotSpeed

            rotSpeed = rotController.calculate(odometry.gyro.heading.radians, targetRot)
        } else {
            targetRot = odometry.gyro.heading.radians
        }

        if(fieldCentric) {
            if(ally.get() == Alliance.Red) {
                speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                    xSpeed,
                    ySpeed,
                    rotSpeed,
                    odometry.gyro.heading.plus(Rotation2d(Math.PI))
                )
            } else {
                speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                    xSpeed,
                    ySpeed,
                    rotSpeed,
                    odometry.gyro.heading
                )
            }
        } else {
            speeds = ChassisSpeeds(xSpeed, ySpeed, rotSpeed)
        }

        driveChassisSpeeds(speeds)
    }

    /**
     * Drive with pre-given chassis speeds
     * @param speeds Chassis speeds to drive with
     */
    override fun driveChassisSpeeds(speeds: ChassisSpeeds) {
        val discreteSpeeds = ChassisSpeeds.discretize(speeds, 0.02)

        // desaturate mutates wheelStates, hence why it is var (kotlin doesn't recognize the mutation)
        var wheelStates: Array<SwerveModuleState> = config.kinematics.toSwerveModuleStates(discreteSpeeds)
        SwerveDriveKinematics.desaturateWheelSpeeds(
            wheelStates,
            config.driverConfig.maxAccel
        )

        frontLeftWheel.state = wheelStates[0]
        frontRightWheel.state = wheelStates[1]
        backLeftWheel.state = wheelStates[2]
        backRightWheel.state = wheelStates[3]
    }

    /**
     * Subsystem periodic function
     */
    override fun periodic() {

        // update odometry
        odometry.update(positions)

        // networktable fun!
        swerveStatesNT = arrayOf(
            frontLeftWheel.state,
            frontRightWheel.state,
            backLeftWheel.state,
            backRightWheel.state
        )

        headingNT = odometry.gyro.heading.radians
        poseNT = odometry.pose

        targetedRotNT = targetRot

        isFieldCentricNT = fieldCentric
        isCorrectingRotNT = correctRot
        isLimitingSlewNT = limitSlew
    }
}