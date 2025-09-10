package team.vaevictis.odometry.gyros

import com.studica.frc.AHRS
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Rotation3d
import team.vaevictis.odometry.Gyro;

/**
 * USB NavX Gyroscope
 */
class NavXGyro: Gyro {

    val gyro: AHRS = AHRS(AHRS.NavXComType.kUSB1);
    private var offset: Rotation2d = Rotation2d(0.0);

    init {

        zeroHeading();

    }

    /**
     * Set heading zero-point to current rotation
     */
    override fun zeroHeading() {
        offset = Rotation2d(0.0);
        gyro.reset();
    }

    /**
     * Set heading offset
     * @param value Updated offset
     */
    override fun setHeadingOffset(value: Rotation2d) {
        offset = value;
    }

    /// current heading
    override val heading: Rotation2d
        get() = gyro.rotation2d.plus(offset);

    /// current 3D orientation
    override val orientation: Rotation3d
        get() = gyro.rotation3d;
}