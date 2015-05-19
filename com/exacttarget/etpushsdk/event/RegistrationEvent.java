package com.exacttarget.etpushsdk.event;

import com.exacttarget.etpushsdk.data.Registration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RegistrationEvent
  extends Registration
{
  private static final long serialVersionUID = 1L;
}


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\event\RegistrationEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */