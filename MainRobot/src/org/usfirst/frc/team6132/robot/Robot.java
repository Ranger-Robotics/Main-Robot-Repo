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
	private Servo servo1 = new Servo(9);

	private Servo servo2 = new Servo(8);
	private static final int kUltrasonicPort = 0;
	//private static final double kValueToInches = 0.125;

	private static final int kGyroPort = 0;
	Encoder encoder1 = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
	//private AnalogGyro m_gyro = new AnalogGyro(kGyroPort);
	//private AnalogInput m_ultrasonic = new AnalogInput(kUltrasonicPort);

	
	
	private Joystick stick = new Joystick(0); // initialize the joystick on port
	//private Joystick stick2 = new Joystick(3);
	Thread m_visionThread;// 0
	double DZ = .1;
	double SpeedModifier = 1;
	double liftSpeedMod;
	double servo1Angle = 90;

	double servo2Angle = 90;
	String startingPosition = "right";
	
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
		setLeft(-0.3);
		setRight(-0.3);
		Timer.delay(0.9);
		setLeft(0);
		setRight(0);
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
		solenoid0.set(false);
		solenoid1.set(true);
		System.out.println("Closing Grabber");
	}
	
	void openGrabber() {
		solenoid0.set(true);
		solenoid1.set(false);
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
		
		
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		char ourSwitchPosition = gameData.charAt(0);
		char ScalePosition = gameData.charAt(1);
		
		if (startingPosition == "left") {
			if(ourSwitchPosition == 'L') { //The bot tries to go for our Switch.
				closeGrabber();
				setLift(0.25);
				Timer.delay(1);
				setLift(0);
				extendGrabber();
				goForward(4.44, 0.3);
				turnRight();
				goForward(1, 0.3);
				openGrabber();
			}
			
			if (ourSwitchPosition == 'R') {
				
				if (ScalePosition == 'L') { //If our switch is on the wrong side, try to go for the switch
					//Adjust timing to get to scale, this is set for switch
					
					closeGrabber();
					setLift(0.25);
					Timer.delay(1);
					setLift(0);
					extendGrabber();
					goForward(8.562857, 0.3);
					turnRight();
					goForward(1, 0.3);
					openGrabber();
				
				} else if (ScalePosition == 'R') { //If both the switch and the scale are wrong, go straight
					closeGrabber();
					setLift(0.25);
					Timer.delay(1);
					setLift(0);
					extendGrabber();
					goForward(4.44, 0.3);
				}
				
				
			}
		}
		
		if (startingPosition == "center") {
			closeGrabber();
			setLift(0.25);
			Timer.delay(1);
			setLift(0);
			extendGrabber();
			goForward(4.44, 0.3); // Just go straight accross the autoline
		}
		
		
		if (startingPosition == "right") {
			if (ourSwitchPosition == 'R') {
				closeGrabber();
				setLift(0.25);
				Timer.delay(1);
				setLift(0);
				extendGrabber();
				goForward(4.44, 0.3);
				turnLeft();
				goForward(1, 0.3);
				openGrabber();
			}
			
			if (ourSwitchPosition == 'L') {
				if (ScalePosition == 'R') {
					closeGrabber();
					setLift(0.25);
					Timer.delay(1);
					setLift(0);
					extendGrabber();
					goForward(8.562857, 0.3);
					turnLeft();
					goForward(1, 0.3);
					openGrabber();
				}
				
				if (ScalePosition == 'L') {
					closeGrabber();
					setLift(0.25);
					Timer.delay(1);
					setLift(0);
					extendGrabber();
					goForward(4.44, 0.3);
				}
			}
			
			
			
		}
		
		
	}


	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void teleopInit() {
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
			
			if (stick.getRawButton(6)) {

				if (shouldGoSlow) {
					shouldGoSlow = false;
					System.out.println("Toggled slow mode to False");
				} else {
					shouldGoSlow = true;
					System.out.println("Toggled slow mode to True");
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
				} else {
					shouldGoMedium = true;
					System.out.println("Toggled Medium Mode To True");
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
				encoder1.reset();
				while (encoder1.get() < 50) {
					motor4.set(0.25);
				}
				motor4.set(0);
			}

			
			//liftSpeedMod = stick2.getRawAxis(3);

			/*
			if (stick2.getRawAxis(1) < -0.2) {
				setLift(stick2.getRawAxis(1));
			}

			if (stick2.getRawAxis(1) > 0.2) {
				setLift(stick2.getRawAxis(1));
			}

			*/
			
			

			
			
		if (stick.getRawButton(7)) {
			motor4.set(0.5);
		} else if (stick.getRawButton(8)) {
			motor4.set(-0.5);
		} else {
			motor4.set(0);
		}
		
		
		
		}


	@Override
	public void testPeriodic() {
	}
}
