// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonFX;
//adding ps4 controls
import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import edu.wpi.first.wpilibj.PS4Controller;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {
  //private final PWMSparkMax m_leftMotor = new PWMSparkMax(0);
  private final PWMTalonFX m_leftMotor = new PWMTalonFX(4);
  private final PWMTalonFX m_rightMotor = new PWMTalonFX(2);
  //private final PWMSparkMax m_rightMotor = new PWMSparkMax(1);
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftMotor, m_rightMotor);
  private final PWMTalonFX m_lShooterMotor = new PWMTalonFX(5);
  private final PWMTalonFX m_rShooterMotor = new PWMTalonFX(1);
  private final DifferentialDrive m_externalMotorDrive = new DifferentialDrive(m_lShooterMotor, m_rShooterMotor);

  //private final Joystick m_stick = new Joystick(0);
  
  //adding ps4 controls
  private final PS4Controller ps4 = new PS4Controller(0);

  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_rightMotor.setInverted(true);
    //m_rShooterMotor.setInverted(true);

  }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.

    m_robotDrive.arcadeDrive(-ps4.getLeftY(), ps4.getLeftX());

    m_externalMotorDrive.arcadeDrive(-ps4.getRightY(), 0.0); //shooter motor controller

    //m_externalMotorDrive.setSpeed()
    
  }
}
