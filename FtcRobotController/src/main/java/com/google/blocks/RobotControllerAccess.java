package com.google.blocks;

import android.webkit.JavascriptInterface;

import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * A class that provides JavaScript access to {@link Telemetry} and other robot controller
 * functionality.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class RobotControllerAccess {
  private final Telemetry telemetry;

  RobotControllerAccess(Telemetry telemetry) {
    this.telemetry = telemetry;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void addData(String key, Object msg) {
    telemetry.addData(key, msg);
  }
}
