// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package edu.wpi.first.wpilibj.examples.arcadedrive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMSparkMax;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup ;
import edu.wpi.first.wpilibj.Talon;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {
  TalonSRX m_FrontleftMotor = new TalonSRX (3);
  TalonSRX m_FrontrightMotor = new TalonSRX (0);
  TalonSRX m_BackleftMotor = new TalonSRX (2);
  TalonSRX m_BackrightMotor = new TalonSRX (4);
  SpeedControllerGroup m_left=new SpeedControllerGroup(m_FrontleftMotor,m_BackleftMotor);
  SpeedControllerGroup m_right=new SpeedControllerGroup(m_FrontrightMotor,m_FrontrightMotor);
  //private final PWMSparkMax m_leftMotor = new PWMSparkMax(0);
  //private final PWMSparkMax m_rightMotor = new PWMSparkMax(1);
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_left, m_right);
  private final Joystick m_stick = new Joystick(0);

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    m_robotDrive.arcadeDrive(m_stick.getY(), m_stick.getX());
  }
}
