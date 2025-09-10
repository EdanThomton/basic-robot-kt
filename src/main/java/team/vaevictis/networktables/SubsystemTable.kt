package team.vaevictis.networktables

import edu.wpi.first.networktables.GenericEntry
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance

/**
 * NetworkTables Table for a subsystem
 * 
 * Abstracts away a little bit of WPILib bloat with the magic of kotlin
 */
class SubsystemTable(
    name: String
) {

    private val table: NetworkTable = NetworkTableInstance.getDefault().getTable("subsystems/$name");

    operator fun get(entry: String): GenericEntry
        = table.getTopic(entry).getGenericEntry();
}