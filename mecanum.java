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
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

//Import for Limelight
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

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
import com.ctre.phoenix.motorcontrol.ControlMode;


public class Robot extends TimedRobot 
{
  //driving motors
  CANSparkMax  m_topLeft = new CANSparkMax(1,MotorType.kBrushless); // the top left motor - intiailizes the motor
  CANSparkMax  m_bottomLeft = new CANSparkMax(2, MotorType.kBrushless); // the bottom left motor - intiailizes the motor
  CANSparkMax  m_topRight = new CANSparkMax(4, MotorType.kBrushless); // the top right motor - intiailizes the motor
  CANSparkMax  m_bottomRight = new CANSparkMax(3, MotorType.kBrushless); // the bottom right motor - intiailizes the motor
  CANSparkMax shooter = new CANSparkMax(5, MotorType.kBrushless);
  TalonSRX feeder = new TalonSRX(7);

  Joystick joystick = new Joystick(0);
  Joystick mechanismJoyStick = new Joystick(1);
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  
  private MecanumDrive mecanumDrive;

  Timer time = new Timer();

  @Override
  public void robotInit() 
  {
    //initialize mecanum drive
    mecanumDrive = new MecanumDrive(m_topLeft, m_bottomLeft, m_topRight, m_bottomRight);
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
   //just to test (not at full speed)
   double speed = 0.2;
   double joystickY = joystick.getY(); 

   if (Math.abs(joystickY) < 0.3) 
   {
     joystickY = 0.0;
   }

   if (joystickY < -0.3 && joystick.getDirectionDegrees() < 11.25 && joystick.getDirectionDegrees() > 348.75) 
   {
     m_bottomRight.setInverted(false);
     m_topRight.setInverted(false);
     m_topLeft.setInverted(true);
     m_bottomLeft.setInverted(true);
   }
   if (joystickY > 0.3 && joystick.getDirectionDegrees() < 191.25 && joystick.getDirectionDegrees() > 168.75) 
   {
     m_bottomRight.setInverted(false);
     m_topRight.setInverted(false);
     m_topLeft.setInverted(true);
     m_bottomLeft.setInverted(true);
   }
   double joystickX = joystick.getX(); //storing x-axis values of joystick
   if (Math.abs(joystickX) < 0.3) //deadband for joystick
   {
     joystickX = 0.0;
   }
   if(joystickX < -0.3 && joystick.getDirectionDegrees() < 281.25 && joystick.getDirectionDegrees() > 258.75) 
   {
     m_bottomRight.setInverted(true);
     m_topRight.setInverted(true);
     m_topLeft.setInverted(false);
     m_bottomLeft.setInverted(false);
   }
   if (joystickX > 0.3 && joystick.getDirectionDegrees() < 101.0 && joystick.getDirectionDegrees() > 78.75)
   {
     m_bottomRight.setInverted(true);
     m_topRight.setInverted(true);
     m_topLeft.setInverted(false);
     m_bottomLeft.setInverted(false);
   }

   double joystickZ = joystick.getZ(); 
   if (Math.abs(joystickZ) < 0.3) 
   {
     joystickZ = 0.0;
   }
   if (joystickZ < -0.3)
   {
    m_bottomRight.setInverted(true);
    m_topRight.setInverted(true);
    m_topLeft.setInverted(false);
    m_bottomLeft.setInverted(false);
   }
   if (joystickZ > 0.3)
   {
    m_bottomRight.setInverted(true);
    m_topRight.setInverted(true);
    m_topLeft.setInverted(false);
    m_bottomLeft.setInverted(false);
   }

   if (joystick.getDirectionDegrees() < 337.5 && joystick.getDirectionDegrees() > 292.5)

   if(joystick.getRawButton(5) && joystickX < 0.3)
   {
    m_bottomRight.stopMotor();
    m_topRight.setInverted(true);
    m_topRight.set(joystickY);
    m_topLeft.stopMotor();
    m_bottomLeft.setInverted(true);
    m_bottomLeft.set(joystickY);
    speed = 0.0;
   }

    mecanumDrive.driveCartesian(joystickY*speed, joystickX*speed, joystickZ*speed);
    
    // gebys button controlled diagonal movement->
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
      mecanumDrive.driveCartesian(joystickY*speed, joystickX*speed, joystickZ*speed);
    }
    // if you arent moving diagonally then it initiates normal mechanum, so the code doesnt mix up
    



    //double s = 0.2;

    //MecanumDrive.driveCartesian(joystick.getRawAxis(1)*s, -joystick.getRawAxis(0)*s, joystick.getRawAxis(2)*s);

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
    if(time.get() <= 2){
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
      mecanumDrive.driveCartesian(-0.5,0.0, 0.0);
      shooter.set(0.0);
      feeder.set(ControlMode.PercentOutput, 0.0);
    }
    else
    {
      time.stop();
      mecanumDrive.stopMotor();
      //stop motor
    }
  }

  @Override
  public void testPeriodic()
  {

  }
}
