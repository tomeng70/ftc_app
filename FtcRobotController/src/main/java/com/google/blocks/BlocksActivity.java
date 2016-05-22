package com.google.blocks;

import com.google.util.ProjectsUtil;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.util.RobotLog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;

/**
 * An {@link Activity} that provides Blockly in a WebView.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class BlocksActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_blocks);

    WebView webView = (WebView) findViewById(R.id.webViewBlockly);
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
    public String listProjects() throws IOException {
      String[] projects = ProjectsUtil.listProjectsBlocks();
      return TextUtils.join(",", projects);
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public String openProject(String project) throws IOException {
      return ProjectsUtil.loadProjectBlocks(project);
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public void saveProject(String project, String blkContent, String jsContent) throws IOException {
      ProjectsUtil.saveProject(project, blkContent, jsContent);
    }
  }
}
