package com.google.blocks;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.util.RobotLog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * An {@link Activity} that provides Blockly in a WebView.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class BlocksActivity extends Activity {

  protected Context context;

  protected WebView webView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_blocks);
    context = this;

    webView = (WebView) findViewById(R.id.webViewBlockly);
    webView.setWebChromeClient(new WebChromeClient());

    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);

    webView.addJavascriptInterface(new BlocksIO(), "blocksIO");
    webView.loadUrl("file:///android_asset/FtcBlocks.html");
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    // don't destroy assets on screen rotation
  }

  private class BlocksIO {
    @SuppressWarnings("unused")
    @JavascriptInterface
    public String loadBlocks() {
      // TODO(lizlooney): implement this.
      return "";
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void saveBlocks(String blocks, String scheme) {
      // TODO(lizlooney): ask user for script name.
      String name = "" + System.currentTimeMillis();
      File directory = new File("/sdcard/FIRST/blocks");
      directory.mkdirs();
      File blkFile = new File(directory, name + ".blk");
      File scmFile = new File(directory, name + ".scm");
      write(blocks, blkFile);
      write(scheme, scmFile);
    }
  }

  private static void write(String text, File file) {
    try {
      FileOutputStream stream = new FileOutputStream(file);
      try {
        stream.write(text.getBytes(StandardCharsets.ISO_8859_1));
      } finally {
        stream.close();
      }
    } catch (IOException e) {
      RobotLog.logStacktrace(e);
    }
  }
}
