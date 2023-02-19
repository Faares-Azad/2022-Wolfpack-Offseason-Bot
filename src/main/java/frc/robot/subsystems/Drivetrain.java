// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
  public PowerDistribution pdp = new PowerDistribution(20, ModuleType.kRev);

  private WPI_TalonFX leftdrive1 = new WPI_TalonFX(1);
  private WPI_TalonFX leftdrive2 = new WPI_TalonFX(2);
  private WPI_TalonFX rightdrive1 = new WPI_TalonFX(3);
  private WPI_TalonFX rightdrive2 = new WPI_TalonFX(4);

  private MotorControllerGroup leftdrivegroup = new MotorControllerGroup(leftdrive1, leftdrive2);
  private MotorControllerGroup rightdrivegroup = new MotorControllerGroup(rightdrive1, rightdrive2);
  private DifferentialDrive difdrive = new DifferentialDrive(leftdrivegroup, rightdrivegroup);

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    pdp.setSwitchableChannel(true);
    pdp.clearStickyFaults();

    leftdrive1.configFactoryDefault();
    leftdrive2.configFactoryDefault();
    rightdrive1.configFactoryDefault();
    rightdrive2.configFactoryDefault();

    leftdrive1.set(ControlMode.PercentOutput, 0);
    leftdrive2.set(ControlMode.PercentOutput, 0);
    rightdrive1.set(ControlMode.PercentOutput, 0);
    rightdrive2.set(ControlMode.PercentOutput, 0);

    leftdrive1.setInverted(false);
    leftdrive2.setInverted(false);
    rightdrive1.setInverted(true);
    rightdrive2.setInverted(true);

    leftdrive1.setNeutralMode(NeutralMode.Coast);
    leftdrive2.setNeutralMode(NeutralMode.Coast);
    rightdrive1.setNeutralMode(NeutralMode.Coast);
    rightdrive2.setNeutralMode(NeutralMode.Coast);
  } 

  public void curveDrive(double speed, double rotation, boolean turnInPlace){
    difdrive.curvatureDrive(speed, -rotation, turnInPlace);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
