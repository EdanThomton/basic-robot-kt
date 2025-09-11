# 1418: Vae Victis | Template Robot
Common code + template for a swerve drive robot capable 
of autonomous pathfinding.

## TODO:
- Swerve Drive
  - Autoalign target-centric 
    - both LL + photon?
    - needed for piece/april tag targeting
  - Fix drifting when using rotation correction (in meeting)
  - Implement slew limiting for rotation
- Pathplanner
  - Implement NamedCommands framework
  - Implement Teleop pathfinding