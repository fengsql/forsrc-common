package com.forsrc.common.extend.work;

import com.forsrc.common.constant.Code;
import com.forsrc.common.exception.CommonException;
import com.forsrc.common.spring.base.BService;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UploadFile extends BService {

  @Value("${website.upload.folder:upload}")
  private String uploadFolder;

  public UploadFile() {

  }

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  /**
   * 上传文件，返回保存后的路径和文件名，相对路径，扩展名与原扩展名一致，文件名随机生成。
   * @param file     上传文件。
   * @param request  请求。
   * @param response 响应。
   * @return 返回保存后的路径和文件名，相对路径。
   */
  public String save(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
    Tool.notNull(file);
    return saveWeb(request, file);
  }

  public List<String> save(HttpServletRequest request, HttpServletResponse response, MultipartFile[] files) {
    Tool.notNull(files);
    return saveWeb(request, files);
  }

  /**
   * 上传文件，返回保存后的路径和文件名，绝对路径，文件名和扩展名与原文件名一致。
   * @param file       上传文件。
   * @param uploadPath 上传路径，绝对路径。
   * @return 返回保存后的路径和文件名，绝对路径。
   */
  public String save(MultipartFile file, String uploadPath) {
    Tool.notNull(file);
    Tool.notNull(uploadPath);
    return saveAbs(file, uploadPath);
  }

  public List<String> save(MultipartFile[] files, String uploadPath) {
    Tool.notNull(files);
    Tool.notNull(uploadPath);
    return saveAbs(files, uploadPath);
  }

  // >>>----------------------- normal -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- saveWeb -----------------------

  private List<String> saveWeb(HttpServletRequest request, MultipartFile[] files) {
    String contextPath = getContextPath(request);
    String uploadPath = getUploadPath(contextPath);
    List<String> list = new ArrayList<>();
    for (MultipartFile file : files) {
      list.add(saveWeb(file, contextPath, uploadPath));
    }
    return list;
  }

  private String saveWeb(HttpServletRequest request, MultipartFile file) {
    String contextPath = getContextPath(request);
    String uploadPath = getUploadPath(contextPath);
    return saveWeb(file, contextPath, uploadPath);
  }

  private String saveWeb(MultipartFile file, String contextPath, String uploadPath) {
    String saveFile = saveFile(file, uploadPath, true);
    String url = Tool.subString(saveFile, contextPath.length());
    url = Tool.toUrlPath(url);
    return url;
  }

  // >>>----------------------- saveWeb -----------------------

  // <<<----------------------- saveAbs -----------------------

  private List<String> saveAbs(MultipartFile[] files, String uploadPath) {
    List<String> list = new ArrayList<>();
    for (MultipartFile file : files) {
      list.add(saveAbs(file, uploadPath));
    }
    return list;
  }

  private String saveAbs(MultipartFile file, String uploadPath) {
    return saveFile(file, uploadPath, false);
  }

  // >>>----------------------- saveAbs -----------------------

  // <<<----------------------- saveFile -----------------------

  private String saveFile(MultipartFile file, String uploadPath, boolean isRandomName) {
    if (file == null) {
      throw new CommonException(Code.PARAM_EMPTY);
    }
    ToolFile.forcePath(uploadPath);
    if (!ToolFile.existPath(uploadPath)) {
      throw new CommonException("uploadPath not exist! path: " + uploadPath);
    }

    String originFile = file.getOriginalFilename();
    String saveFile;
    if (!isRandomName) {
      saveFile = ToolFile.joinFileName(uploadPath, originFile);
    } else {
      String fileExt = ToolFile.getFileExt(originFile);
      saveFile = ToolFile.getRandomFile(uploadPath, fileExt);
    }
    File targetFile = new File(saveFile);
    try {
      file.transferTo(targetFile);
      log.debug("save file ok. file: {}. origin: {}", saveFile, originFile);
      return saveFile;
    } catch (Exception e) {
      log.error("transferTo targetFile error! originFile: " + originFile, e);
      throw new CommonException(Code.FAIL.getCode(), "保存文件出现错误.");
    }
  }

  // >>>----------------------- saveFile -----------------------

  // <<<----------------------- tool -----------------------

  private String getContextPath(HttpServletRequest request) {
    return request.getSession().getServletContext().getRealPath("");  //获取tomcat容器目录
  }

  private String getUploadPath(String contextPath) {
    return ToolFile.joinFileName(contextPath, uploadFolder);
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}
