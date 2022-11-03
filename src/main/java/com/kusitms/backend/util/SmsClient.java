package com.kusitms.backend.util;

import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsClient {

  @Value("${custom.sms.api-key}")
  private String apiKey;

  @Value("${custom.sms.api-secret}")
  private String apiSecret;

  @Value("${custom.sms.source}")
  private String from;

  public void sendMessage(String contact, String code) {
    Message coolsms = new Message(apiKey, apiSecret);

    HashMap<String, String> params = new HashMap<>();
    params.put("to", contact);
    params.put("from", from);
    params.put("type", "SMS");
    params.put("text", "Duaily : 인증 번호는 [" + code + "] 입니다. (유효시간 : 3분)");
    params.put("app_version", "test app 1.2");

    try {
      JSONObject object = coolsms.send(params);
      log.debug(String.valueOf(object));
    } catch (CoolsmsException e) {
      throw new RuntimeException(e);
    }

  }
}
