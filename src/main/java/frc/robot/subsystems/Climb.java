// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climb extends SubsystemBase {
  private WPI_TalonFX leftclimb = new WPI_TalonFX(11);
  private WPI_TalonFX rightclimb = new WPI_TalonFX(12);
  private DoubleSolenoid climbPneu = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 3, 4);

  /** Creates a new Climb. */
  public Climb() {
    leftclimb.configFactoryDefault();
    rightclimb.configFactoryDefault();

    leftclimb.set(ControlMode.PercentOutput, 0);
    rightclimb.set(ControlMode.PercentOutput, 0);

    leftclimb.setNeutralMode(NeutralMode.Brake);
    rightclimb.setNeutralMode(NeutralMode.Brake);

    rightclimb.follow(leftclimb);

    climbPneu.set(Value.kForward);
  }

  public void setFirstClimb(double percent){
    leftclimb.set(ControlMode.PercentOutput, percent);
  }

  public void setSecondClimb(boolean release){
    if(release){
      climbPneu.set(Value.kReverse);
    } else {
      climbPneu.set(Value.kForward);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
