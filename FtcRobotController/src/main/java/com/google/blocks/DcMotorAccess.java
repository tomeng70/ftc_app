package com.google.blocks;

import android.webkit.JavascriptInterface;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.Direction;
import com.qualcomm.robotcore.hardware.DcMotorController.RunMode;

import java.util.Locale;

/**
 * A class that provides JavaScript access to a {@link DcMotor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class DcMotorAccess {
  private final DcMotor dcMotor;

  DcMotorAccess(DcMotor dcMotor) {
    this.dcMotor = dcMotor;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setPower(double power) {
    dcMotor.setPower(power);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setMode(String runModeString) {
    RunMode runMode = RunMode.valueOf(runModeString.toUpperCase(Locale.ENGLISH));
    dcMotor.setMode(runMode);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setTargetPosition(double position) {
    dcMotor.setTargetPosition((int) position);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setDirection(String directionString) {
    Direction direction = Direction.valueOf(directionString.toUpperCase(Locale.ENGLISH));
    dcMotor.setDirection(direction);
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getPower() {
    return dcMotor.getPower();
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public int getCurrentPosition() {
    return dcMotor.getCurrentPosition();
  }
}
