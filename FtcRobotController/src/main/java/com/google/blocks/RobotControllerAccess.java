package com.google.blocks;

import android.webkit.JavascriptInterface;

import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.RobotLog;

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
  public void addNumericData(String key, double msg) {
    RobotLog.i("RobotControllerAccess.addNumericData - \"" + key + "\" " + msg);
    try {
      telemetry.addData(key, msg);
    } catch (Exception e) {
      RobotLog.e("RobotControllerAccess.addNumericData - caught " + e);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void addTextData(String key, String msg) {
    RobotLog.i("RobotControllerAccess.addTextData - \"" + key + "\" " + msg);
    try {
      telemetry.addData(key, msg);
    } catch (Exception e) {
      RobotLog.e("RobotControllerAccess.addTextData - caught " + e);
    }
  }
}
