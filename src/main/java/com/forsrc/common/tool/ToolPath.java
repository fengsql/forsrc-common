package com.forsrc.common.tool;

import com.forsrc.common.base.BObject;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class ToolPath extends BObject {
  private static final String path_webinf = "/WEB-INF/";
  private static final String path_webinf_classes = "/WEB-INF/classes/";
  private static final String path_webinf_lib = "/WEB-INF/lib/";
  private static final String path_config = "config/project/";
  private static final String path_src = "/src/main/java";

  private static String classPath = null;

  static {
    initPath();
  }

  // <<----------------------- path -----------------------

  public static String getClassPath() {
    return classPath;
  }

  public static String getPathJar() {
    String jarFile = ToolPath.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    return ToolFile.getFilePath(jarFile);
  }

  // >>----------------------- path -----------------------

  // <<----------------------- private -----------------------

  private static void initPath() {
    if (classPath == null) {
      classPath = System.getProperty("user.dir");
      classPath += path_src;
      classPath = classPath.replace('/', File.separatorChar).replace('\\', File.separatorChar);
      log.info("classPath: " + classPath);
    }
  }

  // >>----------------------- private -----------------------

}