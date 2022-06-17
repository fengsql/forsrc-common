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

@Slf4j
public class UploadFile extends BService {

  @Value("${website.upload.folder:upload}")
  private String uploadFolder;

  public UploadFile() {

  }

  // <<----------------------- initialize -----------------------

  // >>----------------------- initialize -----------------------

  // <<----------------------- abstract -----------------------

  // >>----------------------- abstract -----------------------

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  /**
   * 上传文件，返回保存后的路径和文件名，相对路径。
   * @param file 上传文件。
   * @return 返回保存后的路径和文件名，相对路径。
   */
  public String work(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
    return doWork(request, response, file);
  }

  /**
   * 上传文件，返回保存后的路径和文件名，绝对路径。
   * @param file 上传文件。
   * @return 返回保存后的路径和文件名，绝对路径。
   */
  public String save(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
    return saveFile(request, response, file);
  }

  // >>>----------------------- normal -----------------------

  // <<<----------------------- tool -----------------------

  // >>>----------------------- tool -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- protected -----------------------

  // >>----------------------- protected -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  // >>>----------------------- normal -----------------------

  // <<<----------------------- upload -----------------------

  private String doWork(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
    String saveFile = saveFile(request, response, file);
    //获取tomcat容器目录
    String path = request.getSession().getServletContext().getRealPath("");
    String url = Tool.subString(saveFile, path.length());
    url = Tool.toUrlPath(url);
    return url;
  }

  private String saveFile(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
    if (file == null) {
      throw new CommonException(Code.PARAM_EMPTY);
    }
    //获取tomcat容器目录
    String path = request.getSession().getServletContext().getRealPath("");
    //获取classes目录绝对路径
    //    String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
    //    String path = ResourceUtils.getURL("classpath:").getPath();
    //获取当前项目路径的地址
    //    String path = System.getProperty("user.dir");

    String uploadPath = ToolFile.joinFileName(path, uploadFolder);
    //    log.info("uploadPath: " + uploadPath);
    ToolFile.forcePath(uploadPath);

    String originFile = file.getOriginalFilename();
    String fileExt = ToolFile.getFileExt(originFile); // 扩展名
    String saveFile = ToolFile.getRandomFile(uploadPath, fileExt);
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

  // >>>----------------------- upload -----------------------

  // <<<----------------------- tool -----------------------

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

  // <<<----------------------- get -----------------------

  // >>>----------------------- get -----------------------

  // <<<----------------------- set -----------------------

  // >>>----------------------- set -----------------------

  // <<----------------------- get set -----------------------

  // >>----------------------- get set -----------------------

}
