// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;



import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonFX;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

//adding ps4 controls
import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;


/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {
  //driving motors
  private final PWMTalonFX m_topLeft = new PWMTalonFX(4); // the top left motor - intiailizes the motor
 // private final PWMSparkMax m_topLeft = new PWMSparkMax(4);
  private final PWMTalonFX m_bottomLeft = new PWMTalonFX(0); // the bottom left motor - intiailizes the motor
 // private final PWMSparkMax m_bottomLeft = new PWMSparkMax(4);
  MotorControllerGroup leftMotors = new MotorControllerGroup(m_topLeft, m_bottomLeft); // groups both of the motors on the left side to an object we'll refer later when we use differential drive

  private final PWMTalonFX m_topRight = new PWMTalonFX(2); // the top right motor - intiailizes the motor
  private final PWMTalonFX m_bottomRight = new PWMTalonFX(3); // the bottom right motor - intiailizes the motor
  private final MotorControllerGroup rightMotors = new MotorControllerGroup(m_topRight, m_bottomRight); // groups both of the motors on the left side to an object we'll refer later 

  private final DifferentialDrive drivingMotors = new DifferentialDrive(leftMotors, rightMotors); // differential drive for the motors (can now use tank drive)

  //shooter motors
  private final PWMTalonFX m_lShooterMotor = new PWMTalonFX(5); // the climber motor on the left side - intiailizes the motor
  private final PWMTalonFX m_rShooterMotor = new PWMTalonFX(1); // the climber motor on the right side - intiailizes the motor
  
  //private final DifferentialDrive m_externalMotorDrive = new DifferentialDrive(m_lShooterMotor, m_rShooterMotor);
  
  //adding ps4 controls
  private final PS4Controller ps4 = new PS4Controller(0);

  //creating an object for the timer
  private final Timer time = new Timer();

  @Override
  public void robotInit() {
    /* We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.*/
    rightMotors.setInverted(true);
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
    /* robot uses tank drive
      that means that the left stick on the PS4 controller controls the motors/wheels on the left side of the robot, and right stick controls the right side
    */
    ps4.setRumble(RumbleType.kLeftRumble, 0.9);
    ps4.setRumble(RumbleType.kRightRumble, 0.9);

    drivingMotors.tankDrive(-0.4* ps4.getLeftY(), -0.4  * ps4.getRightY()); // we want positve (+) values to move forwards, and negative (-) to move backwards

    while (ps4.getL2Button() == true) 
    {
      m_lShooterMotor.set(-1); // spins the motor
      m_rShooterMotor.set(-1); // spins the motor
    }
    
    while (ps4.getR2ButtonPressed() == true) // if you press the X button (cross) on the controller, it'll stop the motor from spinning.
    {
      m_lShooterMotor.stopMotor();
      m_rShooterMotor.stopMotor();
    }
    System.out.println("teleop works");

    //m_externalMotorDrive.arcadeDrive(-ps4.getRightY(), 0.0); //shooter motor controller

    //m_externalMotorDrive.setSpeed()
    
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
    if (time.get() <= 15.0) // our autonomous period will run for five (5) seconds 
    {
      System.out.println("autonomous works: 3 sec");
      System.out.println(time.get());
      drivingMotors.tankDrive(-0.5, 0.5);
    }
    else
    {
      drivingMotors.stopMotor();
      time.stop();
      // ps4.setRumble(kLeftRumble, 0.5); // rumble the controller
      // ps4.setRumble(kRightRumble, 0.5); // rumble the controller
    }
  }

  @Override
  public void testPeriodic()
  {
    System.out.println("hi");
  }
}
