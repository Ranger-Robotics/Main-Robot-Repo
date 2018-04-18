# Main-Robot-Repo
<b>Guillotine Source Code</b><br>
This is the main code for Ranger Robotics' (6132) Robot. It is written in Java.<br>
It contains basic functionality to allow driving with two sticks in tank style mode, and contains Auto-Code setup for FIRST Power-up, and adjusted for our bot, "Guillotine" which uses a lift.<br><br>

The Teleop mode is a fairly basic Tank style control designed for an Xbox controller. It includes configurable deadzones, can control Pneumatic Cyclinders, a motor driven lift, as well as two servos that allow for two axis control of a Camera.<br><br>

The Auto-code can grab the Switch and Scale settings from the FMS and respond to the starting positions with specific Auto-Modes. The Auto-Modes are built up from a set of functions that interact with the motors and Pneumatics. This way, our Auto-Code is almost entirely English, and could probably be adjusted by anyone, even without programming expierence. Of course, those functions would have to be adjusted if this was to be used on another Bot.<br><br>

Our code does have some basic event logging, but largely it is there for Debug purposes.<br><br>

Finally, the code does support a USB webcam plugged directly into the RoboRIO. It will take the video feed and push it to the Dashboard.<br><br>

Written by Brendan B. and Jordan H. for FRC 2018.<br><br>