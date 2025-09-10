package team.vaevictis.networktables

import edu.wpi.first.networktables.NetworkTable

/**
 * NetworkTables table
 */
class NetworkTableKT (
    private val table: NetworkTable
) {

    operator fun get(entry: String): NetworkEntryKT = NetworkEntryKT(
        table.getTopic(entry).getGenericEntry(),
        table,
        entry
    )

}