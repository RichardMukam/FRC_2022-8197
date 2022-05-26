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
  GenericHID mecJoystick = new GenericHID(1);
  Timer time = new Timer();
  CANSparkMax m_shooter;
  TalonFX m_intake;
  TalonSRX m_feeder;
  CANSparkMax m_topLeft;
  CANSparkMax m_bottomLeft;
  CANSparkMax m_topRight;
  CANSparkMax m_bottomRight;
  CANSparkMax m_climber;
  RelativeEncoder tlM;
  RelativeEncoder blM;
  RelativeEncoder trM;
  RelativeEncoder brM;
  RelativeEncoder shooter;  

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
    m_climber = new CANSparkMax(8,MotorType.kBrushless);

    m_topRight.setInverted(true);
    m_bottomRight.setInverted(true);
    m_topLeft.setInverted(false);
    m_bottomLeft.setInverted(false);

    CameraServer.startAutomaticCapture();

    mecanumDrive = new MecanumDrive(m_topLeft, m_bottomLeft, m_topRight, m_bottomRight);

    tlM = m_topLeft.getEncoder();
    blM = m_bottomLeft.getEncoder();
    trM = m_topRight.getEncoder();
    brM = m_bottomRight.getEncoder();
    shooter = m_shooter.getEncoder();
    
    // setting the initial positions of the wheels
    tlM.setPosition(0.0);
    blM.setPosition(0.0);
    trM.setPosition(0.0);
    brM.setPosition(0.0);
    shooter.setPosition(0.0);
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
    double xyspeed = 0.4; //correct is 0.6
    double zspeed = 0.3; //correct is 0.3

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
    m_shooter.set(0.0);
    m_feeder.set(TalonSRXControlMode.PercentOutput,0.0);
      m_intake.set(TalonFXControlMode.PercentOutput,0.0);
    if (mecJoystick.getRawButton(6))
    {
      m_shooter.set(-1.0);
    }
    
    if(mecJoystick.getRawButton(8))
    {
      m_shooter.set(0.7);
    }
    if(mecJoystick.getRawButton(5))
    {
      m_feeder.set(TalonSRXControlMode.PercentOutput,-0.8);
      m_intake.set(TalonFXControlMode.PercentOutput,-0.8);
    }
    if(mecJoystick.getRawButton(7))
    {
      m_feeder.set(TalonSRXControlMode.PercentOutput,1.0);
      m_intake.set(TalonFXControlMode.PercentOutput,1.0);
    }
    m_climber.set(0.0);
    
    if( mecJoystick.getRawButton(4)){
      m_climber.set(0.8);
    }

    if(mecJoystick.getRawButton(2)){
      m_climber.set(-0.8);
    }

    //ramp up the shooter, exponential vs, slower rise to high speed-----find out about the pressure of the ball    
    
    double cfShooter = -0.31789925694465637; //conversion factor to ft/s for shooter
    //position
    SmartDashboard.putNumber("TopLMotorPos", tlM.getPosition());
    SmartDashboard.putNumber("BottomLMotorPos", blM.getPosition());
    SmartDashboard.putNumber("TopRMotorPos", trM.getPosition());
    SmartDashboard.putNumber("BottomRMotorPos", brM.getPosition());
    SmartDashboard.putNumber("ShooterPos", shooter.getPosition()*cfShooter);
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
