package com.google.server;

import fi.iki.elonen.NanoHTTPD;

import com.google.util.ProjectsUtil;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.util.RobotLog;

import android.app.Activity;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * An HTTP server that serves FtcBlocks.html and handles the following requests:
 * <ul>
 * <li>/list - list existing projects</li>
 * <li>/load - load the blocks of a project</li>
 * <li>/save - save the blocks and javascript of a project</li>
 * </ul>
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class Server extends NanoHTTPD {
  private static final String URI_LIST = "/list";
  private static final String URI_LOAD = "/load";
  private static final String URI_SAVE = "/save";
  private static final String PARAM_PROJECT = "project";
  private static final String PARAM_BLK = "blk";
  private static final String PARAM_JS = "js";

  private final Activity activity;
  private final AssetManager assetManager;
  private final TextView textView;

  public Server(int port, Activity activity) {
    super(port);
    this.activity = activity;
    this.assetManager = activity.getAssets();
    this.textView = (TextView) activity.findViewById(R.id.log);
  }

  @Override
  public Response serve(
      String uri, Method method, Map<String, String> headers, Map<String, String> parms,
      Map<String, String> files) {
    try {
      addToLog(uri);

      if (uri.equals("/")) {
        Response response = newFixedLengthResponse(
            Response.Status.REDIRECT, NanoHTTPD.MIME_PLAINTEXT, "");
        response.addHeader("Location", "/FtcBlocks.html");
        return response;
      }

      if (uri.equals(URI_LIST)) {
        return list();
      }

      if (uri.equals(URI_LOAD)) {
        String project = parms.get(PARAM_PROJECT);
        if (project != null) {
          return load(project);
        } else {
          return newFixedLengthResponse(
              Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT,
              "Bad Request: project parameter is required");
        }
      }

      if (uri.equals(URI_SAVE)) {
        String project = parms.get(PARAM_PROJECT);
        String blk = parms.get(PARAM_BLK);
        String js = parms.get(PARAM_JS);
        if (project != null && blk != null && js != null) {
          return save(project, blk, js);
        } else {
          return newFixedLengthResponse(
              Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT,
              "Bad Request: project, blk, and js parameters are required");
        }
      }

      // Load files from assets.
      String path = uri.startsWith("/") ? uri.substring(1) : uri;
      return serveAssetFile(path);
    } catch (IOException e) {
      RobotLog.logStacktrace(e);
      return newFixedLengthResponse(
          Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "Internal Error");
    }
  }

  private void addToLog(final String msg) {
    final Runnable updateTextView = new Runnable() {
      @Override
      public void run() {
        textView.setText(textView.getText() + msg + "\n");
        textView.requestLayout();
      }
    };
    activity.runOnUiThread(updateTextView);
  }

  private Response list() throws IOException {
    String[] projects = ProjectsUtil.listProjectsBlocks();
    String text = TextUtils.join(",", projects);
    return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, text);
  }

  private Response load(String project) throws IOException {
    String content = ProjectsUtil.loadProjectBlocks(project);
    return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, content);
  }

  private Response save(String project, String blkContent, String jsContent) throws IOException {
    ProjectsUtil.saveProject(project, blkContent, jsContent);
    return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "");
  }

  private Response serveAssetFile(String path) throws IOException {
    String mimeType = NanoHTTPD.MIME_PLAINTEXT;
    int lastDot = path.lastIndexOf(".");
    if (lastDot != -1) {
      String ext = path.substring(lastDot + 1);
      mimeType = MimeTypesUtil.getMimeType(ext);
      if (mimeType == null) {
        throw new IllegalStateException("Mime type unknown for file extension " + ext);
      }
    }

    InputStream inputStream;
    try {
      inputStream = assetManager.open(path);
    } catch (IOException e) {
      return newFixedLengthResponse(
          Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "");
    }
    return newChunkedResponse(Response.Status.OK, mimeType, inputStream);
  }
}
