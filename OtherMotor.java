// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//ctre
import com.ctre.phoenix.motorcontrol.Faults; //debugger
import com.ctre.phoenix.motorcontrol.InvertType; //inverted
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX; //motorcontroller

//REV sparkmotors
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonFX;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.Joystick; //general controller import
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Timer;

//limelight
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

//lime light in void func

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
    PWMTalonSRX _feeder = new PWMTalonSRX(7);
    PWMSparkMax _shooter = new PWMSparkMax(5);
    

    //timer (for autonomous)
    Timer time = new Timer();

    // controllers
    PS4Controller ps4 = new PS4Controller(0);
    Joystick joystick = new Joystick(1);
    
    @Override
    public void autonomousPeriodic()
    {
        if (time.get() < 5.0)
        {
            
        }
    }
    @Override
    public void teleopPeriodic() {
        _diffDrive.tankDrive(ps4.getLeftY(), ps4.getRightY());

        if (ps4.getCrossButtonPressed())
        {
            
        }
        //create a method for the motors to spin
       
    }

    @Override
    public void robotInit() {
        Ultrasonic.setAutomaticMode(true);
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
