package team.vaevictis.subsystems.maxswerve

import edu.wpi.first.math.geometry.Translation2d

data class MaxWheelConfig(
    val speedID: Int, val angleID: Int,
    val p: Double, val i: Double, val d: Double,
    val ks: Double, val kv: Double, val ka: Double,
    val chassisAngleOffset: Double,
    val chassisLocation: Translation2d
);
