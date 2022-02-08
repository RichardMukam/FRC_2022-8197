/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;


import edu.wpi.first.networktables.*;
import frc.robot.subsystems.*;
import frc.robot.RobotMap;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private DriveTrain m_drive;
  private TalonFX shooterController;
  private TalonSRX intakeController;
  private TalonSRX elevatorController;
  private TalonSRX turretController;
  private Joystick m_controller;
  private ColorSensorWrapper m_color;
  private JoystickButton a_button, x_button;

  // network tables
  private NetworkTable table;
  NetworkTableEntry tx, ty, ta;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    m_drive = new DriveTrain(RobotMap.FRONT_LEFT_MOTOR, RobotMap.FRONT_RIGHT_MOTOR,
                             RobotMap.BACK_LEFT_MOTOR, RobotMap.BACK_RIGHT_MOTOR);
    intakeController = new TalonSRX(RobotMap.INTAKE_MOTOR);
    intakeController.setInverted(true);
    elevatorController = new TalonSRX(RobotMap.ELEVATOR_MOTOR);
    shooterController = new TalonFX(RobotMap.SHOOTER_MOTOR);
    turretController = new TalonSRX(RobotMap.TURRET_MOTOR);
    m_controller = new Joystick(RobotMap.CONTROLLER_ID);
    a_button = new JoystickButton(m_controller, RobotMap.CONTROLLER_A_BUTTON);
    x_button = new JoystickButton(m_controller, RobotMap.CONTROLLER_X_BUTTON);
    m_color = ColorSensorWrapper.getInstance();
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    ta = table.getEntry("ta");

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    double leftOutput = -m_controller.getRawAxis(RobotMap.DRIVE_FORWARD_AXIS) + m_controller.getRawAxis(RobotMap.DRIVE_TURN_AXIS);
    double rightOutput = -m_controller.getRawAxis(RobotMap.DRIVE_FORWARD_AXIS) - m_controller.getRawAxis(RobotMap.DRIVE_TURN_AXIS);
    if((m_controller.getRawAxis(RobotMap.CONTROLLER_LEFT_TRIGGER_AXIS) < 0.5)
    && (m_controller.getRawAxis(RobotMap.CONTROLLER_RIGHT_TRIGGER_AXIS) < 0.5)) {
      leftOutput *= 0.6;
      rightOutput *= 0.6;
    }
   m_drive.setSpeed(leftOutput, -rightOutput);
   if(m_controller.getRawButton(RobotMap.CONTROLLER_X_BUTTON)){
      //intakeController.set(ControlMode.PercentOutput, -1.5);
      elevatorController.set(ControlMode.PercentOutput, 0.5);
      //turretController.set(ControlMode.PercentOutput, 0.5);
    } else if (m_controller.getRawButton(RobotMap.CONTROLLER_LEFT_BUTTON)){
      shooterController.set(ControlMode.PercentOutput, -20.5); // originally -7.5
    } else if (m_controller.getRawbotton(RobotMap.CONTROLLER_A_BUTTON))
  {intakeController.set(ControlMode.PercentOutput,0.5);}
    else if(m_controller.getRawbotton(RobotMap.CONTROLLER_Y_BUTTON))
      intakeController.set(ControlMode.PercentOutput, 0);
      elevatorController.set(ControlMode.PercentOutput, 0.5 );
      shooterController.set(ControlMode.PercentOutput, 0);
      turretController.set(ControlMode.PercentOutput, 0);
    }
    

  @Override
  public void testInit() {
    SmartDashboard.putNumber("Red", 0);
    SmartDashboard.putNumber("Green", 0);
    SmartDashboard.putNumber("Blue", 0);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    int[] c;
    c = m_color.getColorValue();
    SmartDashboard.putNumber("Red", (double) c[0]);
    SmartDashboard.putNumber("Green", (double) c[1]);
    SmartDashboard.putNumber("Blue", (double) c[2]);
    if (m_controller.getRawButtonPressed(RobotMap.CONTROLLER_A_BUTTON))
      System.out.println("R: " + c[0] + ", G: " + c[1] + ", B: " + c[2]);
    
    // limelight
    double x = tx.getDouble (0.0);
    double y = ty.getDouble (0.0);
    double area = ta.getDouble (0.0);
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);
  }
}
