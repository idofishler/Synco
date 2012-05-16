#include <AFMotor.h>

AF_Stepper motor(48, 2);

int initDistance = 0;   // for incoming serial data
int newDistance = 0;
boolean first = true;

void setup() {
        Serial.begin(9600);     // opens serial port, sets data rate to 9600 bps
        motor.setSpeed(10);  // 10 rpm
        motor.step(100, FORWARD, DOUBLE); 
        motor.release();
        delay(1000);
}

void loop() {

        // send data only when you receive data:
        if (Serial.available() > 0) {
          // only do first time
          if (first == true) {
            initDistance = Serial.read();
            first = false;
          }
          // every other time
          else {
            newDistance =  Serial.read();     // read the incoming byte:
            // say what you got:
            Serial.print("I received: ");
            Serial.println(newDistance, DEC);
            int gap = newDistance - initDistance;
            Serial.print("Gap is: ");
            Serial.println(gap, DEC);
            
            if (newDistance > initDistance) {
              motor.step(gap, FORWARD, DOUBLE);
            }
            else if (newDistance < initDistance) {
            motor.step(-gap, BACKWARD, DOUBLE);
            }
            initDistance = newDistance; 
        }
      }
}
