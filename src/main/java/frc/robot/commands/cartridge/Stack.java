// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.cartridge;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Cartridge;

public class Stack extends CommandBase {
  private Cartridge m_cartridge;
  
  /** Creates a new Stack. */
  public Stack(Cartridge n_cartridge) {
    m_cartridge = n_cartridge;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_cartridge);
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_cartridge.setFilter(0.2);
    m_cartridge.setCartridge(0.4);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
