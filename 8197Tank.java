// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//limelight


//ctre
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {
    // right side
    WPI_VictorSPX _rightBack = new WPI_VictorSPX(1);
    WPI_VictorSPX _rightFront = new WPI_VictorSPX(2);
    MotorControllerGroup _driveRight = new MotorControllerGroup(_rightBack, _rightFront);
    // left side
    WPI_VictorSPX _leftBack = new WPI_VictorSPX(3);
    WPI_VictorSPX _leftFront = new WPI_VictorSPX(4);
    MotorControllerGroup _driveLeft = new MotorControllerGroup(_leftBack, _leftFront);
    // set up for differential drive
    DifferentialDrive _diffDrive = new DifferentialDrive(_driveLeft, _driveRight); 

    //sensors
    //Ultrasonic _Ultrasonic = new Ultrasonic(1,1);

    //timer (for autonomous)
    Timer time = new Timer();

    // controllers
    PS4Controller ps4 = new PS4Controller(0);
    
    @Override
    public void robotInit() {
        //Ultrasonic.setAutomaticMode(true);
        /* factory default values */
        _rightBack.configFactoryDefault();
        _rightFront.configFactoryDefault();
        _leftBack.configFactoryDefault();
        _leftFront.configFactoryDefault();

      /* [3] flip values so robot moves forward when stick-forward/LEDs-green */
        _rightBack.setInverted(false); // !< Update this
        _leftBack.setInverted(false); // !< Update this

        /* set up followers */
        _rightFront.follow(_rightBack);
        _leftFront.follow(_leftBack);

        /*
         * set the invert of the followers to match their respective master controllers
         */
        _rightFront.setInverted(InvertType.FollowMaster);
        _leftFront.setInverted(InvertType.FollowMaster);

        /*
         * [4] adjust sensor phase so sensor moves positive when Talon LEDs are green
         */
        _rightBack.setSensorPhase(false);
        _leftBack.setSensorPhase(true);
    }
    
    @Override
    public void teleopPeriodic() {
        
        double m_lMotors = -ps4.getLeftY();
        double m_rMotors = ps4.getRightY();

        if (Math.abs(m_lMotors) < 0.10) {
            m_lMotors = 0;
        }
        if (Math.abs(m_rMotors) < 0.10) {
            m_rMotors = 0;
        }
        
        _diffDrive.tankDrive(m_lMotors, m_rMotors);
    }

    @Override
    public void autonomousInit()
    {
        time.reset();
        time.start();
    }

    @Override
    public void autonomousPeriodic()
    {
        if (time.get() <= 1.0)
        {
            _diffDrive.tankDrive(-0.4, 0.4);
            System.out.println("move forward");
        }
        if (time.get() > 1.0 && time.get() < 2.0)
        {
            _diffDrive.tankDrive(0.6, 0.6);
            System.out.println("turn");
        }
        /*
        if(_Ultrasonic.getRangeInches() > 12) {
           // The manuever the robot should do
           _diffDrive.tankDrive(.5, .5);
           // *.5 stands for the speed meaning that the right and left speed should be .5
        }
        else {
           //if not, the robot should move
           _diffDrive.tankDrive(0, 0);
        }*/
    }
    
}
