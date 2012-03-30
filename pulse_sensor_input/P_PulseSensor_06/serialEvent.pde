/*
Serial data is sent from arduino with leading ascii character 
ascii character tells processing what to do with the data
*/


void serialEvent(Serial port){   
   String inData = port.readStringUntil('\n');  
   inData = trim(inData);             // trim the \n off the end
// leading 'Q' means heart rate data   
   if (inData.charAt(0) == 'Q'){      // following string contains current heart rate  
     inData = inData.substring(1);    // cut off the leading 'Q'
     pulseRate = int(inData);         // convert ascii string to integer
     newRate = true;                  // flag the new heart rate data so it can be mapped onscreen
     return;
   }  
// leading 'S' means sensor data   
   if (inData.charAt(0) == 'S'){      // following string contains raw sensor data
     inData = inData.substring(1);    // cut off the leading 'S'
     Sensor = int(inData);            // convert ascii string to integer
//  Set Ypos variable equal to new Sensor data value and fit it into the Pulse Window     
     Ypos = (PulseWindowY-PulseWindowH/2)+(PulseOffset - Sensor);
// If the new sensor data drifts outside the pulse window, adjust the pulse widnow offset so we can see the waveform     
     if (Ypos < PulseWindowMin || Ypos > PulseWindowMax){
       int selfCalibration = PulseDisplayBaseline - Sensor;
       selfCalibration = constrain(selfCalibration-200,-312,312);
       PulseOffset = PulseDisplayBaseline - selfCalibration; 
     }
   return;     
   }

// leading 'B' means arduino found a heart beat
   if (inData.charAt(0) == 'B'){          // following strint is time between beats in miliseconds
     inData = inData.substring(1);        // cut off the leading 'B'
     HRV = int(inData);                  // convert ascii string to integer
     beat = true;                         // set beat flag to advance heart rate graph
     heart = 10;                          // begin heart graphic 'swell' timer 
     return;
   }
 
   
   
}
