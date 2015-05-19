package com.exacttarget.etpushsdk.location;

import android.location.Location;
import android.location.LocationListener;

public abstract interface ILastLocationFinder
{
  public abstract Location getLastBestLocation(int paramInt, long paramLong);
  
  public abstract void setChangedLocationListener(LocationListener paramLocationListener);
  
  public abstract void cancel();
}


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\location\ILastLocationFinder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */