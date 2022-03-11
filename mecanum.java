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

  /*//driving motors
  PWMSparkMax m_topLeft = new PWMSparkMax(4); // the top left motor - intiailizes the motor
 // private final PWMSparkMax m_topLeft = new PWMSparkMax(4);
  PWMSparkMax m_bottomLeft = new PWMSparkMax(3); // the bottom left motor - intiailizes the motor
 // private final PWMSparkMax m_bottomLeft = new PWMSparkMax(4);
  PWMSparkMax m_topRight = new PWMSparkMax(2); // the top right motor - intiailizes the motor
  PWMSparkMax m_bottomRight = new PWMSparkMax(1); // the bottom right motor - intiailizes the motor
  //adding ps4 controls*/


  //driving motors
  CANSparkMax  m_topLeft = new CANSparkMax(1,MotorType.kBrushless); // the top left motor - intiailizes the motor
 // private final PWMSparkMax m_topLeft = new PWMSparkMax(4);
 CANSparkMax  m_bottomLeft = new CANSparkMax(2, MotorType.kBrushless); // the bottom left motor - intiailizes the motor
 // private final PWMSparkMax m_bottomLeft = new PWMSparkMax(4);
 CANSparkMax  m_topRight = new CANSparkMax(4, MotorType.kBrushless); // the top right motor - intiailizes the motor
 CANSparkMax  m_bottomRight = new CANSparkMax(3, MotorType.kBrushless); // the bottom right motor - intiailizes the motor

  //adding Joystick
  PS4Controller ps4 = new PS4Controller(0);
  Joystick joystick = new Joystick(0);

  //creating an object for the timer
  Timer time = new Timer();

  //mecanum
  private MecanumDrive Mecanum;
  @Override
  public void robotInit() {
    Mecanum = new MecanumDrive(m_topLeft, m_bottomLeft, m_topRight, m_bottomRight);

    //initialize mecanum drives motors
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("<variablename>").setNumber(3);
    /* We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.*/
    //m_rShooterMotor.setInverted(true);
  }

  // this method runs once the robot enters teleop
  @Override
  public void teleopInit()
  {

    // ps4.setRumble(kLeftRumble, 0.5); // rumbles the ps4 controller (we have access to this method despite it being in GenericHID because the PS4Controller class extends the class "GenericHID")
    // ps4.setRumble(kRightRumble, 0.5); // rumbles the ps4 controller
  }
  
  // this method is ran periodically if the robot is in teleop
  @Override
  public void teleopPeriodic() {
    /* robot uses  drive
      that means that the left stick on the PS4 controller controls the motors/wheels on the left side of the robot, and right stick controls the right side
    */
    ps4.setRumble(RumbleType.kLeftRumble, 0.9);
    ps4.setRumble(RumbleType.kRightRumble, 0.9);

    Mecanum.driveCartesian(ps4.getLeftY(), ps4.getLeftX(), ps4.getRightX(), 0.0);
    // mecanum drive. takes input of left joystick to move left right/foreward back. takes left x input to rotate(I assume). last input is gyroAngle(dont know use)

    //raw mecanum code(move forward)
    /*if (ps4.getLeftY() < 0.1){
      m_topLeft.set(ps4.getLeftY()*-1);
      m_bottomLeft.set(ps4.getLeftY()*-1);
      m_topRight.set(ps4.getLeftY());
      m_bottomRight.set(ps4.getLeftY());

    }
    else if (ps4.getLeftY() > 0.1){
      m_topLeft.set(ps4.getLeftY()*-1);
      m_bottomLeft.set(ps4.getLeftY()*-1);
      m_topRight.set(ps4.getLeftY());
      m_bottomRight.set(ps4.getLeftY());

    }
    else{
      m_topLeft.stopMotor();
      m_bottomLeft.stopMotor();
      m_topRight.stopMotor();
      m_bottomRight.stopMotor();
    }
    //move back

    /*
    //go to right
    else if (ps4.getRightX() > 0.1){
      m_topLeft.set(ps4.getRightX());
      m_bottomLeft.set(ps4.getRightX()*-1);
      m_topRight.set(ps4.getRightX()*-1);
      m_bottomRight.set(ps4.getRightX());
    }
    //go to left
    else if (ps4.getRightX() < -0.1){
      m_topLeft.set(ps4.getLeftY());
      m_bottomLeft.set(ps4.getLeftY()*-1);
      m_topRight.set(ps4.getLeftY()*-1);
      m_bottomRight.set(ps4.getLeftY());
    }
    */



    
    
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
      Mecanum.driveCartesian(0.5, 0.5, 0.0, 0.0);


    }
    else
    {
      time.stop();
      Mecanum.stopMotor();
      //stop motor
    }
  }

  @Override
  public void testPeriodic()
  {
    System.out.println("hi");
  }
}
