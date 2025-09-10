package team.vaevictis.networktables

import edu.wpi.first.networktables.GenericEntry
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance

class SubsystemTable(
    name: String
) {

    private val table: NetworkTable = NetworkTableInstance.getDefault().getTable("subsystems/$name");

    operator fun get(entry: String): GenericEntry
        = table.getTopic(entry).getGenericEntry();
}