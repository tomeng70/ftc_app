package com.google.blocks;

import android.webkit.JavascriptInterface;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * A class that provides JavaScript access to a {@link TouchSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class TouchSensorAccess {
  private final TouchSensor touchSensor;

  TouchSensorAccess(HardwareMap hardwareMap, String deviceName) {
    TouchSensor touchSensor = null;
    try {
      touchSensor = hardwareMap.touchSensor.get(deviceName);
    } catch (Exception e) {
      RobotLog.e("TouchSensorAccess - caught " + e);
      RobotLog.e("TouchSensorAccess - touchSensor is null");
    }
    this.touchSensor = touchSensor;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean isPressed() {
    if (touchSensor != null) {
      return touchSensor.isPressed();
    }
    return false;
  }
}
