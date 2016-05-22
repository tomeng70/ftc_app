package com.google.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * A class that provides utility methods related to blocks projects.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class ProjectsUtil {
  private static final File BLOCKS_DIR = new File("/sdcard/FIRST/blocks");
  private static final String BLK_EXT = ".blk";
  private static final String JS_EXT = ".js";

  // Prevent instantiation of utility class.
  private ProjectsUtil() {}

  /**
   * Returns the names of existing blocks projects that have a blocks file.
   */
  public static String[] listProjectsBlocks() throws IOException {
    return listProjects(BLK_EXT);
  }

  /**
   * Returns the names of existing blocks projects that have a JavaScript file.
   */
  public static String[] listProjectsJavaScript() throws IOException {
    return listProjects(JS_EXT);
  }

  private static String[] listProjects(final String ext) {
    String[] files = BLOCKS_DIR.list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String filename) {
        return filename.endsWith(ext);
      }
    });
    if (files != null) {
      String[] projects = new String[files.length];
      for (int i = 0; i < files.length; i++) {
        projects[i] = files[i].substring(0, files[i].length() - ext.length());
      }
      return projects;
    }
    return new String[0];
  }

  /**
   * Returns the content of the blocks file with the given project name.
   *
   * @param project the name of the project
   */
  public static String loadProjectBlocks(String project) throws IOException {
    return readFile(new File(BLOCKS_DIR, project + BLK_EXT));
  }

  /**
   * Returns the content of the JavaScript file with the given project name.
   *
   * @param project the name of the project
   */
  public static String loadProjectJavaScript(String project) throws IOException {
    return readFile(new File(BLOCKS_DIR, project + JS_EXT));
  }

  private static String readFile(File file) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = new BufferedReader(new FileReader(file));
    try {
      String line = null;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
      }
    } finally {
      reader.close();
    }
    return sb.toString();
  }

  /**
   * Save the blocks file and JavaScript file with the given project name.
   *
   * @param project the name of the project
   * @param blkContent the content to write to the blocks file
   * @param jsContent the content to write to the JavaScript file.
   */
  public static void saveProject(String project, String blkContent, String jsContent)
      throws IOException {
    if (!BLOCKS_DIR.exists()) {
      BLOCKS_DIR.mkdirs();
    }
    writeFile(new File(BLOCKS_DIR, project + BLK_EXT), blkContent);
    writeFile(new File(BLOCKS_DIR, project + JS_EXT), jsContent);
  }

  private static void writeFile(File file, String content) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    try {
      writer.write(content);
    } finally {
      writer.close();
    }
  }
}
