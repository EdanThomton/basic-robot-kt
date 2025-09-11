package team.vaevictis.odometry

import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Rotation3d

/**
 * Gyro Interface
 */
interface Gyro {

    fun zeroHeading()
    fun setHeadingOffset(value: Rotation2d)
    val heading: Rotation2d
    val orientation: Rotation3d

}