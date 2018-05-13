/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

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


	private SpeedController motor0 = new Talon(0); // initialize the motor as a
	private SpeedController motor1 = new Talon(1);
	private SpeedController motor2 = new Talon(2);
	private SpeedController motor3 = new Talon(3);// Talon on channel 0
	private SpeedController motor4 = new Talon(4);
	private SpeedController motor5 = new Talon(5);
	private Servo servo1 = new Servo(9);
	
	
	static GenericHID.RumbleType RightRumble;
	static GenericHID.RumbleType LeftRumble;
	//public static final GenericHID.RumbleType RightRumble;
	private Servo servo2 = new Servo(8);
	private static final int kUltrasonicPort = 0;
	//private static final double kValueToInches = 0.125;

	private static final int kGyroPort = 0;
	Encoder encoder1 = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
	//private AnalogGyro m_gyro = new AnalogGyro(kGyroPort);
	//private AnalogInput m_ultrasonic = new AnalogInput(kUltrasonicPort);

	
	
	private Joystick stick = new Joystick(0); // initialize the joystick on port
	private Joystick stick2 = new Joystick(1);
	Thread m_visionThread;// 0
	double DZ = .1;
	double SpeedModifier = 1;
	double maxAcceleration = 0.05;
	double liftSpeedMod;
	double servo1Angle = 90;

	double servo2Angle = 90;
	//CHANGE THIS TO CHANGE STARTING POSITION
	String startingPosition = "left"; //ah24 or AH246
	
	//Grabber Cyclinder 1
	Solenoid solenoid0 = new Solenoid(0);
	Solenoid solenoid1 = new Solenoid(1);
	
	//Grabber Cyclinder 2
	Solenoid solenoid2 = new Solenoid(2);
	Solenoid solenoid3 = new Solenoid(3);
	
	//Grabber Extender
	Solenoid solenoid4 = new Solenoid(4);
	Solenoid solenoid5 = new Solenoid(5);
	
	boolean shouldGoSlow = false;
	boolean shouldGoMedium = false;



	@Override
	public void robotInit() {
		
		encoder1.setMaxPeriod(.1);
		encoder1.setMinRate(10);
		encoder1.setDistancePerPulse(5);
		encoder1.setReverseDirection(true);
		encoder1.setSamplesToAverage(7);
			
			m_visionThread = new Thread(() -> {
				// Get the UsbCamera from CameraServer
				UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
				// Set the resolution
				camera.setResolution(640, 480);

				// Get a CvSink. This will capture Mats from the camera
				CvSink cvSink = CameraServer.getInstance().getVideo();
				// Setup a CvSource. This will send images back to the Dashboard
				CvSource outputStream
						= CameraServer.getInstance().putVideo("Rectangle", 640, 480);

				// Mats are very memory expensive. Lets reuse this Mat.
				Mat mat = new Mat();

				// This cannot be 'true'. The program will never exit if it is. This
				// lets the robot stop this thread when restarting robot code or
				// deploying.
				while (!Thread.interrupted()) {
					// Tell the CvSink to grab a frame from the camera and put it
					// in the source mat.  If there is an error notify the output.
					if (cvSink.grabFrame(mat) == 0) {
						// Send the output the error.
						outputStream.notifyError(cvSink.getError());
						// skip the rest of the current iteration
						continue;
					}
					// Put a rectangle on the image
					Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
							new Scalar(255, 255, 255), 5);
					// Give the output stream a new image to display
					outputStream.putFrame(mat);
				}
			});
			m_visionThread.setDaemon(true);
			m_visionThread.start();
			
	}
	
	void turnLeft() {
		setLeft(-0.6);
		setRight(-0.6);
		Timer.delay(1.4);//1.94 but less umpj
		setLeft(0);
		setRight(0);
	}
	// Ignore this comment.

	void turnRight() {
		setLeft(0.3);
		setRight(0.3);
		Timer.delay(1.16);
		setLeft(0);
		setRight(0);
	}

	void setLeft(double value) {
		value = value * 0.846;
		if (Math.abs(motor0.get() - value) <= maxAcceleration) {
			motor0.set(value);
			motor1.set(value);
		} else {
			if (value > 0) {
				motor0.set(motor0.get() + maxAcceleration);
				motor1.set(motor0.get() + maxAcceleration);
			} else {
				motor0.set(motor0.get() - maxAcceleration);
				motor1.set(motor0.get() - maxAcceleration);
			}
		}
	}

	void setRight(double value) {
		if (Math.abs(motor2.get() - value) <= maxAcceleration) {
			motor2.set(value);
			motor3.set(value);
		} else {
			if (value > 0) {
				motor2.set(motor2.get() + maxAcceleration);
				motor3.set(motor2.get() + maxAcceleration);
			} else {
				motor2.set(motor2.get() - maxAcceleration);
				motor3.set(motor2.get() - maxAcceleration);
			}
		}
	}
	
	void setLeftAbs(double value) {
		value = value * 0.846;
		motor0.set(value);
		motor1.set(value);
	}
	
	void setRightAbs(double value) {
		motor2.set(value);
		motor3.set(value);
	}
	
	void setAllToZero() {
		motor0.set(0);
		motor1.set(0);
		motor2.set(0);
		motor3.set(0);
		motor4.set(0);
	}
	
	void goForward(double time, double speed) {
		setLeft(speed);
		setRight(-speed);
		Timer.delay(time);
		setLeft(0);
		setRight(0);
	}

	void setLift(double value) {
		motor4.set(value);
		motor5.set(value);
	}
	
	void reportBotInfo() {
		System.out.println(" ");
		System.out.println("Bot Status Info");
		System.out.println("====================");
		System.out.println("Starting Position: " + startingPosition);
	}
	
	void safePneumatics() {
		solenoid0.set(false);
		solenoid1.set(false);
		solenoid2.set(false);
		solenoid3.set(false);
		System.out.println("The Pneumatics have been safed.");
	}
	
	void closeGrabber() {
		solenoid0.set(true);
		solenoid1.set(false);
		System.out.println("Closing Grabber");
	}
	
	void openGrabber() {
		solenoid0.set(false);
		solenoid1.set(true);
		System.out.println("Opening Grabber");
	}
	
	void extendGrabber() {
		solenoid2.set(true);
		solenoid3.set(true);
		System.out.println("Extending Grabber");
	}
	
	void retractGrabber() {
		solenoid2.set(false);
		solenoid3.set(false);
		System.out.println("Retracting Grabber");
	}


	@Override
	public void disabledInit() {
		reportBotInfo();
	}

	@Override
	public void disabledPeriodic() {

	}


	@Override
	public void autonomousInit() {
		reportBotInfo();
		safePneumatics();
		
		/*
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		char ourSwitchPosition = gameData.charAt(0);
		char ScalePosition = gameData.charAt(1);
		
<<<<<<< HEAD
		if (startingPosition == "right") {
			if(ourSwitchPosition == 'L') { //The bot tries to go for our Switch.
				closeGrabber();
				Timer.delay(1);
				setLeftAbs(0.6);
				setRightAbs(-0.61);
				Timer.delay(1.68);
				setLeftAbs(0);
				setRightAbs(0);
				Timer.delay(1.5);
				turnRight();
				Timer.delay(0.5);
				setLift(-0.75);
				Timer.delay(4.5);
				setLift(0);
				goForward(0.2, 0.3);
				extendGrabber();
				Timer.delay(1.5);
				openGrabber();
			}
			
			if (ourSwitchPosition == 'R') {
				
				if (ScalePosition == 'L') { //If our switch is on the wrong side, try to go for the switch
					//Adjust timing to get to scale, this is set for switch
					
					closeGrabber();
					Timer.delay(1);
					setLeftAbs(0.6);
					setRightAbs(-0.605);
					Timer.delay(3.24);
					setLeftAbs(0);
					setRightAbs(0);
					
		if (gameData != null) {
			if (startingPosition == "left") {
				if(ourSwitchPosition == 'L') { //The bot tries to go for our Switch.
					closeGrabber();
					Timer.delay(1);
					goForward(1.68, 0.6);
					Timer.delay(1.5);
					turnRight();
					Timer.delay(0.5);
					setLift(-0.75);
					Timer.delay(4.5);
					setLift(0);
					goForward(0.2, 0.3);
					extendGrabber();
					Timer.delay(1.5);
					openGrabber();
				}
				
				if (ourSwitchPosition == 'R') {
					
					if (ScalePosition == 'L') { //If our switch is on the wrong side, try to go for the switch
						//Adjust timing to get to scale, this is set for switch
						
						closeGrabber();
						Timer.delay(1);
						goForward(3.44, 0.6);
						Timer.delay(1.5);
						turnRight();
						Timer.delay(0.5);
						setLift(-1);
						Timer.delay(6.5);
						setLift(0);
						goForward(0.2, 0.3);
						extendGrabber();
						Timer.delay(1.5);
						openGrabber();
					
					} else if (ScalePosition == 'R') { //If both the switch and the scale are wrong, go straight
						closeGrabber();
						Timer.delay(1);
						Timer.delay(0.5);
						setLift(-0.75);
						Timer.delay(4.5);
						setLift(0);
						goForward(1.68, 0.6);
						Timer.delay(1.5);
						extendGrabber();
					}
					
					
				}
			}
		}
		
		if (startingPosition == "center") {
			closeGrabber();
			Timer.delay(1);
			Timer.delay(0.5);
			setLift(-0.75);
			Timer.delay(4.5);
			setLift(0);
			goForward(1.68, 0.6); // Just go straight accross the autoline
			Timer.delay(1.5);
			extendGrabber();
		}
		
		//
		if (startingPosition == "left") {
			if (ourSwitchPosition == 'R') {
				closeGrabber();
				Timer.delay(1);
				setLeftAbs(0.61);
				setRightAbs(-0.6);
				Timer.delay(1.68);
				setLeftAbs(0);
				setRightAbs(0);
				Timer.delay(1.5);
				turnLeft();
			
			if (startingPosition == "center") {
				closeGrabber();
				Timer.delay(1);
				Timer.delay(0.5);
				setLift(-0.75);
				Timer.delay(4.5);
				setLift(0);
				goForward(1.68, 0.6); // Just go straight accross the autoline
				Timer.delay(1.5);
				extendGrabber();
			}
			
			
			if (startingPosition == "right") {
				if (ourSwitchPosition == 'R') {
					closeGrabber();
					Timer.delay(1);
					setLeftAbs(0.605);
					setRightAbs(-0.6);
					Timer.delay(3.24); //This was 3.24 before
					setLeftAbs(0);
					setRightAbs(0);
					goForward(1.68, 0.6);
					Timer.delay(1.5);
					turnLeft();
					Timer.delay(0.5);
					setLift(-0.75);
					Timer.delay(4.5);
					setLift(0);
					goForward(0.2, 0.3);
					extendGrabber();
					Timer.delay(1.5);
					openGrabber();
				}
				
				if (ourSwitchPosition == 'L') {
					if (ScalePosition == 'R') {
						closeGrabber();
						Timer.delay(1);
						goForward(3.24, 0.6);
						Timer.delay(1.5);
						turnLeft();
						Timer.delay(0.5);
						setLift(-1);
						Timer.delay(5.5);
						setLift(0);
						goForward(0.2, 0.3);
						extendGrabber();
						Timer.delay(1.5);
						openGrabber();
					}
					
					if (ScalePosition == 'L') {
						closeGrabber();
						Timer.delay(1);
						Timer.delay(0.5);
						setLift(-0.75);
						Timer.delay(4.5);
						setLift(0);
						goForward(1.68, 0.6);
						Timer.delay(1.5);
						extendGrabber();
					}
				}
				
				
				
			}
			
			
			
		}//
		} else {
			closeGrabber();
			Timer.delay(1);
			Timer.delay(0.5);
			setLift(-0.75);
			Timer.delay(4.5);
			setLift(0);
			goForward(1.68, 0.6);
			Timer.delay(1.5);
			extendGrabber();
		}
		
		System.out.println("Autonomous mode has finished.");
		*/
		
		//Brendino auto
		closeGrabber();
		Timer.delay(1);
		goForward(0.2, 0.3);
		goForward(1.68, 0.6);
		setLeft(0);
		setRight(0);
		Timer.delay(1);
		motor2.set(-.5);
		motor3.set(-.5);
		Timer.delay(.5);
		motor3.set(0);
		motor3.set(0);
		
		//Just goes straight
	}


	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void teleopInit() {
		System.out.println("Teleop has started.");
		SpeedModifier = 1;
		reportBotInfo();
		
		safePneumatics();
	}


	@Override
	public void teleopPeriodic() {

			
			if (stick.getPOV() == 270) {
				servo1Angle = servo1Angle + 7.5;
				servo1.setAngle(servo1Angle);
				Timer.delay(0.05);
				servo1Angle = 90;
				servo1.setAngle(servo1Angle);
				System.out.println("Camera Left");
				
			}
			if (stick.getPOV() == 90) {
	
				servo1Angle = servo1Angle - 15.5;
				servo1.setAngle(servo1Angle);
				Timer.delay(0.05);
				servo1Angle = 90;
				servo1.setAngle(servo1Angle);
				System.out.println("Camera Right");
			}
			
			if (stick.getPOV() == 0) { //REMOVE THIS
	
				servo2Angle = servo2Angle - 10.5;
				servo2.setAngle(servo2Angle);
				Timer.delay(0.05);
				servo2Angle = 90;
				servo2.setAngle(servo2Angle);
				System.out.println("Camera Up");
			}
			
			if (stick.getPOV() == 180) { //REMOVE THIS
				servo2Angle = servo2Angle + 7.5;
				servo2.setAngle(servo2Angle);
				Timer.delay(0.05);
				servo2Angle = 90;
				servo2.setAngle(servo2Angle);
				System.out.println("Camera Down");
			
			}
			
			if (servo1Angle >= 179) {
				servo1Angle = 178;
			}
			if (servo1Angle <= 1) {
				servo1Angle = 2;
			}
			if (servo2Angle >= 179) {
				servo2Angle = 178;
			}
			if (servo2Angle <= 1) {
				servo2Angle = 2;
			}
			servo1.setAngle(servo1Angle);
			servo2.setAngle(servo2Angle);

			
			
			
			
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
			
			
			
			
			if (stick.getRawButton(1)) {
				System.out.println("Toggling Solenoids");
				if (solenoid0.get()) {
					System.out.println("Grabber mode 1");
					solenoid0.set(false);
					solenoid1.set(true);
				} else {
					System.out.println("Grabber mode 2");
					solenoid0.set(true);
					solenoid1.set(false);
				}
				Timer.delay(0.3);
			}
			
			if (stick.getRawButton(2)) {
				if (solenoid2.get()) {
					System.out.println("Extender mode 1");
					solenoid2.set(false);
					solenoid3.set(false);
				} else {
					System.out.println("Extender mode 2");
					solenoid2.set(true);
					solenoid3.set(true);
				}
				Timer.delay(0.3);
			}
			
	//secondJoystick
			if (stick2.getRawButton(1)) {
				System.out.println("Toggling Solenoids");
				if (solenoid0.get()) {
					System.out.println("Grabber mode 1");
					solenoid0.set(false);
					solenoid1.set(true);
				} else {
					System.out.println("Grabber mode 2");
					solenoid0.set(true);
					solenoid1.set(false);
				}
				Timer.delay(0.3);
			}
			
			if (stick2.getRawButton(2)) {
				if (solenoid2.get()) {
					System.out.println("Extender mode 1");
					solenoid2.set(false);
					solenoid3.set(false);
				} else {
					System.out.println("Extender mode 2");
					solenoid2.set(true);
					solenoid3.set(true);
				}
				Timer.delay(0.3);
			}
			
			if (stick.getRawButton(6)) {

				if (shouldGoSlow) {
					shouldGoSlow = false;
					System.out.println("Toggled slow mode to False");
					stick.setRumble(RightRumble, 1);
					Timer.delay(0.2);
					stick.setRumble(RightRumble, 0);
					
				
				} else {
					shouldGoSlow = true;
					System.out.println("Toggled slow mode to True");
					stick.setRumble(RightRumble, 1);
					Timer.delay(0.4);
					stick.setRumble(RightRumble, 0);
					
				}
		    	Timer.delay(0.2);
		    	if (stick.getRawButton(3) && stick.getRawButton(4)) {
		    		turnRight();
		    		turnLeft();
		    		turnLeft();
		    		turnRight();
		    		turnRight();
		    		turnLeft();
		    		turnLeft();
		    		turnRight();
		    		
		    		goForward(0.5, 0.3);
		    		goForward(0.5, -0.3);
		    		
		    		turnRight();
		    		turnLeft();
		    		turnLeft();
		    		turnRight();
		    		turnRight();
		    		turnLeft();
		    		turnLeft();
		    		turnRight();
		    		
		    	}
			}
			
			if (stick.getRawButton(5)) {
				if (shouldGoMedium)  {
					shouldGoMedium = false;
					System.out.println("Toggled Medium Mode To False");
					//stick.setRumble(r, .5);
					stick.setRumble(LeftRumble, 1);
					Timer.delay(0.2);
					stick.setRumble(LeftRumble, 0);
				} else {
					shouldGoMedium = true;
					System.out.println("Toggled Medium Mode To True");
					stick.setRumble(LeftRumble, 1);
					Timer.delay(0.4);
					stick.setRumble(LeftRumble, 0);
				}
				Timer.delay(0.2);
			}
			
			
			if (shouldGoSlow) {
				SpeedModifier = 0.25;
			}
			
			if (shouldGoMedium) {
				SpeedModifier = 0.5;
			}
			
			if (shouldGoSlow && shouldGoMedium) {
				SpeedModifier = 0.125;
			}
			
			if (!shouldGoSlow) {
				if (!shouldGoMedium) {
					SpeedModifier = 1;
				}
			}
			
			if (stick.getRawButton(4)) {
				turnRight();
				
				/*
				encoder1.reset();
				while (encoder1.get() < 50) {
					motor4.set(0.25);
				}
				motor4.set(0);
				*/ 
			}

			
			//liftSpeedMod = stick2.getRawAxis(3);

			if (stick2.getRawAxis(2) > 0.6){
				stick.setRumble(RightRumble, stick2.getRawAxis(3));
				
			}
			if (stick2.getRawAxis(2) < 0.4){
				stick.setRumble(RightRumble, stick2.getRawAxis(2));
				
			}
			
			
			
			

			
			
		if (stick.getRawButton(7)) {
			setLift(stick2.getRawAxis(3) + 1);
		} else if (stick.getRawButton(8)) {
			setLift(-(stick2.getRawAxis(3) + 1));
		} else {
			if (stick2.getRawAxis(1) > 0.3) {
				setLift(stick2.getRawAxis(3) + 1);	
			} else if (stick2.getRawAxis(1) < -0.3) {
				setLift(-(stick2.getRawAxis(3) + 1));
			} else {
				setLift(0);
			}
		}
		
		
		
		}


	@Override
	public void testPeriodic() {
	}
}
