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
import edu.wpi.first.wpilibj.Joystick; //controller or joystick

public class Robot extends TimedRobot {
  WPI_VictorSPX _rightBack = new WPI_VictorSPX(1);
  WPI_VictorSPX _rightFront = new WPI_VictorSPX(2);
  WPI_VictorSPX _leftBack = new WPI_VictorSPX(3);  
  WPI_VictorSPX _leftFront = new WPI_VictorSPX(4); 
  DifferentialDrive _diffDrive = new DifferentialDrive(_leftBack, _rightBack);

  Joystick _joystick = new Joystick(0);

  Faults _faults_L = new Faults();
  Faults _faults_R = new Faults();

  @Override
  public void teleopPeriodic() {

        String work = "";

        /* get gamepad stick values */
        //multiples the y value (raw axis) of the L-stick of the joystick by -1 to get a final result
        double turn = +1 * _joystick.getRawAxis(0); /* positive is right */ //original value is +1  
        double forw = -1 * _joystick.getRawAxis(1); /* positive is forward */ //original value is -1 
        boolean btn1 = _joystick.getRawButton(1); /* is button is down, print joystick values */

        /* deadband gamepad 10% */
        if (Math.abs(forw) < 0.10) {
            forw = 0;
        }
        if (Math.abs(turn) < 0.10) {
            turn = 0;
        }

        /* drive robot */
        _diffDrive.arcadeDrive(forw, turn);

        /*
         * [2] Make sure Gamepad Forward is positive for FORWARD, and GZ is positive for
         * RIGHT
         */
        work += " GF:" + forw + " GT:" + turn;

        /* get sensor values */
        // double leftPos = _leftFront.GetSelectedSensorPosition(0);
        // double rghtPos = _rghtFront.GetSelectedSensorPosition(0);
        double leftVelUnitsPer100ms = _leftBack.getSelectedSensorVelocity(0);
        double rightVelUnitsPer100ms = _rightBack.getSelectedSensorVelocity(0);

        work += " L:" + leftVelUnitsPer100ms + " R:" + rightVelUnitsPer100ms;

        /*
         * drive motor at least 25%, Talons will auto-detect if sensor is out of phase
         */
        // *faults are for debugging, checking problems
        _leftBack.getFaults(_faults_L); // *use this implementation
        _rightBack.getFaults(_faults_R); // *use this implementation

        if (_faults_L.SensorOutOfPhase) { // *use this implementation
            work += " L sensor is out of phase";
        }
        if (_faults_R.SensorOutOfPhase) { // *use this implementation
            work += " R sensor is out of phase";
        }

        /* print to console if btn1 is held down */
        // *debugging
        if (btn1) {
            System.out.println(work);
        }
    }

    @Override
    public void robotInit() {
        /* factory default values */
        _rightBack.configFactoryDefault();
        _rightFront.configFactoryDefault();
        _leftBack.configFactoryDefault();
        _leftFront.configFactoryDefault();

        /* set up followers */
        _rightFront.follow(_rightBack);
        _leftFront.follow(_leftBack);

        /* [3] flip values so robot moves forward when stick-forward/LEDs-green */
        _rightBack.setInverted(true); // !< Update this
        _leftBack.setInverted(false); // !< Update this

        /*
         * set the invert of the followers to match their respective master controllers
         */
        _rightFront.setInverted(InvertType.FollowMaster);
        _leftFront.setInverted(InvertType.FollowMaster);

        /*
         * [4] adjust sensor phase so sensor moves positive when Talon LEDs are green
         */
        _rightBack.setSensorPhase(true);
        _leftBack.setSensorPhase(true);
    }
}
