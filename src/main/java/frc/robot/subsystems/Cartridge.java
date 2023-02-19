// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Cartridge extends SubsystemBase {
  private WPI_TalonFX cartridgeMotor = new WPI_TalonFX(7);
  private WPI_TalonFX filterMotor = new WPI_TalonFX(6);
  private DoubleSolenoid kickerPneu = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 6);

  private AnalogInput sharpsense = new AnalogInput(2);
  private ColorSensorV3 colorsense = new ColorSensorV3(I2C.Port.kMXP);

  SendableChooser<DriverStation.Alliance> allianceChoose = new SendableChooser<>();

  /** Creates a new Cartridge. */
  public Cartridge() {
    allianceChoose.setDefaultOption("Blue Alliance", Alliance.Blue);
    allianceChoose.addOption("Red Alliance", Alliance.Red);
    SmartDashboard.putData(allianceChoose);

    cartridgeMotor.configFactoryDefault();
    filterMotor.configFactoryDefault();
    
    cartridgeMotor.set(ControlMode.PercentOutput, 0);
    filterMotor.set(ControlMode.PercentOutput, 0);

    cartridgeMotor.setInverted(false);
    filterMotor.setInverted(true);

    cartridgeMotor.setNeutralMode(NeutralMode.Brake);
    filterMotor.setNeutralMode(NeutralMode.Brake);

    kickerPneu.set(Value.kForward);
  }

  public void setFilter(double percent){
    filterMotor.set(ControlMode.PercentOutput, percent);
  }

  public void setCartridge(double percent){
    cartridgeMotor.set(ControlMode.PercentOutput, percent);
  }

  private double getSharpDist(){
    return ((Math.pow(sharpsense.getAverageVoltage(), -1.2045)) * 27.726);
  }
  
  public boolean sharpDetect(){
    if(getSharpDist() <= 140.0){
      return true;
    } 
    return false;
  }

  public boolean revDetectDist(){
    if(colorsense.getProximity() >= 160){
      return true;
    }
    return false;
  }

  public boolean revDetectColor(){
    if(getRevColor() >= 180){
      return false;
    }
    return true;
  }

  private int getRevColor(){
    if(allianceChoose.getSelected() == Alliance.Blue){
      return colorsense.getRed();
    } else {
      return colorsense.getBlue();
    }
  }

  public void toggleKicker(boolean position){
    if(position){
      kickerPneu.set(Value.kReverse);
    } else {
      kickerPneu.set(Value.kForward);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Blue val", colorsense.getBlue());
    SmartDashboard.putNumber("Red val", colorsense.getRed());
    SmartDashboard.putNumber("Color val", getRevColor());
    SmartDashboard.putNumber("Rev distance", colorsense.getProximity());
    SmartDashboard.putNumber("Sharp distance", getSharpDist());
  }
}
