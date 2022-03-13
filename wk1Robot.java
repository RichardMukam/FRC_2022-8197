// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//ctre
import com.ctre.phoenix.motorcontrol.Faults; //debugger (use later)
import com.ctre.phoenix.motorcontrol.InvertType; //inverted
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX; //motorcontroller

//general imports
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.Joystick; //general controller import
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

//rev spark max
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

//talonsrx
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

//limelight
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Robot extends TimedRobot {
    // right side
    WPI_VictorSPX rightBack = new WPI_VictorSPX(1);
    WPI_VictorSPX rightFront = new WPI_VictorSPX(2);
    MotorControllerGroup rightMotors = new MotorControllerGroup(rightBack, rightFront);
    // left side
    WPI_VictorSPX leftBack = new WPI_VictorSPX(3);
    WPI_VictorSPX leftFront = new WPI_VictorSPX(4);
    MotorControllerGroup leftMotors = new MotorControllerGroup(leftBack, leftFront);
    // set up for differential drive
    DifferentialDrive _diffDrive = new DifferentialDrive(leftMotors, rightMotors); //this was originally driveRight,driveLeft, but should've been driveLeft,driveRight
    TalonSRX feeder = new TalonSRX(7);
    TalonSRX hood = new TalonSRX(6);                                                                                             
    CANSparkMax shooter = new CANSparkMax(5, MotorType.kBrushless); //neo motor
    
    //timer (for autonomous)
    Timer time = new Timer();

    // controllers
    PS4Controller ps4 = new PS4Controller(0);
    Joystick joystick = new Joystick(1);
    
    @Override
    public void autonomousPeriodic()
    {
        /*if (time.get() < 5.0)
        {
            drive   
        }*/
    }

    @Override
    public void teleopPeriodic() {
        //rumble the controller
        ps4.setRumble(RumbleType.kLeftRumble, 0.5);
        ps4.setRumble(RumbleType.kRightRumble, 0.5);
        //to drive
        _diffDrive.tankDrive(ps4.getLeftY(), ps4.getRightY());
        //shooter/feeder
        if (joystick.getRawButton(1)) //spin the feeder/shooter motors forward (in) (trigger button)
        {
            shooter.set(1.0);
            //hood.set(ControlMode.PercentOutput, 0.5);
            feeder.set(ControlMode.PercentOutput, 1.0);            
        }
        else if (joystick.getRawButton(2)) //spin the feeder/shooter motors backwards (reverse) (side button)
        {
            feeder.set(ControlMode.PercentOutput, -0.25);
            //hood.set(ControlMode.PercentOutput, -0.5);
        }
        else
        {
            feeder.set(ControlMode.PercentOutput, 0.0);
            hood.set(ControlMode.PercentOutput, 0.0);
            shooter.set(0.0);
        }
        //limelight
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        NetworkTableEntry tx = table.getEntry("tx");
        NetworkTableEntry ty = table.getEntry("ty");
        NetworkTableEntry ta = table.getEntry("ta");

        //read values periodically
        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double area = ta.getDouble(0.0);

        //post to smart dashboard periodically
        SmartDashboard.putNumber("LimelightX", x);
        SmartDashboard.putNumber("LimelightY", y);
        SmartDashboard.putNumber("LimelightArea", area);

        System.out.println(x);
        System.out.println(y);
        System.out.println(area);
    }

    @Override
    public void robotInit() {
        //rumbling the controller as soon as the robot turns on
        ps4.setRumble(RumbleType.kLeftRumble, 0.1);
        ps4.setRumble(RumbleType.kRightRumble, 0.1);
        /* factory default values */
        rightBack.configFactoryDefault();
        rightFront.configFactoryDefault();
        leftBack.configFactoryDefault();
        leftFront.configFactoryDefault();

      /* [3] flip values so robot moves forward when stick-forward/LEDs-green */
        rightBack.setInverted(false); // !< Update this
        leftBack.setInverted(false); // !< Update this

        /* set up followers */
        rightFront.follow(rightBack);
        leftFront.follow(leftBack);

        /*
         * set the invert of the followers to match their respective master controllers
         */
        rightFront.setInverted(InvertType.FollowMaster);
        leftFront.setInverted(InvertType.FollowMaster);
    }
}
