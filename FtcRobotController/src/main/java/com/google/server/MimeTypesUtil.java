package com.google.server;

import java.util.HashMap;
import java.util.Map;

public class MimeTypesUtil {
  private static final Map<String, String> MIME_TYPES_BY_EXT = new HashMap<String, String>();
  static {
    MIME_TYPES_BY_EXT.put("asc", "text/plain");
    MIME_TYPES_BY_EXT.put("class", "application/octet-stream");
    MIME_TYPES_BY_EXT.put("css", "text/css");
    MIME_TYPES_BY_EXT.put("cur", "image/x-win-bitmap");
    MIME_TYPES_BY_EXT.put("doc", "application/msword");
    MIME_TYPES_BY_EXT.put("exe", "application/octet-stream");
    MIME_TYPES_BY_EXT.put("flv", "video/x-flv");
    MIME_TYPES_BY_EXT.put("gif", "image/gif");
    MIME_TYPES_BY_EXT.put("html", "text/html");
    MIME_TYPES_BY_EXT.put("htm", "text/html");
    MIME_TYPES_BY_EXT.put("ico", "image/x-icon");
    MIME_TYPES_BY_EXT.put("java", "text/x-java-source, text/java");
    MIME_TYPES_BY_EXT.put("jpeg", "image/jpeg");
    MIME_TYPES_BY_EXT.put("jpg", "image/jpeg");
    MIME_TYPES_BY_EXT.put("js", "application/javascript");
    MIME_TYPES_BY_EXT.put("m3u8", "application/vnd.apple.mpegurl");
    MIME_TYPES_BY_EXT.put("m3u", "audio/mpeg-url");
    MIME_TYPES_BY_EXT.put("md", "text/plain");
    MIME_TYPES_BY_EXT.put("mov", "video/quicktime");
    MIME_TYPES_BY_EXT.put("mp3", "audio/mpeg");
    MIME_TYPES_BY_EXT.put("mp4", "video/mp4");
    MIME_TYPES_BY_EXT.put("ogg", "application/x-ogg");
    MIME_TYPES_BY_EXT.put("ogv", "video/ogg");
    MIME_TYPES_BY_EXT.put("pdf", "application/pdf");
    MIME_TYPES_BY_EXT.put("png", "image/png");
    MIME_TYPES_BY_EXT.put("svg", "image/svg+xml");
    MIME_TYPES_BY_EXT.put("swf", "application/x-shockwave-flash");
    MIME_TYPES_BY_EXT.put("ts", "video/mp2t");
    MIME_TYPES_BY_EXT.put("txt", "text/plain");
    MIME_TYPES_BY_EXT.put("wav", "audio/wav");
    MIME_TYPES_BY_EXT.put("xml", "text/xml");
    MIME_TYPES_BY_EXT.put("zip", "application/octet-stream");
  }

  // Prevent instantiation of util class.
  private MimeTypesUtil() {
  }

  public static String getMimeType(String extension) {
    // Remove leading dot, if necessary
    if (extension.startsWith(".")) {
      extension = extension.substring(1);
    }
    return MIME_TYPES_BY_EXT.get(extension);
  }
}
