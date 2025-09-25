package team.vaevictis.pathplanner

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.commands.PathPlannerAuto
import com.pathplanner.lib.config.PIDConstants
import com.pathplanner.lib.config.RobotConfig
import com.pathplanner.lib.controllers.PPHolonomicDriveController
import com.pathplanner.lib.path.PathPlannerPath
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import team.vaevictis.subsystems.HolonomicDriveSubsystem

/**
 * PathPlanner utilities
 */
class PathPlanner {

    val autoChooser: SendableChooser<Command> = AutoBuilder.buildAutoChooser()

    val config: RobotConfig = RobotConfig.fromGUISettings()

    init {

        SmartDashboard.putData("Auto Chooser", autoChooser)

    }

    /**
     * Chosen auto command from Pathplanner auto chooser
     */
    val autonomousCommand: Command get() = autoChooser.selected

    /**
     * Configure the PathPlanner AutoBuilder for a holonomic drive
     * @param holonomicDrive Holonomic Drive Subsystem Object
     */
    fun configureHolonomic(holonomicDrive: HolonomicDriveSubsystem) {
        AutoBuilder.configure(
            holonomicDrive::pose::get,
            holonomicDrive::resetPose,
            holonomicDrive::currentSpeeds::get,
            holonomicDrive::driveChassisSpeeds,
            PPHolonomicDriveController(
                PIDConstants(5.0, 0.0, 0.0),
                PIDConstants(5.0, 0.0, 0.0),
            ),
            config,
            {
                val alliance = DriverStation.getAlliance()
                if (alliance.isPresent) {
                    alliance.get() == DriverStation.Alliance.Red
                } else {
                    false
                }
            },
            holonomicDrive
        )
    }

    /**
     * Create a command to follow a PathPlanner path
     * @param pathFilename Name of the path file
     */
    fun follow(pathFilename: String): Command {
        try {
            val path: PathPlannerPath = PathPlannerPath.fromPathFile(pathFilename)
            return AutoBuilder.followPath(path)
        } catch(e: Exception) {
            println("PathPlanner Exception! ${e.message}")
            return Commands.none()
        }
    }

    /**
     * Create a command to follow a full Autonomous routine
     * @param autoFilename Name of the auto file
     */
    fun auto(autoFilename: String): Command = PathPlannerAuto(autoFilename)

}