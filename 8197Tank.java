// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//ctre
import com.ctre.phoenix.motorcontrol.Faults; //debugger
import com.ctre.phoenix.motorcontrol.InvertType; //inverted
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX; //motorcontroller

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.Joystick; //general controller import
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
    DifferentialDrive _diffDrive = new DifferentialDrive(_driveLeft, _driveRight); //this was originally driveRight,driveLeft, but should've been driveLeft,driveRight

    //sensors
    Ultrasonic _Ultrasonic = new Ultrasonic(1,1);

    //timer (for autonomous)
    Timer time = new Timer();

    // controllers
    Joystick _joystick = new Joystick(0);
    PS4Controller ps4 = new PS4Controller(0);

    // debug
    Faults _faults_L = new Faults();
    Faults _faults_R = new Faults();
    
    @Override
    public void autonomousInit()
    {
        time.reset();
        time.start();
    }
    @Override
    public void autonomousPeriodic()
    {
        if (time.get() <= 5.0)
        {
            _diffDrive.tankDrive(0.2, 0.2);
        }
        else if (time.get() > 5.0 && time.get() < 15.0)
        {
            _diffDrive.tankDrive(-0.5, 0.5);
        }
        else if (time.get() > 15.0)
        {
            _diffDrive.stopMotor();
            time.stop();
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
    @Override
    public void teleopPeriodic() {

        //String work = ""; debug

        /* get gamepad stick values */
        // multiples the y value (raw axis) of the L-stick of the joystick by -1 to get
        // a final result
        // double turn = +1 * _joystick.getRawAxis(0); /* positive is right */
        // //original value is +1 use this when using arcade drive
        double lSide = -1 * ps4.getLeftY(); // + is forward, getting the y-value of the L-stick, moving up goes fo
        double rSide = +1 * ps4.getRightY(); // + is forwad, gets y-value of R-stick

        /* deadband gamepad 10% */ // basically mistaken movement of L-stick or R-stick
        if (Math.abs(lSide) < 0.10) {
            lSide = 0;
        }
        if (Math.abs(rSide) < 0.10) {
            rSide = 0;
        }
        /*
         * if (Math.abs(turn) < 0.10) { //for arcade
         * turn = 0;
         * }
         */

        /* drive robot */
        // _diffDrive.arcadeDrive(forw, turn); //arcade drive
        _diffDrive.tankDrive(lSide, rSide);

        /*
         * [2] Make sure Gamepad Forward is positive for FORWARD, and GZ is positive for
         * RIGHT
         */
        // work += " GF:" + forw; //+ " GT:" + turn

        /* get sensor values */
        // double leftPos = _leftFront.GetSelectedSensorPosition(0);
        // double rghtPos = _rghtFront.GetSelectedSensorPosition(0);
        //double leftVelUnitsPer100ms = _leftBack.getSelectedSensorVelocity(0);
        //double rightVelUnitsPer100ms = _rightBack.getSelectedSensorVelocity(0);

        //work += " L:" + leftVelUnitsPer100ms + " R:" + rightVelUnitsPer100ms;

        /*
         * drive motor at least 25%, Talons will auto-detect if sensor is out of phase
         */
        // *faults are for debugging, checking problems
        _leftBack.getFaults(_faults_L); // *use this implementation
        _rightBack.getFaults(_faults_R); // *use this implementation

       /* if (_faults_L.SensorOutOfPhase) { // *use this implementation
            work += " L sensor is out of phase";
        }
        if (_faults_R.SensorOutOfPhase) { // *use this implementation
            work += " R sensor is out of phase";
        }*/

    }

    @Override
    public void robotInit() {
        Ultrasonic.setAutomaticMode(true);
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

}
