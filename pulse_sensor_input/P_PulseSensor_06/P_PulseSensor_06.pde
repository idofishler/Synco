/*
This program functions as a data visualizer for the Pulse Sensor
Serial input is designed to mate with arduino sketch "A_PulseSensor_xx"
This code made by Joel Murphy and Yury Gitman in Brooklyn, Summer 2011.

interactive features in this version:
press 'S' or 's' to take a picture of the data window. (.tif image)
Pulse data window will plot pulse 1:1 (ADC value : pixesls) and auto adjust to fit
waveform in screen
CC Attribution Share-Alike    http://creativecommons.org/licenses/by-sa/3.0/us/
*/

import processing.serial.*;      // this is how we talk to arduino
PFont font, rateFont, smallFont;            // create font instances
Serial port;                     // create and name the serial port

int heart = 0;            // used to time pulsing of heart graphic with your heart beat
int pulseRate = 0;        // used to hold pulse rate value sent from arduino (beats per minute)
int Sensor = 0;           // used to hold raw sensor data from arduino
int HRV;                  // time between this current beat and the last beat in mS (used for Heart Rate Variability) 
int Ypos;                 // used to print the new Pulse Sensor data point
int[] pulseY;                 // used to hold pulse waveform Y positions
int[] rateY;                 // used to hold bpm waveform Y positions
// these variables will hold pulse window specs
int PulseWindowMin;
int PulseWindowMax;        
int PulseWindowW;
int PulseWindowH;
int PulseWindowY;
int PulseWindowX;
int PulseDisplayBaseline = 712;  // these variables are used to adjust the pulse window display 
int PulseOffset = 712;           // the max and min will auto adjust if the waveform drifts beyond the screen

color eggshell = color(255,253,248); // offwhite color for data display windows
color R = color(250,0,0);            // red color for datapoints and heart graphic
int grey = 80;                       // grey color for numeric data windows

boolean beat = false;            // used to advance heart rate graph
boolean newRate = false;         // used to update heart rate display


void setup() {
size(800,600); // Stage size
frameRate(100); // refresh rate
font = loadFont("Arial-BoldMT-36.vlw");  // font for small text
rateFont = loadFont("Arial-BoldMT-80.vlw");  // font for larger text
smallFont = loadFont("Arial-BoldMT-14.vlw"); // font for smaller text
textFont(font); // set up to print small font
textAlign(RIGHT);  // referemce points
rectMode(CENTER);

//  define the size of the pulse window
PulseWindowW = 710;    // width of pulse window
PulseWindowH = 400;    // height of pulse window
PulseWindowX = width/2+30;  // center X coordinate of pulse window
PulseWindowY = height-225;  // center Y coordinate of pulse window 
PulseWindowMin = PulseWindowY - PulseWindowH/2;  // top Y position of pulse window
PulseWindowMax = PulseWindowY + PulseWindowH/2;  // bottom Y position of pulse window

pulseY = new int[PulseWindowW+1];    // array to hold Y coordinate of pulse datapoints
rateY = new int[320];                // array to hold y coordinate of heart rate datapoints



// find and establish contact with the serial port
println(Serial.list());       // print a list of available serial ports
port = new Serial(this, Serial.list()[0], 115200); // choose the right one in square brackets
port.bufferUntil('\n');          // arduino will end each ascii string with a '\n' at the end (carriage return)
port.clear();                    // flush the serial buffer



// set arbitrary initial data position at top of pulse window
Ypos = PulseWindowMin;    
for (int i = 0; i < pulseY.length; i++){  
  pulseY[i] = PulseWindowMin;  
}

// set the BPM visualizer line to 0
 for (int i=0; i<rateY.length; i++){
    rateY[i] = 162;      // 162 is the pixel height for 0 heart rate
   }
}


void draw(){

printScreen();      // DRAW THE MAJOR SCREEN COMPONENTS AND TEXT
  
// THESE ARE THE PULSE SENSOR WAVEFORM DRAWING ROUTINES
// fisrt, move the Y coordintae of previous pulse data points over one pixel to the left
 for (int i = 0; i < pulseY.length-1; i++){  
   pulseY[i] = pulseY[i+1];     
 }
// new data enters on the right of the screen. sensor value is placed in the last array position
pulseY[pulseY.length-1] = Ypos;  // Ypos is updated in the serialEvent tab
// This for loop renders the Pulse Sensor waveform
 for (int x = 1; x < pulseY.length-1; x++){    // variable 'x' will take the place of pixel x position
   stroke(R);                                  // get ready to make a red line
// Here are a few ways to draw the datapoints.    
//   line(x+61, pulseY[x]+1, x+61, pulseY[x]-1);          // display previous datapoint as vertical line
//   point(x+61,pulseY[x]);                               //display previous datapoint as point
//   ellipse(x+61,pulseY[x],1,1);                         // display previous datapoint as a small dot
   line(x+76, pulseY[x], x+75, pulseY[x-1]);              //display previous datapoint as a connected line
 }
 

//  THESE ARE THE HEART RATE DRAWING ROUTINES 
 if (beat == true){   // only move the heart rate line over once every time the heart beats (beat flag set in serialEvent tab)
   beat = false;      // reset beat flag
   for (int i=0; i<rateY.length-1; i++){
    rateY[i] = rateY[i+1];      // shif the bpm Y coordinates over one pixel to the left
   }
 }
// update the BPM display Y coordinate when arduino sends a new calculation
 if (newRate == true){                          // when the new rate is sent from arduino 
   float dummy = map(pulseRate,0,200,135,10);   // map it to the heart rate window Y
   rateY[rateY.length-1] = int(dummy);          // set the rightmost pixel to the new data point value
 }
// print out the graph of the heart rate
 stroke(250,0,0);                                     // color of heart rate graph
 for (int x=0; x < rateY.length-1; x++){        // variable 'x' will take the place of pixel x position
   line(x+76, rateY[x]+2, x+76, rateY[x]-2);    // display previous heart rate datapoint as vertical line
 }
}  //end of draw loop




void printScreen(){          // DRAW MAJOR SCREEN ELEMENTS AND TEXT
   background(0);                           // black background
   noStroke();  
   fill(grey);                              // grey
   rect(40,PulseWindowY,70,PulseWindowH);   // draw box around pulse window numbers  
   rect(40,90,70,150);                      // draw box around rate window numbers  
   fill(eggshell);                          // eggshell
   rect(PulseWindowX,PulseWindowY,PulseWindowW,PulseWindowH);        // draw pulse terminal
   rect(235,90,320,150);                                             // draw bpm terminal
   text("0",65,163);                        // print ʻ0ʻ at bottom of bpm terminal (min heart rate for zombies)
   text("200",65,35);                       // print ʻ200ʻ at top of bpm terminal (max heart rate for young runners) 
   text(HRV,65,PulseWindowY);              // print the waveform amplitude
   text("BPM",width-215,100);               // clarification (BPM = Beats Per Minute = heart rate)
   text("Pulse Sensor Visualizer 0.6",width-35,160);      // name of program, version   
   textFont(smallFont);
   text("mS Time",72,PulseWindowY + 20);
   text("Between",72,PulseWindowY + 40);  // print the high number of visible datapoints (DEPENDS ON AUTO OFFSET)
   text("Beats",72,PulseWindowY + 60);      // print the low number of visible datapoints  (DITTO)   
   textFont(rateFont);                      // set up to print large text
   text(pulseRate,width-275,100);           // print out the pulse rate 
   textFont(font);                          // set up to print small text
   
   
// DRAW THE HEART AND MAYBE MAKE IT BEAT
  fill(250,0,0);
  stroke(250,0,0);
  heart--;                    // the heart variable is used to time how long the heart graphic swells when your heart beats
  if (heart < 0){heart = 0;}  // don't let the heart variable go into negative numbers
  if (heart > 0){             // if a beat happened recently, 
    strokeWeight(8);          // make the heart big
  }
  smooth();   // draw the heart with two bezier curves
  bezier(width-125,30, width-45,-40, width-25,120, width-125,130);
  bezier(width-125,30, width-215,-40, width-225,120, width-125,130);
  strokeWeight(1);        // reset the strokeWeight
 
}  // END OF printScreen FUNCTION

//  original heart design and positioning
//  bezier(width-150,50, width-70,-20, width-50,140, width-150,150);
//  bezier(width-150,50, width-230,-20, width-250,140, width-150,150);




