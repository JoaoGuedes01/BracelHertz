#include <WiFi.h>

void ConnectToWiFi() {
  const char* ssid = "z+e";
  const char* password = "jdsdas";
  delay(10);
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);



 while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
    WiFi.begin(ssid, password);
}
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void setup() {
  Serial.begin(115200);
  ConnectToWiFi();
}

void loop() {
  int Signal = analogRead(36);
  Serial.println(Signal);
  delay(2500);
}