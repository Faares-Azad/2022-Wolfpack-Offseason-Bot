// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.auto.Auto;
import frc.robot.commands.cartridge.Eject;
import frc.robot.commands.cartridge.Feed;
import frc.robot.commands.cartridge.Hold;
import frc.robot.commands.cartridge.Idle;
import frc.robot.commands.cartridge.Reject;
import frc.robot.commands.cartridge.Stack;
import frc.robot.commands.climb.Deploy;
import frc.robot.commands.climb.Elevate;
import frc.robot.commands.drivetrain.StickDrive;
import frc.robot.commands.intake.Extend;
import frc.robot.commands.shooter.Dormant;
import frc.robot.commands.shooter.Manual;
import frc.robot.commands.shooter.Shoot;
import frc.robot.subsystems.Cartridge;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.triggers.QueueHold;
import frc.robot.triggers.QueueReject;
import frc.robot.triggers.QueueStack;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain subDrivetrain = new Drivetrain();
  private final Climb subClimb = new Climb();
  private final Intake subIntake = new Intake();
  private final Cartridge subCartridge = new Cartridge();
  private final Shooter subShooter = new Shooter();

  private final QueueStack trigQueueStack = new QueueStack(subCartridge);
  private final QueueHold trigQueueHold = new QueueHold(subCartridge);
  private final QueueReject trigQueueReject = new QueueReject(subCartridge);
  
  public static final CommandXboxController controller1 = new CommandXboxController(0);
  public static final CommandXboxController controller2 = new CommandXboxController(1);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    subDrivetrain.setDefaultCommand(new StickDrive(subDrivetrain));
    subCartridge.setDefaultCommand(new Idle(subCartridge));
    subShooter.setDefaultCommand(new Dormant(subShooter));
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    controller1.rightBumper().whileActiveOnce(new Eject(subCartridge), false);
    controller1.rightTrigger(0.1).whileActiveOnce(new Extend(subIntake));
    
    controller2.rightBumper().whileActiveContinuous(new Feed(subCartridge, subShooter), false);
    controller2.rightTrigger(0.1).whileActiveContinuous(new Shoot(subShooter));
    controller2.leftBumper().whileActiveContinuous(new Manual(subShooter));

    controller2.a().whileActiveContinuous(new Elevate(subClimb));
    controller2.x().whileActiveOnce(new Deploy(subClimb));

    trigQueueStack.whileActiveContinuous(new Stack(subCartridge));
    trigQueueHold.whileActiveContinuous(new Hold(subCartridge));
    trigQueueReject.whileActiveContinuous(new Reject(subCartridge));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return new Auto(subDrivetrain, subIntake, subCartridge, subShooter);
  }
}
