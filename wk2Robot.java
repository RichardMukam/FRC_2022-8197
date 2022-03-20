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
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder.Type;
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
  CANSparkMax m_topLeft;
  CANSparkMax m_bottomLeft;
  CANSparkMax m_topRight;
  CANSparkMax m_bottomRight;
  RelativeEncoder tlM;
  RelativeEncoder blM;
  RelativeEncoder trM;
  RelativeEncoder brM;


  @Override
  public void robotInit() 
  {
    m_topLeft = new CANSparkMax(1,MotorType.kBrushless); 
    m_bottomLeft = new CANSparkMax(2, MotorType.kBrushless); 
    m_topRight = new CANSparkMax(4, MotorType.kBrushless); 
    m_bottomRight = new CANSparkMax(3, MotorType.kBrushless);
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
    //getting joystick values
    double joystickY = joystick.getY();
    double joystickX = joystick.getX();
    double joystickZ = joystick.getZ();
    double xyspeed = 0.8;
    double zspeed = 0.5;

    //deadband values
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

    //mecanum drive function
    mecanumDrive.driveCartesian(-joystickY*xyspeed, joystickX*xyspeed, joystickZ*zspeed);

    //shooter,feeder mechanism
      //shooter
    if (joystick.getRawButton(1))
    {
      m_shooter.set(1.0);
    }
    else
    {
      m_shooter.set(0.0);
    }

      //reverse all
    if (mechanismJoyStick.getRawButton(2))
    {
      m_shooter.set(-1.0);
      m_intake.set(TalonFXControlMode.PercentOutput, -1.0);
      m_feeder.set(TalonSRXControlMode.PercentOutput, -1.0);
    }
    else
    {
      m_shooter.set(0.0);
      m_intake.set(TalonFXControlMode.PercentOutput, 0.0);
      m_feeder.set(TalonSRXControlMode.PercentOutput, 0.0);
    }
      //intake,feeder
    if (mechanismJoyStick.getRawButton(3))
    {
      m_intake.set(TalonFXControlMode.PercentOutput, -1.0);
      m_feeder.set(TalonSRXControlMode.PercentOutput, -1.0);
    }
    else
    {
      m_intake.set(TalonFXControlMode.PercentOutput, 0.0);
      m_feeder.set(TalonSRXControlMode.PercentOutput, 0.0);
    }
    //reverse intake,feeder
    if (mechanismJoyStick.getRawButton(4))
    {
      m_intake.set(TalonFXControlMode.PercentOutput, 1.0);
      m_feeder.set(TalonSRXControlMode.PercentOutput, 1.0);
    }
    else
    {
      m_intake.set(TalonFXControlMode.PercentOutput, 0.0);
      m_feeder.set(TalonSRXControlMode.PercentOutput, 0.0);
    }
    //intake 
    if (mechanismJoyStick.getRawButton(10))
    {
      m_intake.set(TalonFXControlMode.PercentOutput, -1.0);
      m_feeder.set(TalonSRXControlMode.PercentOutput, -1.0);
    }

    if (mechanismJoyStick.getRawButton(7))
    {
      m_shooter.set(-0.8);
    }
    
    //encoders
    tlM = m_topLeft.getEncoder(Type.kHallSensor, 42);
    blM = m_bottomLeft.getEncoder(Type.kHallSensor, 42);
    trM = m_topRight.getEncoder(Type.kHallSensor, 42);
    brM = m_bottomRight.getEncoder(Type.kHallSensor, 42);

// wheels are 8 inches
    tlM.setPosition(0.0);
    blM.setPosition(0.0);
    trM.setPosition(0.0);
    brM.setPosition(0.0);


    
    SmartDashboard.putNumber("Top Left Motor Position", tlM.getPosition());
    SmartDashboard.putNumber("Bottom Left Motor Position", blM.getPosition());
    SmartDashboard.putNumber("Top Right Motor Position", trM.getPosition());
    SmartDashboard.putNumber("Bottom Right Motor Position", brM.getPosition());

    
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
    // shooter winds up
    if (time.get() < 2.5)
    {
      m_shooter.set(-0.8);
    }
    // shooter and feeder, (SHOOTING)
    if (time.get() > 2.5 && time.get() < 6.0)
    {
      m_shooter.set(-0.8);
      m_feeder.set(TalonSRXControlMode.PercentOutput, -1.0);
    }
    // moves backwards (to get point), feeder and shooter stop
    if (time.get() > 6.0 && time.get() < 8.5)
    {
      m_topLeft.set(-0.2);
      m_topRight.set(-0.2);
      m_bottomLeft.set(-0.2);
      m_bottomRight.set(-0.2);
      m_shooter.set(0.0);
      m_feeder.set(TalonSRXControlMode.PercentOutput, 0.0);
    }
    // drive train stops
    if (time.get() > 8.5)
    {
      m_topLeft.set(0.0);
      m_topRight.set(0.0);
      m_bottomLeft.set(0.0);
      m_bottomRight.set(0.0);
    }
  }

  @Override
  public void testPeriodic()
  {

  }
}
