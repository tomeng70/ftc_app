package com.google.blocks;

import android.webkit.JavascriptInterface;

import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * A class that provides JavaScript access to a {@link TouchSensor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class TouchSensorAccess {
  private final TouchSensor touchSensor;

  TouchSensorAccess(TouchSensor touchSensor) {
    this.touchSensor = touchSensor;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean isPressed() {
    return touchSensor.isPressed();
  }
}
