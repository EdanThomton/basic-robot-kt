package frc.robot.subsystems.climb

import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.PneumaticHub
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import team.vaevictis.networktables.NetworkTableInstanceKT
import team.vaevictis.networktables.NetworkTableKT

/**
 * Pneumatics climber subsystem
 */
class ClimbSubsystem: SubsystemBase() {

    private val table: NetworkTableKT = NetworkTableInstanceKT.DEFAULT["subsystems/Climber"]
    private var climbingNT  by table["climbing"].boolean
    private var attachingNT by table["attaching"].boolean
    private var pressureNT  by table["pressure"].double
    private var timeNT      by table["time"].double

    val hub: PneumaticHub = PneumaticHub(ClimbConstants.HUB_ID)

    val climbSolenoid: DoubleSolenoid = DoubleSolenoid(
        ClimbConstants.HUB_ID,
        PneumaticsModuleType.REVPH,
        ClimbConstants.CLIMB_SOLENOID_FORWARD,
        ClimbConstants.CLIMB_SOLENOID_REVERSE
    )

    val attachSolenoid: DoubleSolenoid = DoubleSolenoid(
        ClimbConstants.HUB_ID,
        PneumaticsModuleType.REVPH,
        ClimbConstants.ATTACH_SOLENOID_FORWARD,
        ClimbConstants.ATTACH_SOLENOID_REVERSE
    )

    var attaching: Boolean = false
    var climbing: Boolean = false

    var time = 0.0

    init {
        climbSolenoid.set(DoubleSolenoid.Value.kReverse)
        attachSolenoid.set(DoubleSolenoid.Value.kReverse)
    }

    fun toggleAttachingFun() {
        attachSolenoid.toggle()
        attaching = !attaching
    }
    fun extendAttach() {
        attachSolenoid.set(DoubleSolenoid.Value.kForward)
        attaching = true
    }
    fun retractAttach() {
        attachSolenoid.set(DoubleSolenoid.Value.kReverse)
        attaching = false
    }
    val toggleAttach: Command get() = runOnce { toggleAttachingFun() }

    fun toggleClimbingFun() {
        climbSolenoid.toggle()
        climbing = !climbing
    }
    fun extendClimb() {
        climbSolenoid.set(DoubleSolenoid.Value.kForward)
        climbing = true
    }
    fun retractClimb() {
        climbSolenoid.set(DoubleSolenoid.Value.kReverse)
        climbing = false
    }
    val toggleClimb: Command get() = runOnce { toggleClimbingFun() }

    override fun periodic() {
        if(DriverStation.isTeleopEnabled()) {
            if(time >= 90.0) {
                hub.enableCompressorAnalog(100.0, 120.0);
            }
            time += 0.02
        }

        climbingNT = climbing
        attachingNT = attaching
        pressureNT = hub.getPressure(0)
        timeNT = time
    }

}