package com.google.blocks;

import android.webkit.JavascriptInterface;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * A class that provides JavaScript access to a {@link OpticalDistanceSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class OpticalDistanceSensorAccess {
  private final OpticalDistanceSensor opticalDistanceSensor;

  OpticalDistanceSensorAccess(HardwareMap hardwareMap, String deviceName) {
    OpticalDistanceSensor opticalDistanceSensor = null;
    try {
      opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get(deviceName);
    } catch (Exception e) {
      RobotLog.e("OpticalDistanceSensorAccess - caught " + e);
      RobotLog.e("OpticalDistanceSensorAccess - opticalDistanceSensor is null");
    }
    this.opticalDistanceSensor = opticalDistanceSensor;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getLightDetected() {
    if (opticalDistanceSensor != null) {
      return opticalDistanceSensor.getLightDetected();
    }
    return 0.0;
  }
}
