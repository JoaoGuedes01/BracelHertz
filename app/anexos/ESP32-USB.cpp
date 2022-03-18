    int UpperThreshold = 518;
    int LowerThreshold = 490;
    int reading = 0;
    int BPM = 0;
    bool IgnoreReading = false;
    bool FirstPulseDetected = false;
    unsigned long FirstPulseTime = 0;
    unsigned long SecondPulseTime = 0;
    unsigned long PulseInterval = 0;
    
    void setup(){
      Serial.begin(115200);
    }

    void loop(){

      reading = analogRead(A0); 
      if(reading > UpperThreshold && IgnoreReading == false){
        if(FirstPulseDetected == false){
          FirstPulseTime = millis();
          FirstPulseDetected = true;
        }
        else{
          SecondPulseTime = millis();
          PulseInterval = SecondPulseTime - FirstPulseTime;
          FirstPulseTime = SecondPulseTime;
        }
        IgnoreReading = true;
      }
      if(reading < LowerThreshold){
        IgnoreReading = false;
      }  

       // uncomment these lines in case you want to view the various values in the console.....
      Serial.print(reading);
      Serial.println("");
      Serial.flush();
      delay(1000);
    }