package com.google.blocks;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * A subclass of {@link LinearOpMode} that reads in javascript from a file and executes it.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class BlocksOpMode extends LinearOpMode {
  private static final String JAVASCRIPT_EXTENSION = ".js";

  private static final String BLOCKS_OP_MODE_IDENTIFIER = "blocksOpMode";
  private static final String LINEAR_OP_MODE_IDENTIFIER = "linearOpMode";
  private static final String ROBOT_CONTROLLER_IDENTIFIER = "robotController";
  private static final String LEFT_MOTOR_IDENTIFIER = "motorL";
  private static final String RIGHT_MOTOR_IDENTIFIER = "motorR";
  private static final String OPTICAL_DISTANCE_SENSOR_IDENTIFIER = "sensorOpticalDistance";
  private static final String TOUCH_SENSOR_IDENTIFIER = "sensorTouch";

  private static final String LEFT_MOTOR_DEVICE_NAME = "left_drive";
  private static final String RIGHT_MOTOR_DEVICE_NAME = "right_drive";
  private static final String TOUCH_SENSOR_DEVICE_NAME = "touch_sensor";
  private static final String OPTICAL_DISTANCE_SENSOR_DEVICE_NAME = "sensor_EOPD";

  private static Activity activity;
  private static WebView webView;

  private final String name;
  private final String script;

  /**
   * Instantiates a BlocksOpMode that reads javascript from the given file and executes it when the
   * opMode is run.
   */
  private BlocksOpMode(String name, File file) {
    this.name = name;
    script = readFile(file);
  }

  private String readFile(File file) {
    try {
      byte[] bytes = new byte[(int) file.length()];
      FileInputStream fileInputStream = new FileInputStream(file);
      try {
        fileInputStream.read(bytes);
      } finally {
        fileInputStream.close();
      }
      return new String(bytes, StandardCharsets.ISO_8859_1);
    } catch (IOException e) {
      RobotLog.logStacktrace(e);
    }
    return "";
  }

  @Override
  public void runOpMode() throws InterruptedException {
    RobotLog.i("BlocksOpMode - start of runOpMode for " + name);

    final Object scriptFinishedLock = new Object();

    synchronized (scriptFinishedLock) {
      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          try {
            addJavascriptInterfaces(scriptFinishedLock);
            loadScript();
          } catch (Exception e) {
            RobotLog.e("BlocksOpMode - run 1 - caught " + e);
            // The exception may not have a stacktrace, so we check before calling
            // RobotLog.logStacktrace.
            if (e.getStackTrace() != null) {
              RobotLog.logStacktrace(e);
            }
          }
        }
      });

      // Either the script will finish naturally, or the Stop button on the driver station will be
      // pressed. If the stop button is pressed, our thread will be terminated and the following
      // scriptFinishedLock.wait() will be interrupted.
      boolean interrupted = false;
      try {
        scriptFinishedLock.wait();
      } catch (InterruptedException e) {
        interrupted = true;
      }

      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          try {
            // TODO(lizlooney) - find out if the following stops a runaway script from executing.
            removeJavascriptInterfaces();

            clearScript();
          } catch (Exception e) {
            RobotLog.e("BlocksOpMode - run 2 - caught " + e);
            // The exception may not have a stacktrace, so we check before calling
            // RobotLog.logStacktrace.
            if (e.getStackTrace() != null) {
              RobotLog.logStacktrace(e);
            }
          }
        }
      });

      if (interrupted) {
        Thread.currentThread().interrupt();
      }
      RobotLog.i("BlocksOpMode - end of runOpMode for " + name);
    }
  }

  private void addJavascriptInterfaces(Object scriptFinishedLock) {
    webView.addJavascriptInterface(
        new JavascriptAccess(scriptFinishedLock), BLOCKS_OP_MODE_IDENTIFIER);
    webView.addJavascriptInterface(
        new LinearOpModeAccess(this), LINEAR_OP_MODE_IDENTIFIER);
    webView.addJavascriptInterface(
        new RobotControllerAccess(telemetry), ROBOT_CONTROLLER_IDENTIFIER);
    webView.addJavascriptInterface(
        new DcMotorAccess(hardwareMap, LEFT_MOTOR_DEVICE_NAME),
        LEFT_MOTOR_IDENTIFIER);
    webView.addJavascriptInterface(
        new DcMotorAccess(hardwareMap, RIGHT_MOTOR_DEVICE_NAME),
        RIGHT_MOTOR_IDENTIFIER);
    webView.addJavascriptInterface(
        new TouchSensorAccess(hardwareMap, TOUCH_SENSOR_DEVICE_NAME),
        TOUCH_SENSOR_IDENTIFIER);
    webView.addJavascriptInterface(
        new OpticalDistanceSensorAccess(hardwareMap, OPTICAL_DISTANCE_SENSOR_DEVICE_NAME),
        OPTICAL_DISTANCE_SENSOR_IDENTIFIER);
  }

  private void removeJavascriptInterfaces() {
    webView.removeJavascriptInterface(BLOCKS_OP_MODE_IDENTIFIER);
    webView.removeJavascriptInterface(LINEAR_OP_MODE_IDENTIFIER);
    webView.removeJavascriptInterface(ROBOT_CONTROLLER_IDENTIFIER);
    webView.removeJavascriptInterface(LEFT_MOTOR_IDENTIFIER);
    webView.removeJavascriptInterface(RIGHT_MOTOR_IDENTIFIER);
    webView.removeJavascriptInterface(OPTICAL_DISTANCE_SENSOR_IDENTIFIER);
    webView.removeJavascriptInterface(TOUCH_SENSOR_IDENTIFIER);
  }

  private class JavascriptAccess {
    private final Object scriptFinishedLock;

    private JavascriptAccess(Object scriptFinishedLock) {
      this.scriptFinishedLock = scriptFinishedLock;
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void scriptFinished() {
      synchronized (scriptFinishedLock) {
        scriptFinishedLock.notifyAll();
      }
    }
  }

  private void loadScript() {
    String html = "<html><body onload='runOpMode()'><script type='text/javascript'>"
        + "function runOpMode() {\n"
        + script
        + "  blocksOpMode.scriptFinished();\n"
        + "}\n"
        + "</script></body></html>\n";
    webView.loadData(html, "text/html", null /* encoding */);
  }

  private void clearScript() {
    webView.loadData("", "text/html", null /* encoding */);
  }

  /**
   * Sets the {@link WebView} so that all BlocksOpModes can access it.
   */
  public static void setActivityAndWebView(Activity a, WebView wv) {
    activity = a;
    webView = wv;
    webView.getSettings().setJavaScriptEnabled(true);
  }

  /**
   * Registers a {@link BlocksOpMode} for each javascript file found in /sdcard/FIRST/blocks.
   * Javascript files are identified by the .js file extension.
   */
  public static void registerAll(OpModeManager manager) {
    try {
      File directory = new File("/sdcard/FIRST/blocks");
      if (directory.isDirectory()) {
        File[] files = directory.listFiles(new FileFilter() {
          @Override
          public boolean accept(File file) {
            return file.isFile()
                && file.canRead()
                && file.getName().endsWith(JAVASCRIPT_EXTENSION);
          }
        });
        if (files != null) {
          for (File file :  files) {
            String name = file.getName();
            name = name.substring(0, name.length() - JAVASCRIPT_EXTENSION.length());
            try {
              manager.register(name, new BlocksOpMode(name, file));
            } catch (Exception e) {
              RobotLog.logStacktrace(e);
            }
          }
        }
      }
    } catch (Exception e) {
      RobotLog.logStacktrace(e);
    }
  }
}
