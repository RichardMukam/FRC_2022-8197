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
  
  private MecanumDrive MecanumDrive;

  Timer time = new Timer();

  @Override
  public void robotInit() 
  {
    //initialize mecanum drive
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
  //mecanum drive, all drivetrain behaviors

  //storing y-axis values of joystick
  // double joystickY = joystick.getY(); 
  // //deadband for joystick, if y-axis is less than 0.3, register no value (catch for errors)
  // if (Math.abs(joystickY) < 0.3) 
  // {
  //   joystickY = 0.0;
  // }

  // //forward
  // if (joystickY < -0.3) 
  // {
  //   m_bottomRight.setInverted(false);
  //   m_topRight.setInverted(false);
  //   m_topLeft.setInverted(true);
  //   m_bottomLeft.setInverted(true);
  // }
  // //backward
  // if (joystickY > 0.3) 
  // {
  //   m_bottomRight.setInverted(false);
  //   m_topRight.setInverted(false);
  //   m_topLeft.setInverted(true);
  //   m_bottomLeft.setInverted(true);
  // }

  // double joystickX = joystick.getX(); //storing x-axis values of joystick
  // if (Math.abs(joystickX) < 0.3) //deadband for joystick
  // {
  //   joystickX = 0.0;
  // }
  // //sliding left
  // if(joystickX < -0.3) 
  // {
  //   m_bottomRight.setInverted(true);
  //   m_topRight.setInverted(true);
  //   m_topLeft.setInverted(false);
  //   m_bottomLeft.setInverted(false);
  // }
  // //sliding right
  // if (joystickX > 0.3)
  // {
  //   m_bottomRight.setInverted(true);
  //   m_topRight.setInverted(true);
  //   m_topLeft.setInverted(false);
  //   m_bottomLeft.setInverted(false);
  // }
  // //22.5 degrees to 67.5, joystick bounds top right
  // if (joystick.getDirectionDegrees() > 22.5 && joystick.getDirectionDegrees() < 67.5)
  // {
  //   m_bottomLeft.stopMotor();
  //   m_topRight.stopMotor();
  //   m_bottomRight.setInverted(false);
  //   m_topLeft.setInverted(true);
  //   m_bottomRight.set(-0.5);
  //   m_topLeft.set(-0.5);
  // }

  // double joystickZ = joystick.getZ(); //storing z-axis values of joystick
  // if (Math.abs(joystickZ) < 0.3) //deadband for joystick
  // {
  //   joystickZ = 0.0;
  // }

  // if (joystick.getRawButton(1))
  // {
  //   shooter.set(1.0);
  //   feeder.set(ControlMode.PercentOutput, 0.6);
  // }
  // else
  // {
  //   shooter.set(0.0);
  //   feeder.set(ControlMode.PercentOutput, 0.0);
  // }
 
  
  //twist of joystick, rotate counterclockwise, (work in prog)
  /*if (joystick.getTwist() > 0.3)
  {

    }*/

    // MecanumDrive.driveCartesian(joystickY, joystickX, joystickZ);

    // //limelight
    
    // NetworkTableEntry tx = table.getEntry("tx");
    // NetworkTableEntry ty = table.getEntry("ty");
    // NetworkTableEntry ta = table.getEntry("ta");
    // NetworkTableEntry tv = table.getEntry("tv");

    // // double x = tx.getDouble(0);
    // // double y = ty.getDouble(0);
    // double area = ta.getDouble(0.0);
    // // double isTarget = tv.getDouble(0);

    // // SmartDashboard.putNumber("LimelightX", x);
    // // SmartDashboard.putNumber("LimelightY", y);
    // SmartDashboard.putNumber("LimelightArea: ", area);
    // // SmartDashboard.putNumber("LimelightIsTarget", isTarget);

    // System.out.println(area);
    // System.out.println(isTarget);

    double s = 0.2;

    MecanumDrive.driveCartesian( joystick.getRawAxis(1)*s, -joystick.getRawAxis(0)*s, joystick.getRawAxis(2)*s);

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
      MecanumDrive.driveCartesian(-0.5,0.0, 0.0);
      shooter.set(0.0);
      feeder.set(ControlMode.PercentOutput, 0.0);
    }
    else
    {
      time.stop();
      MecanumDrive.stopMotor();
      //stop motor
    }
  }

  @Override
  public void testPeriodic()
  {

  }
}
