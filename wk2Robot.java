// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;

//import for PS4
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

//import for Limelight
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

//import for mecanumdrive
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;

//spark motors
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

//talonfx & talonsrx
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;

import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot 
{
  private MecanumDrive mecanumDrive;
  Joystick joystick = new Joystick(0);
  Joystick mechanismJoyStick = new Joystick(1);
  Timer time = new Timer();
  private CANSparkMax m_shooter;
  private TalonFX m_intake;
  private TalonSRX m_feeder;

  @Override
  public void robotInit() 
  {
    CANSparkMax  m_topLeft = new CANSparkMax(1,MotorType.kBrushless); 
    CANSparkMax  m_bottomLeft = new CANSparkMax(2, MotorType.kBrushless); 
    CANSparkMax  m_topRight = new CANSparkMax(4, MotorType.kBrushless); 
    CANSparkMax  m_bottomRight = new CANSparkMax(3, MotorType.kBrushless);
    m_shooter = new CANSparkMax(5, MotorType.kBrushless);
    m_intake = new TalonFX(7);
    m_feeder = new TalonSRX(6);

    m_topRight.setInverted(true);
    m_bottomRight.setInverted(true);
    m_topLeft.setInverted(false);
    m_bottomLeft.setInverted(false);

    CameraServer.startAutomaticCapture();
    mecanumDrive = new MecanumDrive(m_topLeft, m_bottomLeft, m_topRight, m_bottomRight);
  }

  @Override
  public void teleopInit()
  {

  }
  
  @Override
  public void teleopPeriodic() 
  {  
    double joystickY = joystick.getY();
    double joystickX = joystick.getX();
    double joystickZ = joystick.getZ();
    double xyspeed = 1.0;
    double zspeed = 0.4;

    if (Math.abs(joystickY) < 0.3)
    {
      joystickY = 0.0;
    }
    if (Math.abs(joystickX) < 0.3)
    {
      joystickX = 0.0;
    }
    if (Math.abs(joystickZ) < 0.3)
    {
      joystickZ = 0.0;
    }

    mecanumDrive.driveCartesian(-joystickY*xyspeed, joystickX*xyspeed, joystickZ*zspeed);

    if (mechanismJoyStick.getRawButton(1))
    {
      m_shooter.set(1.0);
    }
    if (mechanismJoyStick.getRawButton(2))
    {
      m_shooter.set(-1.0);
      m_intake.set(TalonFXControlMode.PercentOutput, -1.0);
      m_feeder.set(TalonSRXControlMode.PercentOutput, -1.0);
    }
    if (mechanismJoyStick.getRawButton(3))
    {
      m_intake.set(TalonFXControlMode.PercentOutput, 1.0);
      m_feeder.set(TalonSRXControlMode.PercentOutput, 1.0);
    }
    
  }

  @Override
  public void autonomousInit()
  {
    time.reset();
    time.start();
  } 

  @Override
  public void autonomousPeriodic()
  {

  }

  @Override
  public void testPeriodic()
  {

  }
}
