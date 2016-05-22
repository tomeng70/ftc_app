package com.google.blocks;

import com.google.util.ProjectsUtil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.util.RobotLog;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A subclass of {@link LinearOpMode} that loads JavaScript from a file and executes it.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class BlocksOpMode extends LinearOpMode {
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

  private final String project;
  private final String javaScript;

  /**
   * Instantiates a BlocksOpMode that loads JavaScript for the given project and executes it when
   * the opMode is run.
   *
   * @param project the name of the project.
   */
  private BlocksOpMode(String project, String javaScript) {    
    this.project = project;
    this.javaScript = javaScript;
  }

  @Override
  public void runOpMode() throws InterruptedException {
    RobotLog.i("BlocksOpMode - start of runOpMode for " + project);

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
      RobotLog.i("BlocksOpMode - end of runOpMode for " + project);
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
        + javaScript
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
   * Registers a {@link BlocksOpMode} for each blocks project that has a JavaScript file.
   */
  public static void registerAll(OpModeManager manager) {
    try {
      String[] projects = ProjectsUtil.listProjectsJavaScript();
      for (String project : projects) {
        try {
          String javaScript = ProjectsUtil.loadProjectJavaScript(project);
          manager.register(project, new BlocksOpMode(project, javaScript));
        } catch (Exception e) {
          RobotLog.logStacktrace(e);
        }
      }
    } catch (Exception e) {
      RobotLog.logStacktrace(e);
    }
  }
}
