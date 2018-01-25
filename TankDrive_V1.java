package org.usfirst.frc.team6132.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//I don't *think* we need to have any more imports here for the
//encoders and solenoids, but I could be wrong.


public class Robot extends SampleRobot {
	RobotDrive myRobot = new RobotDrive(0, 1);
	Joystick stick = new Joystick(0);
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	SendableChooser<String> chooser = new SendableChooser<>();

	 	private SpeedController motor1;	// the motor to directly control with a joystick
	    private SpeedController motor2;
	    private SpeedController motor3;
	    private SpeedController motor4;
	    private SpeedController motor5; // this is just to see if i can program the second joystick delete motors 5 6 7 if necessary //rainbrop//
	    private SpeedController motor6;
	    private SpeedController motor7;
	    private SpeedController motor8;
	    private SpeedController motor9;
	    public double DZ;//deadzone on joystick
	    public double SM;//speed multipler


	public Robot() {
		myRobot.setExpiration(0.1);
//starts the controller board
	    motor1 = new Talon(0); //Motor 1 in the code is motor 0 on the pins
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
        //The following creates an encoder object and sets it's properties.
        //Encoder enc;
        //enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
        //enc.setMaxPeriod(.1); //Not sure if we need this line
        //enc.setMinRate(10);
        //enc.setDistancePerPulse(5); //We need to figure out the value based on the specific Encoder
        //enc.setReverseDirection(true);
        //enc.setSamplesToAverage(7);
        
        
        //I am pretty sure that solenoid objects are made like below:
        //solenoid1 = new Solenoid(int)
        //There should be a single argument that contains the channel number.
        //The code for open/closing the solenoid is in the operator control loop
	
	}
	
	public void setAllSolenoids(boolean state) {
		//Does nothing at the moment, could be used to set the states of all the solenoids.
		//solenoid1.set(state);
	}
	
	public void setAllMotors(double value) {
		motor1.set(value);
		motor2.set(value);
		motor3.set(-value);
		motor4.set(-value);
		motor5.set(value);
		motor6.set(value);
		motor7.set(value);
		motor8.set(value);
		motor9.set(value);
	}

	@Override// camera later
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto modes", chooser);

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
		//We can get the current encoder distance with the following:
		//double distance = sampleEncoder.getDistance();
		//The distance is dependent on the distnace per pulse var set above
		//The encoder can be reset with enc.reset();
		
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
			myRobot.arcadeDrive(stick); // drive with arcade style (use right
										// stick)
			Timer.delay(0.005); // wait for a motor update time
			
			//The following jumble of if statements just grabs the Y value off both joysticks
			//and then sets the motors to the right speed to match. I am not sure this works
			//I am gonna try and go through it a bit later
			if (stick.getY() > DZ)  {
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
				
				
				
			}	else if (stick.getY() < -DZ)  {
				motor1.set(stick.getY()+DZ);
				motor2.set(stick.getY()+DZ);
				if (stick.getRawAxis(5) > DZ)  {
					motor3.set(-(stick.getRawAxis(5)-DZ));
					motor4.set(-(stick.getRawAxis(5)-DZ));
				}else if (stick.getRawAxis(5) < -DZ){
					motor3.set(-(stick.getRawAxis(5)+DZ));
					motor4.set(-(stick.getRawAxis(5)+DZ));
					}
				
				
				
				
			}else if (stick.getRawAxis(5) < -DZ)  {
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
						
						
			}else if (stick.getRawAxis(5) > DZ)  {
				motor3.set(-(stick.getRawAxis(5)+DZ));
				motor4.set(-(stick.getRawAxis(5)+DZ));
				if (stick.getY() > DZ)  {
					motor1.set(-(stick.getRawAxis(5)-DZ));
					motor2.set(-(stick.getRawAxis(5)-DZ));
				}else if (stick.getY() < -DZ){
					motor1.set(-(stick.getY()+DZ));
					motor2.set(-(stick.getY()+DZ));
				}
				
				
			}else if (stick.getRawButton(1)){
				setAllMotors(0.2);
			} else if (stick.getRawButton(2)){
				//solenoid1.set(boolean); //Open or close the solenoid with true or false
				//int currentSolenoidState = solenoid1.get();
			} else if (stick.getRawButton(3)){
				setAllMotors(0); //Just in case we need an emergency stop
			}



			else { //If none of the above is true, set all motors to 0.
					setAllMotors(0);
			}
		}
	}
}
