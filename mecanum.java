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



/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {
  //driving motors
  CANSparkMax  m_topLeft = new CANSparkMax(1,MotorType.kBrushless); // the top left motor - intiailizes the motor
 CANSparkMax  m_bottomLeft = new CANSparkMax(2, MotorType.kBrushless); // the bottom left motor - intiailizes the motor
 CANSparkMax  m_topRight = new CANSparkMax(4, MotorType.kBrushless); // the top right motor - intiailizes the motor
 CANSparkMax  m_bottomRight = new CANSparkMax(3, MotorType.kBrushless); // the bottom right motor - intiailizes the motor

   //creating an object for Joystick
   Joystick joystick = new Joystick(0);
   Joystick mechanismJoyStick = new Joystick(1);
  
   //mecanum
  private MecanumDrive MecanumDrive;

  //creating an object for the timer
  Timer time = new Timer();

  @Override
  public void robotInit() {

    MecanumDrive = new MecanumDrive(m_topLeft, m_bottomLeft, m_topRight, m_bottomRight);

    //initialize mecanum drives motors
    /* We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.*/
  }

  // this method runs once the robot enters teleop
  @Override
  public void teleopInit()
  {
    
  }
  
  // this method is ran periodically if the robot is in teleop
  @Override
  public void teleopPeriodic() {
    
    //mecanum drive, all drivetrain behaviors

    double joystickY = joystick.getY(); //storing y-axis values of joystick
    if (Math.abs(joystickY) < 0.3) //deadband for joystick
    {
      joystickY = 0.0;
    }
    //forward
    if (joystickY < -0.3) 
    {
      m_bottomRight.setInverted(false);
      m_topRight.setInverted(false);
      m_topLeft.setInverted(true);
      m_bottomLeft.setInverted(true);
    }
    //backward
    if (joystickY > 0.3) 
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
    //sliding left
    if(joystickX < -0.3) 
    {
      m_bottomRight.setInverted(true);
      m_topRight.setInverted(true);
      m_topLeft.setInverted(false);
      m_bottomLeft.setInverted(false);
    }
    //sliding right
    if (joystickX > 0.3)
    {
      m_bottomRight.setInverted(true);
      m_topRight.setInverted(true);
      m_topLeft.setInverted(false);
      m_bottomLeft.setInverted(false);
    }
    //22.5 degrees to 67.5, joystick bounds top right
    if (joystick.getDirectionDegrees() > 22.5 && joystick.getDirectionDegrees() < 67.5)
    {
      m_bottomLeft.stopMotor();
      m_topRight.stopMotor();
      m_bottomRight.setInverted(false);
      m_topLeft.setInverted(true);
      m_bottomRight.set(-0.5);
      m_topLeft.set(-0.5);
    }
    //112.5 degrees to 157.5, joystick bounds top left
    if (joystick.getDirectionDegrees() > 112.5 && joystick.getDirectionDegrees() < 157.5)
    {
      m_topLeft.stopMotor();
      m_bottomRight.stopMotor();
      m_topRight.setInverted(true);
      m_bottomLeft.setInverted(false);
      m_topRight.set(-0.5);
      m_bottomLeft.set(-0.5);
    }
    //202.5 degrees to 247.5, joystick bounds bottom left
    if (joystick.getDirectionDegrees() > 202.5 && joystick.getDirectionDegrees() < 247.5)
    {
      m_bottomLeft.stopMotor();
      m_topRight.stopMotor();
      m_bottomRight.setInverted(true);
      m_topLeft.setInverted(false);
      m_bottomRight.set(0.5);
      m_topLeft.set(0.5);
    }
    //292.5 degrees to 337.5, joystick bounds bottom right
    if (joystick.getDirectionDegrees() > 292.5 && joystick.getDirectionDegrees() < 337.5)
    {
      m_topLeft.stopMotor();
      m_bottomRight.stopMotor();
      m_topRight.setInverted(false);
      m_bottomLeft.setInverted(true);
      m_topRight.set(0.5);
      m_bottomLeft.set(0.5);
    }

    double joystickZ = joystick.getZ(); //storing z-axis values of joystick
    if (Math.abs(joystickZ) < 0.3) //deadband for joystick
    {
      joystickZ = 0.0;
    }
    //twist of joystick, rotate counterclockwise, (work in prog)
    /*if (joystick.getTwist() > 0.3)
    {

    }*/
  }

  // this method runs once the robot enters autonomous
  @Override
  public void autonomousInit()
  {

  } 

  // this method is ran periodically if the robot is in autonomous
  @Override
  public void autonomousPeriodic()
  {
    if (time.get() <= 15.0) // our autonomous period will run for five (5) seconds 
    {
      System.out.println("autonomous works: 3 sec");
      System.out.println(time.get());
      MecanumDrive.driveCartesian(0.5, 0.5, 0.0, 0.0);


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
