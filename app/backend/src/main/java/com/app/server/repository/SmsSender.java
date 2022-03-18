package com.app.server.repository;

import com.app.server.payload.request.SmsRequest;

public interface SmsSender {

    void sendSms(SmsRequest smsRequest);

}
