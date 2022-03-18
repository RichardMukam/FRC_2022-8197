// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonFX;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

//Import for PS4
import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
//Import for mecanumdrive
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
//spark motors
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
//limelight
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.CANifier.PWMChannel;
import com.ctre.phoenix.motorcontrol.ControlMode;
//Pneumatics
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
//camera
import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot 
{
  //driving motors
  CANSparkMax  m_topLeft = new CANSparkMax(1,MotorType.kBrushless); // the top left motor - intiailizes the motor
  CANSparkMax  m_bottomLeft = new CANSparkMax(2, MotorType.kBrushless); // the bottom left motor - intiailizes the motor
  CANSparkMax  m_topRight = new CANSparkMax(4, MotorType.kBrushless); // the top right motor - intiailizes the motor
  CANSparkMax  m_bottomRight = new CANSparkMax(3, MotorType.kBrushless); // the bottom right motor - intiailizes the motor
  CANSparkMax shooter = new CANSparkMax(5, MotorType.kBrushless);
  TalonSRX feeder = new TalonSRX(7);
  TalonSRX intake = new TalonSRX(6);

  Joystick joystick = new Joystick(0);
  Joystick mechanismJoyStick = new Joystick(1);
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

  //pneumatics compressor
  Compressor pcmCompressor = new Compressor(0, PneumaticsModuleType.CTREPCM); 

  //pneumatics solenoids
  DoubleSolenoid solenoid1 = new DoubleSolenoid(4,PneumaticsModuleType.CTREPCM,4, 5);
  

  
  private MecanumDrive MecanumDrive;

  Timer time = new Timer();

  @Override
  public void robotInit() 
  { 
    //initialize mecanum drive
    CameraServer.startAutomaticCapture();

    //pneumatics
    pcmCompressor.enableDigital();
    pcmCompressor.disable();

    //enabling solenoid channels
    solenoid1.set(kOff);
    solenoid1.set(kForward);
    solenoid1.set(kReverse);

    //If the solenoid is set to forward, it'll be set to reverse. If the solenoid is set to reverse, it'll be set to forward. If the solenoid is set to off, nothing happens.
    solenoid1.toggle();



    boolean enabled = pcmCompressor.enabled();
    boolean pressureSwitch = pcmCompressor.getPressureSwitchValue();
    double current = pcmCompressor.getCurrent();
    
    MecanumDrive = new MecanumDrive(m_topLeft, m_bottomLeft, m_topRight, m_bottomRight);
  }

  // this method runs once the robot enters teleop
  @Override
  public void teleopInit()
  {
    
  }
  
  // this method is ran periodically if the robot is in teleop
  @Override
  public void teleopPeriodic() 
  { 
    if (mechanismJoyStick.getRawButton(1)){
      shooter.set(1.0);
    }
    else{
      shooter.set(0.0);
    }

    if(mechanismJoyStick.getRawButton(2)){
      feeder.set(ControlMode.Current,1.0);
    }
    else{
      feeder.set(ControlMode.Current,0.0);
    }
    //jam fix(reverse)
    if (mechanismJoyStick.getRawButton(2)){
      shooter.set(-1.0);
    }
    else{
      shooter.set(0.0);
    }

    if(mechanismJoyStick.getRawButton(3)){
      intake.set(ControlMode.Current,1.0);
    }
    else{
      intake.set(ControlMode.Current,0.0);
    }

    /*double joystickY = joystick.getY(); 
    double joystickX = joystick.getX();
    double joystickZ = joystick.getZ(); 
    double speed = 0.2;
    double diagSpeed = 0.0;
    // speed for diagonal movement
    boolean isAngle = false;
    // if you are moving diagonally
    if(joystick.getRawButtonPressed(7)){
      diagSpeed = 0.2;
      
    }
    if(joystick.getRawButtonPressed(8)){
      diagSpeed = 0.5;
      
    }
    if(joystick.getRawButtonPressed(9)){
      diagSpeed = 1.0;
      
    }
    //changing speed for each button pressed

    if(joystick.getRawButton(3)||joystick.getRawButton(4)||joystick.getRawButton(5)||joystick.getRawButton(6))
    {
     isAngle = true;
    }// if any of those diagonal controll buttons are pressed then angle movement is true
    if(joystick.getRawButton(5)){
      m_bottomRight.stopMotor();
      m_topLeft.stopMotor();
      m_topRight.setInverted(true);
      m_bottomLeft.setInverted(true);
      m_topRight.set(diagSpeed);
      m_bottomLeft.set(diagSpeed);
    }
    if(joystick.getRawButton(6)){
      m_topLeft.setInverted(true);
      m_bottomRight.setInverted(true);
      m_topRight.stopMotor();
      m_bottomLeft.stopMotor();
      m_bottomRight.set(diagSpeed);
      m_topLeft.set(diagSpeed);
    }
   if(joystick.getRawButton(3)){
      m_topLeft.setInverted(true);
      m_bottomRight.setInverted(true);
      m_topRight.stopMotor();
      m_bottomLeft.stopMotor();
      m_bottomRight.set(-diagSpeed);
      m_topLeft.set(-diagSpeed);
    }
    if(joystick.getRawButton(4)){
      m_bottomRight.stopMotor();
      m_topLeft.stopMotor();
      m_topRight.setInverted(true);
      m_bottomLeft.setInverted(true);
      m_topRight.set(-diagSpeed);
      m_bottomLeft.set(-diagSpeed);
    }
    //setting motor directions based on mecanum chart
    if(isAngle == false){
      MecanumDrive.driveCartesian(joystickY*speed, joystickX*speed, joystickZ*speed);

    }*/

  }

  // this method runs once the robot enters autonomous
  @Override
  public void autonomousInit()
  {
    time.reset();
    time.start();
  } 

  // this method is ran periodically if the robot is in autonomous
  @Override
  public void autonomousPeriodic()
  {
    /*if(time.get() <= 2){
      shooter.set(1.0);
    }
    else if (time.get() <= 5.0) // our autonomous period will run for three (3) seconds 
    {
      //drive forward 
      System.out.println("autonomous works: 3 sec");
      System.out.println(time.get());
      //MecanumDrive.driveCartesian(0.5, 0.5, 0.0, 0.0);
      feeder.set(ControlMode.PercentOutput, 0.6);
    }
    else if(time.get() <= 7.0){
      // drive backward 
      MecanumDrive.driveCartesian(-0.5,0.0, 0.0);
      shooter.set(0.0);
      feeder.set(ControlMode.PercentOutput, 0.0);
    }
    else
    {
      time.stop();
      MecanumDrive.stopMotor();
      //stop motor
    }*/
  }

  @Override
  public void testPeriodic()
  {

  }
}
