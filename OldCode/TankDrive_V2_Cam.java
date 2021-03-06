package org.usfirst.frc.team6132.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends SampleRobot {
	RobotDrive myRobot = new RobotDrive(0, 1);
	Joystick controller = new Joystick(0);
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	SendableChooser<String> chooser = new SendableChooser<>();
	
	 	private SpeedController motor1;	// the motor to directly control with a joycontroller
	    private SpeedController motor2;
	    private SpeedController motor3;
	    private SpeedController motor4;
	    private SpeedController motor5; // this is just to see if i can program the second joycontroller delete motors 5 6 7 if necessary //rainbrop//
	    private SpeedController motor6;
	    private SpeedController motor7;
	    private SpeedController motor8;
	    private SpeedController motor9;
	    public double DZ;//deadzone on joystick
	    public double SM;//speed multipler

	 
	public Robot() {
		myRobot.setExpiration(0.1);
//starts the controller board
	    motor1 = new Talon(0);
        motor2 = new Talon(1);
        motor3 = new Talon(2);
        motor4 = new Talon(3); 
        motor5 = new Talon(4); 
        motor6 = new Talon(5);
        motor7 = new Talon(6);
        motor8 = new Talon(7);
        motor9 = new Talon(8);
        DZ = (.25); 
        SM = (1.333333333333333333333333333333333333333333333333333333333333333333333333333333333);
	}

	@Override// camera later
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto modes", chooser);
    CameraServer.getInstance().startAutomaticCapture();
    
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * if-else structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomous() {
		String autoSelected = chooser.getSelected();
		// String autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		//auto make robot drive forward
		motor1.set(.39);
    	motor2.set(.39);
    	motor3.set(-.4);
    	motor4.set(-.4);
    	Timer.delay(2.25);
    	motor1.set(0);
    	motor2.set(0);
    	motor3.set(0);
    	motor4.set(0);
    	Timer.delay(1.1);
    	
		switch (autoSelected) {
		case customAuto:
			myRobot.setSafetyEnabled(false);
			myRobot.drive(-0.5, 1.0); // spin at half speed
			Timer.delay(2.0); // for 2 seconds
			myRobot.drive(0.0, 0.0); // stop robot
			break;
		case defaultAuto:
		default:
			myRobot.setSafetyEnabled(false);
			myRobot.drive(-0.5, 0.0); // drive forwards half speed
			Timer.delay(2.0); // for 2 seconds
			myRobot.drive(0.0, 0.0); // stop robot
			break;
		}
	}

	/**
	 * Runs the motors with arcade steering.
	 */
	@Override
	public void operatorControl() {
		myRobot.setSafetyEnabled(true);
		while (isOperatorControl() && isEnabled()) {
			myRobot.arcadeDrive(controller); // drive with arcade style (use right
										// controller)
			Timer.delay(0.005); // wait for a motor update time
			if (controller.getY() > DZ)  {
				motor1.set(controller.getY()-DZ);
				motor2.set(controller.getY()-DZ);
				if (controller.getRawAxis(5) > DZ)  {
					motor3.set(-(controller.getRawAxis(5)-DZ));
				    motor4.set(-(controller.getRawAxis(5)-DZ));
				}else
				if (controller.getRawAxis(5) < -DZ){
					motor3.set(-(controller.getRawAxis(5)+DZ));
				    motor4.set(-(controller.getRawAxis(5)+DZ));
				}
			}	else
			if (controller.getY() < -DZ)  {
				motor1.set(controller.getY()+DZ);
				motor2.set(controller.getY()+DZ);
				if (controller.getRawAxis(5) > DZ)  {
					motor3.set(-(controller.getRawAxis(5)-DZ));
					motor4.set(-(controller.getRawAxis(5)-DZ));
				}else
					if (controller.getRawAxis(5) < -DZ){
						motor3.set(-(controller.getRawAxis(5)+DZ));
					    motor4.set(-(controller.getRawAxis(5)+DZ));
					}else
			if (controller.getRawAxis(5) < -DZ)  {
				motor3.set(-(controller.getRawAxis(5)+DZ));
				motor4.set(-(controller.getRawAxis(5)+DZ));
				if (controller.getY() > DZ)  {
					motor1.set((controller.getY()-DZ));
					motor2.set((controller.getY()-DZ));
				}else
					if (controller.getY() < -DZ){
						motor1.set((controller.getY()+DZ));
						motor2.set((controller.getY()+DZ));
					}else
						if (controller.getRawAxis(5) > DZ)  {
							motor3.set(-(controller.getRawAxis(5)+DZ));
							motor4.set(-(controller.getRawAxis(5)+DZ));
							if (controller.getY() > DZ)  {
								motor1.set(-(controller.getRawAxis(5)-DZ));
								motor2.set(-(controller.getRawAxis(5)-DZ));
							}else
								if (controller.getY() < -DZ){
									motor1.set(-(controller.getY()+DZ));
									motor2.set(-(controller.getY()+DZ));
								}else
			if (controller.getRawButtonPressed(1)){
				motor1.set(.1);
				motor2.set(.1);
			}
			
			
			else {
					motor1.set(0);
					motor2.set(0);
					motor3.set(0);
					motor4.set(0);
					motor5.set(0);
					motor6.set(0);
					motor7.set(0);
					motor8.set(0);
					motor9.set(0);
			}
		}
	}
}
		}
	}
}
