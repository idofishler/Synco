#include <AFMotor.h>
#include <EEPROM.h>

#define INIT_STEPS 0
#define MAX_STEPS 460
#define CAL_MOVE 5
#define P1 7
#define P2 8

AF_Stepper motor(200, 2);

int steps = INIT_STEPS;


void setup() {
        Serial.begin(9600);     // opens serial port, sets data rate to 9600 bps
        motor.setSpeed(10);  // 10 rpm
        resetPos();
        //rangeTest();
        delay(1000);
}

void rangeTest() {
  motor.step(MAX_STEPS-INIT_STEPS, FORWARD, SINGLE);
  delay(1000);
  motor.step(MAX_STEPS-INIT_STEPS, BACKWARD, SINGLE); 
  motor.release();
}

void resetPos() {
  int lastPos = readFromEEPROM();
  int gap = lastPos - INIT_STEPS;
  move(-gap);
  writeToEEPROM(INIT_STEPS);
  motor.release();
}

void loop() {
    // send data only when you receive data:
    if (Serial.available() > 0) {
        int val = Serial.read();
        if (val == '0') {
          //Serial.println("NOT_SYNCED");
          if (steps > 0) {
            motor.step(1, BACKWARD, SINGLE);
            //motor.release();
            steps--;
          }
        }
        else if (val == '1') {
          //Serial.println("SYNCED");
          if (steps < MAX_STEPS) {
            motor.step(1, FORWARD, SINGLE);
            //motor.release();
            steps++;
          }
        }
        else if (val == '+') {
          move(CAL_MOVE);
        }
        else if (val == '-') {
          move(-CAL_MOVE);
        }
        else if (val == 't') {
          rangeTest();
        }
        writeToEEPROM(steps);
    }
}

void move(int steps) {
  if (steps > 0) {
    motor.step(steps, FORWARD, SINGLE);
  }
  else if (steps < 0) {
    motor.step(-steps, BACKWARD, SINGLE);
  }
  motor.release();
}

void writeToEEPROM(int in) {
  if (in <= 255) {
    EEPROM.write(P1,in);
    EEPROM.write(P2, 0);
  }
  else if (in > 255 && in <= 512) {
    EEPROM.write(P1,255);
    EEPROM.write(P2,in-255);
  }
}

int readFromEEPROM() {
  int p1 = EEPROM.read(P1);
  int p2 = EEPROM.read(P2);
  return p1+p2;
}
