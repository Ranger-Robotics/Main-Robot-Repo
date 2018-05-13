package org.usfirst.frc.team6132.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;


public class Robot extends TimedRobot {
	
	private SpeedController motor0 = new Talon(0);
	private SpeedController motor1 = new Talon(1);
	private SpeedController motor2 = new Talon(2);
	private SpeedController motor3 = new Talon(3);
	private SpeedController motor4 = new Talon(4);
	private SpeedController motor5 = new Talon(5);
	
	private Joystick stick = new Joystick(0); // initialize the joystick on port
	private Joystick stick2 = new Joystick(1);
	
	//Grabber Cyclinder 1
	Solenoid solenoid0 = new Solenoid(0);
	Solenoid solenoid1 = new Solenoid(1);
		
	//Grabber Cyclinder 2
	Solenoid solenoid2 = new Solenoid(2);
	Solenoid solenoid3 = new Solenoid(3);
		
	//Grabber Extender
	Solenoid solenoid4 = new Solenoid(4);
	Solenoid solenoid5 = new Solenoid(5);
	
	private AnalogGyro Gyro = new AnalogGyro(0);
	
	double Deadzone = .1;
	double SpeedModifier = 1;
	double TargetDirection = 0;
	double RotationRate = 0.4;
	double moveRate = 0.4;
		
	void setLeft(double value) {
		value = value * 0.846; //Was 0.846
		motor0.set(value);
		motor1.set(value);
	}

	void setRight(double value) {
		motor2.set(-value);
		motor3.set(-value);
	}
	
	public void robotInit() {
		Gyro.initGyro();
		
	}
	
	public void disabledInit() {
		
	}
	
	public void disabledPeriodic() {

	}
	
	public void autonomousInit() {
		
	}
	
	
	public void autonomousPeriodic() {

	}
	
	public void teleopInit() {
		
	}
	
	public void teleopPeriodic() {
		
		if (Math.abs(stick.getY()) > Deadzone && Math.abs(stick.getX()) > Deadzone) {
		
			TargetDirection = Math.toDegrees(Math.asin(stick.getX() / Math.sqrt(stick.getX() * stick.getX() + stick.getY() * stick.getY())));
			if (stick.getY() < 0) {
				TargetDirection = 180 - TargetDirection;
			}
		
		}
		
		double twoSideGyroAngle = Gyro.getAngle();
		
		if (Gyro.getAngle() <= 180) {
			twoSideGyroAngle = Gyro.getAngle();
		}
		
		if (Gyro.getAngle() > 180) {
			twoSideGyroAngle = -180 + (Gyro.getAngle() - 180);
		}
		
		if (twoSideGyroAngle < TargetDirection) {
			setLeft(RotationRate);
			setRight(-RotationRate);
		}
		
		if (twoSideGyroAngle > TargetDirection) {
			setLeft(-RotationRate);
			setRight(RotationRate);
		}
		
		if (twoSideGyroAngle == TargetDirection) {
			setLeft(0);
			setRight(0);
		}
		
		if (twoSideGyroAngle - TargetDirection < 0.5 && Math.abs(stick.getY()) > Deadzone && Math.abs(stick.getX()) > Deadzone) {
			setLeft(moveRate);
			setRight(moveRate);
		}
		
		
		
		
	}
	
	public void testPeriodic() {
		
	}
	
}