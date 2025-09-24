package frc.robot.subsystems.LED

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj.motorcontrol.Spark
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase

/**
 * Rev blinkin LED Subsystem
 */
class LEDSubsystem: SubsystemBase() {

    private val blinkin: Spark = Spark(0)

    var color: LEDColor = LEDColor.RAINBOW
    var priority: Boolean = false;

    override fun periodic() {
        try {
            blinkin.set(color.pwmColor)
        } catch(e: Exception) {
            println("Error! ${e.stackTrace}")
        }
    }

    fun allianceColor() {
        if(DriverStation.getAlliance().isPresent) {
            if(DriverStation.getAlliance().get() == Alliance.Blue) {
                color = LEDColor.BLUE
            } else {
                color = LEDColor.RED
            }
        } else {
            color = LEDColor.WHITE
        }
        priority = false
    }

    fun aligningColor() {
        color = LEDColor.GOLD
        priority = true
    }

    fun alignedColor() {
        color = LEDColor.STROBE_GOLD
        priority = true
    }

    fun nonMovingColor() {
        color = LEDColor.RAINBOW
        priority = true
    }

    fun coralInColor() {
        if(!priority) color = LEDColor.GREEN
    }

    fun resetColor() {
        if(!priority) allianceColor()
    }

    val nonMovingColorCommand: Command get() = runOnce { nonMovingColor() }
    val allianceColorCommand: Command get() = runOnce { allianceColor() }

}