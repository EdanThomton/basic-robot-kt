package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command

/**
 * Example 1s waiting command
 */
class ExampleCommand: Command() {

    var count = 0

    override fun initialize() {
        count = 0
        println("Example Command Started")
    }

    override fun execute() {
        count++
    }

    override fun end(interrupted: Boolean) {
        println("Example Command Ended")
    }

    override fun isFinished(): Boolean = count > 50

}