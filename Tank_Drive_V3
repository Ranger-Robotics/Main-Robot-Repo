package org.usfirst.frc.team6132.robot;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * This sample program shows how to control a motor using a joystick. In the
 * operator control part of the program, the joystick is read and the value is
 * written to the motor.
 *
 * Joystick analog values range from -1 to 1 and speed controller inputs also
 * range from -1 to 1 making it easy to work together. The program also delays a
 * short time in the loop to allow other threads to run. This is generally a
 * good idea, especially since the joystick values are only transmitted from the
 * Driver Station once every 20ms.
 */
public class Robot extends SampleRobot {

	private SpeedController motor1 = new Talon(0); // initialize the motor as a
	private SpeedController motor2 = new Talon(1);
	private SpeedController motor3 = new Talon(2);
	private SpeedController motor4 = new Talon(3);// Talon on channel 0
	private Joystick stick = new Joystick(0); // initialize the joystick on port
	Thread m_visionThread;// 0
	double DZ = .1;
	
	
	
	private final double kUpdatePeriod = 0.005; // update every 0.005 seconds/5
												// milliseconds (200Hz)

	public Robot() {
	}
	
	public void robotInit() {
		m_visionThread = new Thread(() -> {
			// Get the Axis camera from CameraServer
			AxisCamera camera
					= CameraServer.getInstance().addAxisCamera("axis-camera.local");
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
	public void autonomous() {
		motor1.set(1);
		motor2.set(1);
		motor3.set(-1);
		motor4.set(-1);
		Timer.delay(2.25);
		motor1.set(0);
		motor2.set(0);
		motor3.set(0);
		motor4.set(0);
	}
	@Override
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {
			// Set the motor's output.
			// This takes a number from -1 (100% speed in reverse) to +1 (100%
			// speed going forward)
			if (stick.getY() > DZ)  {//forward 
				motor1.set(stick.getY()-DZ);
				motor2.set(stick.getY()-DZ);
				if (stick.getRawAxis(5) > DZ)  {
					motor3.set(-(stick.getRawAxis(5)-DZ));
				    motor4.set(-(stick.getRawAxis(5)-DZ));
				}else
				if (stick.getRawAxis(5) < -DZ){
					motor3.set(-(stick.getRawAxis(5)+DZ));
				    motor4.set(-(stick.getRawAxis(5)+DZ));
				}
			}	else if (stick.getY() < -DZ)  {//backward
				motor1.set(stick.getY()+DZ);
				motor2.set(stick.getY()+DZ);
				if (stick.getRawAxis(5) > DZ)  {
					motor3.set(-(stick.getRawAxis(5)-DZ));
					motor4.set(-(stick.getRawAxis(5)-DZ));
				}else if (stick.getRawAxis(5) < -DZ){
					motor3.set(-(stick.getRawAxis(5)+DZ));
					motor4.set(-(stick.getRawAxis(5)+DZ));
					}
			}else if (stick.getRawAxis(5) < -DZ)  {//backward
				motor3.set(-(stick.getRawAxis(5)+DZ));
				motor4.set(-(stick.getRawAxis(5)+DZ));
				if (stick.getY() > DZ)  {
					motor1.set((stick.getY()-DZ));
					motor2.set((stick.getY()-DZ));
				}else
					if (stick.getY() < -DZ){
						motor1.set((stick.getY()+DZ));
						motor2.set((stick.getY()+DZ));
					}
						
						
			}else if (stick.getRawAxis(5) > DZ)  {//forward
				motor3.set(-(stick.getRawAxis(5)+DZ));
				motor4.set(-(stick.getRawAxis(5)+DZ));
				if (stick.getY() > DZ)  {
					motor1.set(-(stick.getRawAxis(5)-DZ));
					motor2.set(-(stick.getRawAxis(5)-DZ));
				}else if (stick.getY() < -DZ){
					motor1.set(-(stick.getY()+DZ));
					motor2.set(-(stick.getY()+DZ));
				}
			else {
					motor1.set(0);
					motor2.set(0);
					motor3.set(0);
					motor4.set(0);
					
			}	

			Timer.delay(kUpdatePeriod); // wait 5ms to the next update
		}
	}
}
}
