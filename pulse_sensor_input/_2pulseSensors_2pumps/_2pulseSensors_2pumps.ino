/*
>> Pulse Sensor Digital Filter <<
This code is the library prototype for Pulse Sensor by Yury Gitman and Joel Murphy
    www.pulsesensor.com 
    >>> Pulse Sensor purple wire goes to Analog Pin 0 <<<
Pulse Sensor sample aquisition and processing happens in the background via Timer 1 interrupt. 1mS sample rate.
The following variables are automatically updated:
Pulse :     boolean that is true when a heartbeat is sensed then false in time with pin13 LED going out.
Signal :    int that holds the analog signal data straight from the sensor. updated every 1mS.
HRV  :      int that holds the time between the last two beats. 1mS resolution.
B  :        boolean that is made true whenever HRV is updated. User must reset.
BPM  :      int that holds the heart rate value. derived from averaging HRV every 10 pulses.
QS  :       boolean that is made true whenever BPM is updated. User must reset.
Scale  :    int that works abit like gain. use to change the amplitude of the digital filter output. useful range 12<>20 : high<>low default = 12
FSignal  :  int that holds the output of the digital filter/amplifier. updated every 1mS.

See the README for detailed information and known issues.
Code Version 0.6 by Joel Murphy  December 2011  Happy New Year! 
*/



long Hxv[2][4]; // these arrays are used in the digital filter
long Hyv[2][4]; // H for highpass, L for lowpass
long Lxv[2][4];
long Lyv[2][4];

unsigned long readingsR; // used to help normalize the signal
unsigned long readingsL; // used to help normalize the signal
unsigned long peakTimeR; // used to time the start of the heart pulse
unsigned long lastPeakTimeR = 0;// used to find the time between beats
unsigned long peakTimeL; // used to time the start of the heart pulse
unsigned long lastPeakTimeL = 0;// used to find the time between beats
volatile int PeakR;     // used to locate the highest point in positive phase of heart beat waveform
volatile int PeakL;     // used to locate the highest point in positive phase of heart beat waveform
int rateR;              // used to help determine pulse rate
int rateL;              // used to help determine pulse rate
volatile int BPMR;      // used to hold the pulse rate
volatile int BPML;      // used to hold the pulse rate
int offsetR = 0;        // used to normalize the raw data
int offsetL = 0;        // used to normalize the raw data
int sampleCounter;     // used to determine pulse timing
int beatCounterL = 1;   // used to keep track of pulses
int beatCounterR = 1;   // used to keep track of pulses
volatile int SignalR;   // holds the incoming raw data - RIGHT player
volatile int SignalL;   // holds the incoming raw data LEFT player
int NSignalR;           // holds the normalized signal 
int NSignalL;           // holds the normalized signal 
volatile int FSignalR;  // holds result of the bandpass filter
volatile int FSignalL;  // holds result of the bandpass filter
volatile int HRVR;      // holds the time between beats
volatile int HRVL;      // holds the time between beats
volatile int Scale = 13;  // used to scale the result of the digital filter. range 12<>20 : high<>low amplification
volatile int FadeR = 0;
volatile int FadeL = 0;

boolean first = true; // reminds us to seed the filter on the first go
volatile boolean PulseR = false;  // becomes true when there is a heart pulse
volatile boolean PulseL = false;  // becomes true when there is a heart pulse
volatile boolean BR = false;     // becomes true when there is a heart pulse
volatile boolean BL = false;     // becomes true when there is a heart pulse
volatile boolean QSR = false;      // becomes true when pulse rate is determined. every 20 pulses
volatile boolean QSL = false;      // becomes true when pulse rate is determined. every 20 pulses

int pulsePinR = A0;  // pulse sensor purple wire connected to analog pin 5 - RIGHT player
int pulsePinL = A5; //  pulse sensor purple wire connected to analog pin 4 - LEFT player
int pumpPinR = 4;
int pumpPinL = 7;


void setup(){
pinMode(13,OUTPUT);    // pin 13 will blink to your heartbeat!
pinMode(pumpPinR, OUTPUT);
pinMode(pumpPinL, OUTPUT);
Serial.begin(115200); // we agree to talk fast!
// this next bit will wind up in the library. it initializes Timer1 to throw an interrupt every 1mS.
TCCR1A = 0x00; // DISABLE OUTPUTS AND BREAK PWM ON DIGITAL PINS 9 & 10
TCCR1B = 0x11; // GO INTO 'PHASE AND FREQUENCY CORRECT' MODE, NO PRESCALER
TCCR1C = 0x00; // DON'T FORCE COMPARE
TIMSK1 = 0x01; // ENABLE OVERFLOW INTERRUPT (TOIE1)
ICR1 = 8000;   // TRIGGER TIMER INTERRUPT EVERY 1mS  
sei();         // MAKE SURE GLOBAL INTERRUPTS ARE ENABLED

}



void loop(){
  FadeR -= 15;
  FadeR = constrain(FadeR,0,255);
  analogWrite(11,FadeR);
  FadeL -= 15;
  FadeL = constrain(FadeL,0,255);
  //analogWrite(11,FadeL);
  
delay(20);                    //  take a break

}

// THIS IS THE TIMER 1 INTERRUPT SERVICE ROUTINE. IT WILL BE PUT INTO THE LIBRARY
ISR(TIMER1_OVF_vect){ // triggered every time Timer 1 overflows
// Timer 1 makes sure that we take a reading every milisecond
SignalR = analogRead(pulsePinR);
SignalL = analogRead(pulsePinL);

// First normailize the waveform around 0
readingsR += SignalR; // take a running total
readingsL += SignalL; // take a running total

sampleCounter++;     // we do this every milisecond. this timer is used as a clock
if ((sampleCounter %300) == 0){   // adjust as needed
  offsetR = readingsR / 300;        // average the running total
  offsetL = readingsL / 300;        
  readingsR = 0;                   // reset running total
  readingsL = 0;                   
}
NSignalR = SignalR - offsetR;        // normalizing here
NSignalL = SignalL - offsetL;        // normalizing here

// IF IT'S THE FIRST TIME THROUGH THE SKETCH, SEED THE FILTER WITH CURRENT DATA
if (first = true){
	for (int pos=0; pos<2; pos++){
	  for (int i=0; i<4; i++){
		if (pos == 0) {
			Lxv[pos][i] = Lyv[pos][i] = NSignalR <<10;  // seed the lowpass filter
			Hxv[pos][i] = Hyv[pos][i] = NSignalR <<10;  // seed the highpass filter
		} else if (pos == 1) {
			Lxv[pos][i] = Lyv[pos][i] = NSignalL <<10;  // seed the lowpass filter
			Hxv[pos][i] = Hyv[pos][i] = NSignalL <<10;  // seed the highpass filter
		}
	  }
	}
first = false;      // only seed once please
}
// THIS IS THE BANDPAS FILTER. GENERATED AT www-users.cs.york.ac.uk/~fisher/mkfilter/trad.html
//  BUTTERWORTH LOWPASS ORDER = 3; SAMPLERATE = 1mS; CORNER = 5Hz

for (int pos=0; pos<2; pos++){
    Lxv[pos][0] = Lxv[pos][1]; Lxv[pos][1] = Lxv[pos][2]; Lxv[pos][2] = Lxv[pos][3];
    if (pos == 0) {
		Lxv[pos][3] = NSignalR<<10;    // insert the normalized data into the lowpass filter
	} else if (pos == 1) {
		Lxv[pos][3] = NSignalL<<10;    // insert the normalized data into the lowpass filter	
	}
    Lyv[pos][0] = Lyv[pos][1]; Lyv[pos][1] = Lyv[pos][2]; Lyv[pos][2] = Lyv[pos][3];
    Lyv[pos][3] = (Lxv[pos][0] + Lxv[pos][3]) + 3 * (Lxv[pos][1] + Lxv[pos][2])
          + (3846 * Lyv[pos][0]) + (-11781 * Lyv[pos][1]) + (12031 * Lyv[pos][2]);
//  Butterworth; Highpass; Order = 3; Sample Rate = 1mS; Corner = .8Hz
    Hxv[pos][0] = Hxv[pos][1]; Hxv[pos][1] = Hxv[pos][2]; Hxv[pos][2] = Hxv[pos][3];
    Hxv[pos][3] = Lyv[pos][3] / 4116; // insert lowpass result into highpass filter
    Hyv[pos][0] = Hyv[pos][1]; Hyv[pos][1] = Hyv[pos][2]; Hyv[pos][2] = Hyv[pos][3];
    Hyv[pos][3] = (Hxv[pos][3]-Hxv[pos][0]) + 3 * (Hxv[pos][1] - Hxv[pos][2])
          + (8110 * Hyv[pos][0]) + (-12206 * Hyv[pos][1]) + (12031 * Hyv[pos][2]);
}
FSignalR = Hyv[0][3] >> Scale;  // result of highpass shift-scaled
FSignalL = Hyv[1][3] >> Scale;  // result of highpass shift-scaled

//PLAY AROUND WITH THE SHIFT VALUE TO SCALE THE OUTPUT ~12 <> ~20 = High <> Low Amplification.

if (FSignalR >= PeakR && PulseR == false){  // heart beat causes ADC readings to surge down in value.  
  PeakR = FSignalR;                        // finding the moment when the downward pulse starts
  peakTimeR = sampleCounter;              // recodrd the time to derive HRV. 
}
if (FSignalL >= PeakL && PulseL == false){  // heart beat causes ADC readings to surge down in value.  
  PeakL = FSignalL;                        // finding the moment when the downward pulse starts
  peakTimeL = sampleCounter;              // recodrd the time to derive HRV. 
}
//  NOW IT'S TIME TO LOOK FOR THE HEART BEAT

// RIGHT player
if ((sampleCounter %20) == 0){// only look for the beat every 20mS. This clears out alot of high frequency noise.
  if (FSignalR < 0 && PulseR == false){  // signal surges down in value every time there is a pulse
     PulseR = true;                     // Pulse will stay true as long as pulse signal < 0
     digitalWrite(13,HIGH);            // pin 13 will stay high as long as pulse signal < 0  
     digitalWrite(pumpPinR, HIGH);
     Serial.println("R_high");                    // send to processing
     FadeR = 255;                       // set the fade value to highest for fading LED on pin 11 (optional)   
     HRVR = peakTimeR - lastPeakTimeR;    // measure time between beats
     lastPeakTimeR = peakTimeR;          // keep track of time for next pulse
     BR = true;                         // set the Quantified Self flag when HRV gets updated. NOT cleared inside this ISR     
     rateR += HRVR;                      // add to the running total of HRV used to determine heart rate
     beatCounterR++;                     // beatCounter times when to calculate bpm by averaging the beat time values
     if (beatCounterR == 10){            // derive heart rate every 10 beats. adjust as needed
       rateR /= beatCounterR;             // averaging time between beats
       BPMR = 60000/rateR;                // how many beats can fit into a minute?
       beatCounterR = 0;                 // reset counter
       rateR = 0;                        // reset running total
       QSR = true;                       // set Beat flag when BPM gets updated. NOT cleared inside this ISR
     }
  }
  if (FSignalR > 0 && PulseR == true){    // when the values are going up, it's the time between beats
    digitalWrite(13,LOW);    // so turn off the pin 13 LED
    digitalWrite(pumpPinR, LOW);
    Serial.println("R_low");
    PulseR = false;                      // reset these variables so we can do it again!
    PeakR = 0;                           // 
  }
  
  // LEFT player
  if (FSignalL < 0 && PulseL == false){  // signal surges down in value every time there is a pulse
     PulseL = true;                     // Pulse will stay true as long as pulse signal < 0
     digitalWrite(13,HIGH);            // pin 13 will stay high as long as pulse signal < 0  
     digitalWrite(pumpPinL, HIGH);
     Serial.println("L_high");                  // send to processing
     FadeL = 255;                       // set the fade value to highest for fading LED on pin 11 (optional)   
     HRVL = peakTimeL - lastPeakTimeL;    // measure time between beats
     lastPeakTimeL = peakTimeL;          // keep track of time for next pulse
     BL = true;                         // set the Quantified Self flag when HRV gets updated. NOT cleared inside this ISR     
     rateL += HRVL;                      // add to the running total of HRV used to determine heart rate
     beatCounterL++;                     // beatCounter times when to calculate bpm by averaging the beat time values
     if (beatCounterL == 10){            // derive heart rate every 10 beats. adjust as needed
       rateL /= beatCounterL;             // averaging time between beats
       BPML = 60000/rateL;                // how many beats can fit into a minute?
       beatCounterL = 0;                 // reset counter
       rateL = 0;                        // reset running total
       QSL = true;                       // set Beat flag when BPM gets updated. NOT cleared inside this ISR
     }
  }
  if (FSignalL > 0 && PulseL == true){    // when the values are going up, it's the time between beats
    digitalWrite(13,LOW);               // so turn off the pin 13 LED
    digitalWrite(pumpPinL, LOW);
    Serial.println("L_low");
    PulseL = false;                      // reset these variables so we can do it again!
    PeakL = 0;                           // 
  }
}

}// end isr





