// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  private WPI_TalonFX leftshoot = new WPI_TalonFX(8);
  private WPI_TalonFX rightshoot = new WPI_TalonFX(9);
  private WPI_TalonFX turretMotor = new WPI_TalonFX(10);

  private Servo leftservo = new Servo(0);
  private Servo rightservo = new Servo(1);

  private NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");
  private AddressableLED lightbar = new AddressableLED(9);
  private AddressableLEDBuffer lightbarbuffer = new AddressableLEDBuffer(76);

  /** Creates a new Shooter. */
  public Shooter() {
    leftshoot.configFactoryDefault();
    rightshoot.configFactoryDefault();
    turretMotor.configFactoryDefault();

    leftshoot.set(ControlMode.PercentOutput, 0);
    rightshoot.set(ControlMode.PercentOutput, 0);
    turretMotor.set(ControlMode.PercentOutput, 0);

    leftshoot.setInverted(false);
    rightshoot.setInverted(true);
    turretMotor.setInverted(false);

    leftshoot.setNeutralMode(NeutralMode.Coast);
    rightshoot.setNeutralMode(NeutralMode.Coast);
    turretMotor.setNeutralMode(NeutralMode.Brake);

    rightshoot.follow(leftshoot);

    leftshoot.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 30);
    leftshoot.configAllowableClosedloopError(0, 30);

    leftshoot.config_kF(0, 2048/200.0, 30);
    leftshoot.config_kP(0, 0.25, 30);
    leftshoot.config_kI(0, 0, 30);
    leftshoot.config_kD(0, 0, 30);

    leftshoot.configClosedloopRamp(0.005);
    leftshoot.configMotionAcceleration(500, 30);

    turretMotor.setSelectedSensorPosition(0);
    turretMotor.configForwardSoftLimitEnable(true);
    turretMotor.configReverseSoftLimitEnable(true);
    turretMotor.configForwardSoftLimitThreshold(1400);
    turretMotor.configReverseSoftLimitThreshold(-1400);

    leftservo.setBounds(2.0, 1.8, 1.5, 1.2, 1.0);
    rightservo.setBounds(2.0, 1.8, 1.5, 1.2, 1.0);

    lightbar.setLength(lightbarbuffer.getLength());
    lightbar.setData(lightbarbuffer);
    lightbar.start();
  }

  public void limelightMode(int mode){
    NetworkTableEntry limeLED = limelight.getEntry("ledMode");
    limeLED.setNumber(mode);
  }

  private double calcDistance(){
    double limelightAngleDeg = 42.0;
    double limelightHeightInches = 31.0;
    double goalHeightInches = 100.0;
    double ty = limelight.getEntry("ty").getDouble(0.0);

    return (goalHeightInches - limelightHeightInches)/Math.tan((limelightAngleDeg+ty)*(Math.PI/180));
  }

  public double calcVel(){
    double shootVel = (calcDistance() * 0.85 + 190);
    if(shootVel > 380){
      shootVel = 380;
    } if(shootVel < 150){
      shootVel = 150;
    }
    return shootVel;
  }

  public void revShooter(double velocity){
    leftshoot.set(TalonFXControlMode.Velocity, velocity);
  }

  public boolean shooterReady(){
    if(leftshoot.getSelectedSensorVelocity() >= (calcVel() * 31.5)){
      return true;
    }
    return false;
  }

  public double calcHood(){
    double hoodPos = (calcDistance() * 1.2 - 60);
    if(hoodPos < 0){
      hoodPos = 0;
    } if(hoodPos > 180){
      hoodPos = 180;
    }
    return hoodPos;
  }

  public void setHood(double angle){
    leftservo.setAngle(angle);
    rightservo.setAngle(angle);
  }

  public void turretAim(double speed){
    double tx = limelight.getEntry("tx").getDouble(0.0);
    if(tx > 3){
      turretMotor.set(ControlMode.PercentOutput, -speed);
    } else if(tx < -3){
      turretMotor.set(ControlMode.PercentOutput, speed);
    } else {
      turretMotor.set(ControlMode.PercentOutput, 0);
    }
  }

  public void turretReturn(double speed){
    while(turretMotor.getSelectedSensorPosition() > 50){
      turretMotor.set(ControlMode.PercentOutput, -speed);
    } while(turretMotor.getSelectedSensorPosition() < -50){
      turretMotor.set(ControlMode.PercentOutput, speed);
    }
    turretMotor.set(ControlMode.PercentOutput, 0);
  }

  public void manualTurret(double percent){
    turretMotor.set(ControlMode.PercentOutput, percent);
    turretMotor.setSelectedSensorPosition(0);
  }

  private void greenLED(){
    for(int i = 0; i < lightbarbuffer.getLength(); i++){
      lightbarbuffer.setRGB(i, 0, 255, 0);
    }
  }

  private void redLED(){
    for(int i = 0; i < lightbarbuffer.getLength(); i++){
      lightbarbuffer.setRGB(i, 255, 0, 0);
    }
  }

  public void onLED(){
    double tx = limelight.getEntry("tx").getDouble(0.0);
    if(Math.abs(tx) < 2){
      greenLED();
    } else {
      redLED();
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
