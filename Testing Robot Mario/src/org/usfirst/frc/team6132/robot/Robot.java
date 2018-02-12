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

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;


public class Robot extends TimedRobot {
	int NOTE_B0 = 31;
	int NOTE_C1 = 33;
	int NOTE_CS1= 35;
	int NOTE_D1 = 37;
	int NOTE_DS1= 39;
	int NOTE_E1 = 41;
	int NOTE_F1 = 44;
	int NOTE_FS1= 46;
	int NOTE_G1 = 49;
	int NOTE_GS1= 52;
	int NOTE_A1 = 55;
	int NOTE_AS1= 58;
	int NOTE_B1 = 62;
	int NOTE_C2 = 65;
	int NOTE_CS2= 69;
	int NOTE_D2 = 73;
	int NOTE_DS2= 78;
	int NOTE_E2 = 82;
	int NOTE_F2 = 87;
	int NOTE_FS2= 93;
	int NOTE_G2 = 98;
	int NOTE_GS2= 104;
	int NOTE_A2 = 110;
	int NOTE_AS2= 117;
	int NOTE_B2 = 123;
	int NOTE_C3 = 131;
	int NOTE_CS3= 139;
	int NOTE_D3 = 147;
	int NOTE_DS3= 156;
	int NOTE_E3 = 165;
	int NOTE_F3 = 175;
	int NOTE_FS3= 185;
	int NOTE_G3 = 196;
	int NOTE_GS3= 208;
	int NOTE_A3 = 220;
	int NOTE_AS3= 233;
	int NOTE_B3 = 247;
	int NOTE_C4 = 262;
	int NOTE_CS4= 277;
	int NOTE_D4 = 294;
	int NOTE_DS4= 311;
	int NOTE_E4 = 330;
	int NOTE_F4 = 349;
	int NOTE_FS4= 370;
	int NOTE_G4 = 392;
	int NOTE_GS4= 415;
	int NOTE_A4 = 440;
	int NOTE_AS4= 466;
	int NOTE_B4 = 494;
	int NOTE_C5 = 523;
	int NOTE_CS5= 554;
	int NOTE_D5 = 587;
	int NOTE_DS5= 622;
	int NOTE_E5 = 659;
	int NOTE_F5 = 698;
	int NOTE_FS5= 740;
	int NOTE_G5 = 784;
	int NOTE_GS5= 831;
	int NOTE_A5 = 880;
	int NOTE_AS5= 932;
	int NOTE_B5 = 988;
	int NOTE_C6 = 1047;
	int NOTE_CS6= 1109;
	int NOTE_D6 = 1175;
	int NOTE_DS6= 1245;
	int NOTE_E6 = 1319;
	int NOTE_F6 = 1397;
	int NOTE_FS6= 1480;
	int NOTE_G6 = 1568;
	int NOTE_GS6= 1661;
	int NOTE_A6 = 1760;
	int NOTE_AS6= 1865;
	int NOTE_B6 = 1976;
	int NOTE_C7 = 2093;
	int NOTE_CS7= 2217;
	int NOTE_D7 = 2349;
	int NOTE_DS7= 2489;
	int NOTE_E7 = 2637;
	int NOTE_F7 = 2794;
	int NOTE_FS7= 2960;
	int NOTE_G7 = 3136;
	int NOTE_GS7= 3322;
	int NOTE_A7 = 3520;
	int NOTE_AS7= 3729;
	int NOTE_B7 = 3951;
	int NOTE_C8 = 4186;
	int NOTE_CS8= 4435;
	int NOTE_D8 = 4699;
	int NOTE_DS8= 4978;


	//Mario main theme melody
	int melody[] = {
	  NOTE_E7, NOTE_E7, 0, NOTE_E7,
	  0, NOTE_C7, NOTE_E7, 0,
	  NOTE_G7, 0, 0,  0,
	  NOTE_G6, 0, 0, 0,

	  NOTE_C7, 0, 0, NOTE_G6,
	  0, 0, NOTE_E6, 0,
	  0, NOTE_A6, 0, NOTE_B6,
	  0, NOTE_AS6, NOTE_A6, 0,

	  NOTE_G6, NOTE_E7, NOTE_G7,
	  NOTE_A7, 0, NOTE_F7, NOTE_G7,
	  0, NOTE_E7, 0, NOTE_C7,
	  NOTE_D7, NOTE_B6, 0, 0,

	  NOTE_C7, 0, 0, NOTE_G6,
	  0, 0, NOTE_E6, 0,
	  0, NOTE_A6, 0, NOTE_B6,
	  0, NOTE_AS6, NOTE_A6, 0,

	  NOTE_G6, NOTE_E7, NOTE_G7,
	  NOTE_A7, 0, NOTE_F7, NOTE_G7,
	  0, NOTE_E7, 0, NOTE_C7,
	  NOTE_D7, NOTE_B6, 0, 0
	};
	//Mario main them tempo
	int tempo[] = {
	  12, 12, 12, 12,
	  12, 12, 12, 12,
	  12, 12, 12, 12,
	  12, 12, 12, 12,

	  12, 12, 12, 12,
	  12, 12, 12, 12,
	  12, 12, 12, 12,
	  12, 12, 12, 12,

	  9, 9, 9,
	  12, 12, 12, 12,
	  12, 12, 12, 12,
	  12, 12, 12, 12,

	  12, 12, 12, 12,
	  12, 12, 12, 12,
	  12, 12, 12, 12,
	  12, 12, 12, 12,

	  9, 9, 9,
	  12, 12, 12, 12,
	  12, 12, 12, 12,
	  12, 12, 12, 12,
	};


	private SpeedController motor0 = new Talon(0); // initialize the motor as a
	private SpeedController motor1 = new Talon(1);
	private SpeedController motor2 = new Talon(2);
	private SpeedController motor3 = new Talon(3);// Talon on channel 0
	private SpeedController motor4 = new Talon(4);
	private Servo servo1 = new Servo(9);
	private Joystick stick = new Joystick(0); // initialize the joystick on port
	private Joystick stick2 = new Joystick(3);
	Thread m_visionThread;// 0
	double DZ = .1;
	double SpeedModifier = 1;
	double liftSpeedMod;
	double servo1Angle = 90;
	String startingPosition = "right";


	@Override
	public void robotInit() {
		
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
	
	void setBoth(double value) {
		setLeft(value);
		setRight(value);
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
	
	void buzz(long frequency, long length) {
		  long delayValue = 1000000 / frequency * 16; // calculate the delay value between transitions
		  //// 1 second's worth of microseconds, divided by the frequency, then split in half since
		  //// there are two phases to each cycle
		  long numCycles = frequency * length / 32000; // calculate the number of cycles for proper timing
		  //// multiply frequency, which is really cycles per second, by the number of seconds to
		  //// get the total number of cycles to produce
		  for (long i = 0; i < numCycles; i++) { // for the calculated length of time...
		    setBoth(1); // write the buzzer pin high to push out the diaphram
		    Timer.delay(delayValue/1000000); // wait for the calculated delay value
		    setBoth(0); // write the buzzer pin low to pull back the diaphram
		    Timer.delay(delayValue/1000000); // wait again or the calculated delay value
		  }
		  setBoth(0);

		}


	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {

	}


	@Override
	public void autonomousInit() {
		int size = 78;
	    for (int thisNote = 0; thisNote < size; thisNote++) {

	      // to calculate the note duration, take one second
	      // divided by the note type.
	      //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
	      int noteDuration = 1000 / tempo[thisNote];

	      buzz(melody[thisNote], noteDuration);

	      // to distinguish the notes, set a minimum time between them.
	      // the note's duration + 30% seems to work well:
	      double pauseBetweenNotes = noteDuration * 1.30;
	      Timer.delay(pauseBetweenNotes);

	      // stop the tone playing:
	      buzz(0, noteDuration);

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


	}


	@Override
	public void testPeriodic() {
	}
}
