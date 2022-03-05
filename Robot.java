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
import edu.wpi.first.wpilibj.Timer;

//spark max
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

//talonsrx
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;


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
        if (time.get() < 5.0)
        {
            rightBack.set(0.4);
        }
    }
    @Override
    public void teleopPeriodic() {
        _diffDrive.tankDrive(ps4.getLeftY(), ps4.getRightY());
        //create a method for the motors to spin
       
    }

    @Override
    public void testPeriodic()
    {
        //test the feeder and shooter motor
        if (joystick.getRawButton(1)) //spin the feeder/shooter motors forward (in)
        {
            shooter.set(1.0);
            hood.set(ControlMode.PercentOutput, 1.0);
            feeder.set(ControlMode.PercentOutput, 1.0);            
        }

        else if (joystick.getRawButton(2)) //spin the feeder/shooter motors backwards (reverse)
        {
            feeder.set(ControlMode.PercentOutput, -0.5);
            hood.set(ControlMode.PercentOutput, -0.5);
            shooter.set(0.0);
        }
        else
        {
            feeder.set(ControlMode.PercentOutput, 0.0);
            hood.set(ControlMode.PercentOutput, 0.0);
            shooter.set(0.0);
        }

    }

    @Override
    public void robotInit() {
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
/*
ps4.getCrossedButton - when this method is used, so long as the button is held, the motor will continue 
to spin
ps4.getCrossedButtonPressed - when this method is used, even if the cross button is held, the command will 
only be executed once. 
    in other words, when the button is held, the motor will not continue to spin
*/
