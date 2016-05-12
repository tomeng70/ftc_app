package com.google.blocks;

import android.webkit.JavascriptInterface;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.Direction;
import com.qualcomm.robotcore.hardware.DcMotorController.RunMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.Locale;

/**
 * A class that provides JavaScript access to a {@link DcMotor}.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
class DcMotorAccess {
  private final DcMotor dcMotor;

  DcMotorAccess(HardwareMap hardwareMap, String deviceName) {
    DcMotor dcMotor = null;
    try {
      dcMotor = hardwareMap.dcMotor.get(deviceName);
    } catch (Exception e) {
      RobotLog.e("DcMotorAccess - caught " + e);
      RobotLog.e("DcMotorAccess - dcMotor is null");
    }
    this.dcMotor = dcMotor;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setPower(double power) {
    if (dcMotor != null) {
      dcMotor.setPower(power);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setMode(String runModeString) {
    if (dcMotor != null) {
      RunMode runMode = RunMode.valueOf(runModeString.toUpperCase(Locale.ENGLISH));
      dcMotor.setMode(runMode);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setTargetPosition(double position) {
    if (dcMotor != null) {
      dcMotor.setTargetPosition((int) position);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public void setDirection(String directionString) {
    if (dcMotor != null) {
      RobotLog.i("HeyLiz - DcMotorAccess.setDirection - directionString is " + directionString);
      Direction direction = Direction.valueOf(directionString.toUpperCase(Locale.ENGLISH));
      RobotLog.i("HeyLiz - DcMotorAccess.setDirection - direction is " + direction);
      dcMotor.setDirection(direction);
    }
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public double getPower() {
    if (dcMotor != null) {
      return dcMotor.getPower();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  @JavascriptInterface
  public int getCurrentPosition() {
    if (dcMotor != null) {
      return dcMotor.getCurrentPosition();
    }
    return 0;
  }
}
