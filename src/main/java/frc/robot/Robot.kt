package frc.robot

import edu.wpi.first.hal.FRCNetComm.tInstances
import edu.wpi.first.hal.FRCNetComm.tResourceType
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.util.WPILibVersion
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler

/**
 * Base Robot Class, handles command/autonomous scheduling as well as container initialization
 */
object Robot : TimedRobot() {
    //private var autonomousCommand: Command = Autos.defaultAutonomousCommand

    init {
        HAL.report(
            tResourceType.kResourceType_Language,
            tInstances.kLanguage_Kotlin,
            0,
            WPILibVersion.Version
        );
        RobotContainer;
    }

    override fun robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    override fun autonomousInit() {
        // TODO: add autonomous command initializer
    }

    override fun teleopInit() {
        //autonomousCommand.cancel()
    }

    override fun testInit() {
        CommandScheduler.getInstance().cancelAll();
    }
}