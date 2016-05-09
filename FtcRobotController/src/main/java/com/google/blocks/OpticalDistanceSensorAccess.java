package com.google.blocks;

import android.webkit.JavascriptInterface;

import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * A class that provides JavaScript access to a {@link OpticalDistanceSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class OpticalDistanceSensorAccess {
  private final OpticalDistanceSensor opticalDistanceSensor;

  OpticalDistanceSensorAccess(OpticalDistanceSensor opticalDistanceSensor) {
    this.opticalDistanceSensor = opticalDistanceSensor;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getLightDetected() {
    return opticalDistanceSensor.getLightDetected();
  }
}
