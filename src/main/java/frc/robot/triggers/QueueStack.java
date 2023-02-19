// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.triggers;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Cartridge;

/** Add your docs here. */
public class QueueStack extends Trigger {
  private Cartridge m_cartridge;

  public QueueStack(Cartridge n_cartridge){
    m_cartridge = n_cartridge;
  }

  @Override
  public boolean get() {
    if(m_cartridge.revDetectDist() && m_cartridge.revDetectColor() && !m_cartridge.sharpDetect()){
      return true;
    }
    return false;
  }
}
