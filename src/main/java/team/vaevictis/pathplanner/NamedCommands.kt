package team.vaevictis.pathplanner

import com.pathplanner.lib.auto.NamedCommands
import edu.wpi.first.wpilibj2.command.Command

/**
 * Basic syntax sugar over PathPlanner's NamedCommands
 */
object NamedCommands {

    operator fun set(name: String, command: Command) {
        NamedCommands.registerCommand(name, command)
    }

    operator fun get(name: String): Command {
        return NamedCommands.getCommand(name)
    }

}