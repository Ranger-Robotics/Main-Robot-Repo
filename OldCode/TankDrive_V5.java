/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6132.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;


public class Robot extends TimedRobot {


	private SpeedController motor0 = new Talon(0); // initialize the motor as a
	private SpeedController motor1 = new Talon(1);
	private SpeedController motor2 = new Talon(2);
	private SpeedController motor3 = new Talon(3);// Talon on channel 0
	private SpeedController motor4 = new Talon(4);

	private Joystick stick = new Joystick(0); // initialize the joystick on port
	private Joystick stick2 = new Joystick(3);
	Thread m_visionThread;// 0
	double DZ = .1;
	double SpeedModifier = 1;
	double liftSpeedMod;
	String startingPosition = "left";


	@Override
	public void robotInit() {


	}

	void turnRight() {
		setLeft(0.3);
		setRight(0.3);
		Timer.delay(0.73);
		setLeft(0);
		setRight(0);
	}

	void setLeft(double value) {
		value = value * 0.846;
		motor0.set(value);
		motor1.set(value);
	}

	void setRight(double value) {
		motor2.set(value);
		motor3.set(value);
	}

	void setLift(double value) {
		motor4.set(value);
	}


	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {

	}


	@Override
	public void autonomousInit() {
		String gameData = DriverStation.getInstance().getGameSpecificMessage();

		if(gameData.charAt(0) == 'L') {
			double autoModeSpeed = 0.3;
			setLeft(autoModeSpeed);
			setRight(-autoModeSpeed);
			Timer.delay(4.44);
			setLeft(0);
			setRight(0);
			Timer.delay(1);
			turnRight();
		} else {
			//Put right auto code here
		}
	}


	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void teleopInit() {

	}


	@Override
	public void teleopPeriodic() {



			if (stick.getY() < -DZ) {
				setLeft(-stick.getY()*SpeedModifier);

			} else if (stick.getY() > DZ) {
				setLeft(-stick.getY()*SpeedModifier);

			} else {
				setLeft(0);
			}

			if  (stick.getRawAxis(5) < -DZ) {
				setRight(stick.getRawAxis(5)*SpeedModifier);

			} else if (stick.getRawAxis(5) > DZ) {
				setRight(stick.getRawAxis(5)*SpeedModifier);

			} else if (stick.getRawAxis(3) > 0.5) {
				setLeft(1*SpeedModifier);
				setRight(-1*SpeedModifier);

			} else {

				setRight(0);
			}

			if (stick.getRawButton(6)) {
		    	SpeedModifier = 0.25;
			} else {
				SpeedModifier = 1;
			}

			liftSpeedMod = stick2.getRawAxis(3);

			if (stick2.getRawAxis(1) < -0.2) {
				setLift(stick2.getRawAxis(1));
			}

			if (stick2.getRawAxis(1) > 0.2) {
				setLift(stick2.getRawAxis(1));
			}
	}


	@Override
	public void testPeriodic() {
	}
}
