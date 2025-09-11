package team.vaevictis.networktables

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Pose3d
import edu.wpi.first.math.geometry.Quaternion
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.geometry.Translation3d
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.networktables.GenericEntry
import edu.wpi.first.networktables.NetworkTable

/**
 * NetworkTables table entry
 */
class NetworkEntryKT (
    private val entry: GenericEntry,
    private val table: NetworkTable,
    private val entryName: String
) {

    /** Entry as a Long */
    var long: Long
        get() = entry.getInteger(Long.MIN_VALUE)
        set(value) {
            entry.setInteger(value)
        }
    /** Entry as an array of Longs */
    var longArray: Array<Long>
        get() = entry.getIntegerArray(emptyArray<Long>())
        set(value) {
            entry.setIntegerArray(value)
        }

    /** Entry as a Float */
    var float: Float
        get() = entry.getFloat(Float.NaN)
        set(value) {
            entry.setFloat(value)
        }
    /** Entry as an array of Floats */
    var floatArray: Array<Float>
        get() = entry.getFloatArray(emptyArray<Float>())
        set(value) {
            entry.setFloatArray(value)
        }

    /** Entry as a Double */
    var double: Double
        get() = entry.getDouble(Double.NaN)
        set(value) {
            entry.setDouble(value)
        }
    /** Entry as an array of Doubles */
    var doubleArray: Array<Double>
        get() = entry.getDoubleArray(emptyArray<Double>())
        set(value) {
            entry.setDoubleArray(value)
        }

    /** Entry as a Boolean */
    var boolean: Boolean
        get() = entry.getBoolean(false)
        set(value) {
            entry.setBoolean(value)
        }
    /** Entry as an array of Booleans */
    var booleanArray: Array<Boolean>
        get() = entry.getBooleanArray(emptyArray<Boolean>())
        set(value) {
            entry.setBooleanArray(value)
        }

    /** Entry as a Pose2d */
    var pose2d: Pose2d
        get() = table.getStructTopic(entryName, Pose2d.struct).subscribe(Pose2d()).get()
        set(value) {
            table.getStructTopic(entryName, Pose2d.struct).publish().set(value)
        }

    /** Entry as a Pose3d */
    var pose3d: Pose3d
        get() = table.getStructTopic(entryName, Pose3d.struct).subscribe(Pose3d()).get()
        set(value) {
            table.getStructTopic(entryName, Pose3d.struct).publish().set(value)
        }

    /** Entry as a Translation2d */
    var translation2d: Translation2d
        get() = table.getStructTopic(entryName, Translation2d.struct).subscribe(Translation2d()).get()
        set(value) {
            table.getStructTopic(entryName, Translation2d.struct).publish().set(value)
        }

    /** Entry as a Translation3d */
    var translation3d: Translation3d
        get() = table.getStructTopic(entryName, Translation3d.struct).subscribe(Translation3d()).get()
        set(value) {
            table.getStructTopic(entryName, Translation3d.struct).publish().set(value)
        }

    /** Entry as a Quaternion */
    var quaternion: Quaternion
        get() = table.getStructTopic(entryName, Quaternion.struct).subscribe(Quaternion()).get()
        set(value) {
            table.getStructTopic(entryName, Quaternion.struct).publish().set(value)
        }

    /** Entry as a single SwerveModuleState */
    var swerveModuleState: SwerveModuleState
        get() = table.getStructTopic(entryName, SwerveModuleState.struct).subscribe(SwerveModuleState()).get()
        set(value) {
            table.getStructTopic(entryName, SwerveModuleState.struct).publish().set(value)
        }

    /** Entry as an array of SwerveModuleStates */
    var swerveModuleStateArray: Array<SwerveModuleState>
        get() = table.getStructArrayTopic(entryName, SwerveModuleState.struct).subscribe(emptyArray<SwerveModuleState>()).get()
        set(value) {
            table.getStructArrayTopic(entryName, SwerveModuleState.struct).publish().set(value)
        }
}