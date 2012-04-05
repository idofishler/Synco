/*
  Button
 
 Turns on and off a light emitting diode(LED) connected to digital  
 pin 13, when pressing a pushbutton attached to pin 2. 
 
 
 The circuit:
 * LED attached from pin 13 to ground 
 * pushbutton attached to pin 2 from +5V
 * 10K resistor attached to pin 2 from ground
 
 * Note: on most Arduinos there is already an LED on the board
 attached to pin 13.
 
 
 created 2005
 by DojoDave <http://www.0j0.org>
 modified 30 Aug 2011
 by Tom Igoe
 
 This example code is in the public domain.
 
 http://www.arduino.cc/en/Tutorial/Button
 */

// constants won't change. They're used here to 
// set pin numbers:
const int buttonPinL = 2; // the number of the pushbutton pin
const int buttonPinR = 3;
const int ledPin =  13;      // the number of the LED pin

// variables will change:
int buttonStateL = 0;         // variable for reading the pushbutton status
int buttonStateR = 0;
int flagR = 0;
int flagL = 0;

void setup() {
  //init the sirial port
   Serial.begin(9600);
  // initialize the LED pin as an output:
  pinMode(ledPin, OUTPUT);      
  // initialize the pushbutton pin as an input:
  pinMode(buttonPinL, INPUT);     
  pinMode(buttonPinR, INPUT);      
}

void loop(){
  // read the state of the pushbutton value:
  buttonStateL = digitalRead(buttonPinL);
  buttonStateR = digitalRead(buttonPinR);

  // check if the pushbutton is pressed.
  // if it is, the buttonState is HIGH:
  if (buttonStateR == HIGH && flagR == 0) {     
    flagR = 1;
    // turn LED on:    
    digitalWrite(ledPin, HIGH);
    Serial.write('0');  
  } 
  else if (buttonStateR == LOW) {
    // turn LED off:
    digitalWrite(ledPin, LOW); 
    flagR = 0;
  }
  if (buttonStateL == HIGH && flagL == 0) {     
    flagL = 1;
    // turn LED on:    
    digitalWrite(ledPin, HIGH);  
    Serial.write('1');  
  } 
  else if (buttonStateL == LOW) {
    // turn LED off:
    digitalWrite(ledPin, LOW); 
    flagL = 0;
  }
}
