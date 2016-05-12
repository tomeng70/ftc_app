package com.google.blocks;

import android.webkit.JavascriptInterface;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * A class that provides JavaScript access to a {@link LinearOpMode}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class LinearOpModeAccess {
  private final LinearOpMode linearOpMode; 

  LinearOpModeAccess(LinearOpMode linearOpMode) {
    this.linearOpMode = linearOpMode;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void waitForStart() throws InterruptedException {
    linearOpMode.waitForStart();
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void sleep(double millis) throws InterruptedException {
    linearOpMode.sleep((long) millis);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public boolean opModeIsActive() {
    return linearOpMode.opModeIsActive();
  }
}
