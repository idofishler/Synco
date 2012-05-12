/*
This Arduino program reads data from two pulse sensors and blinks heartrate.
*/

int sensorL = A0;
int sensorR = A5;
int pumpL=4;
int pumpR=7;

// Hearbeat detect variables
int newHeartReadingL = 0;
int lastHeartReadingL = 0;
int DeltaL = 0;
int recentReadingsL[8] = {0,0,0,0,0,0,0,0};
int historySizeL = 8;
int recentTotalL = 0;
int readingsIndexL = 0;
boolean highChangeL = false;
int BeatLTime = 0;

int newHeartReadingR = 0;
int lastHeartReadingR = 0;
int DeltaR = 0;
int recentReadingsR[8] = {0,0,0,0,0,0,0,0};
int historySizeR = 8;
int recentTotalR = 0;
int readingsIndexR = 0;
boolean highChangeR = false;
int BeatRTime = 0;

int beatLength = 150;

int totalThreshold = 2;


void setup() {
// initialize the serial communication:
//Serial.begin(9600);
// initialize the digital pin as an output:
  pinMode(pumpL, OUTPUT);
  pinMode(pumpR, OUTPUT);
}

void loop() {
  
    newHeartReadingL = analogRead(sensorL);
    //Serial.println("L" + newHeartReadingL);
    DeltaL = newHeartReadingL - lastHeartReadingL;
    lastHeartReadingL = newHeartReadingL;
    recentTotalL = recentTotalL - recentReadingsL[readingsIndexL] + DeltaL;
    recentReadingsL[readingsIndexL] = DeltaL;
    readingsIndexL = (readingsIndexL + 1) % historySizeL;

    newHeartReadingR = 0;//analogRead(sensorR);
    //Serial.println("R" + newHeartReadingR);
    DeltaR = newHeartReadingR - lastHeartReadingR;
    lastHeartReadingR = newHeartReadingR;
    recentTotalR = recentTotalL - recentReadingsR[readingsIndexR] + DeltaR;
    recentReadingsR[readingsIndexL] = DeltaR;
    readingsIndexR = (readingsIndexR + 1) % historySizeR;
    if (millis() - BeatLTime >= beatLength)
    {
      digitalWrite(pumpL, LOW);
      //Serial.println("L_OFF");
    }
    
     if (millis() - BeatRTime >= beatLength)
    {
      digitalWrite(pumpR, LOW);
      //Serial.println("R_OFF");
    }
    delay(10);
    
    if (recentTotalL >= totalThreshold) {
          digitalWrite (pumpL, HIGH);
          BeatLTime = millis();
          //Serial.println("L_ON");
          //Serial.write('1');
    }
    
    if (recentTotalR >= totalThreshold) {
          digitalWrite (pumpR, HIGH);
          BeatRTime = millis();
          //Serial.println("R_ON");
          //Serial.write('0');
    }
    delay(10);
} 


