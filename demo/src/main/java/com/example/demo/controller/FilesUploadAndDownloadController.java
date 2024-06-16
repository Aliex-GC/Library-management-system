package com.example.demo.controller;


import com.example.demo.domain.RespBase;
import com.example.demo.entity.FileAttach;
import com.example.demo.mapper.FileAttachMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;


@RestController
@RequestMapping(value = "/file"  )
public class FilesUploadAndDownloadController {
	private static Logger logger = LoggerFactory.getLogger(FilesUploadAndDownloadController.class);

	@Value("${upload.path}")
    private String uploadPath;
	
	@Autowired
	private FileAttachMapper fileAttachMapper;
	/**
	 * 通用文件下载请求
	 *
	 * @param fileId 文件ID
	 */
	@GetMapping("/download")
	public void fileDownload(Long fileId, HttpServletResponse response, HttpServletRequest request)
	{
		try
		{
			if (fileId==null || fileId.longValue()==0){
				throw new Exception("File cannot be empty");
			}
			//获取文件信息
			FileAttach fileAttach=fileAttachMapper.selectById(fileId);
			
			//欲下载的文件的路径
			String abpath = this.uploadPath+File.separator + fileAttach.getFilePath();
			String fileName=fileAttach.getFileName();//原文件名
			File file = new File(abpath);
			if(!file.exists()) {
				throw new Exception("File not exist");
			}
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			// 判断浏览器类型
			if (request.getHeader("USER-AGENT").contains("MSIE")) {
				response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
			} else {
				response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
			}
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		}
		catch (Exception e)
		{
			logger.error("下载文件失败", e);
		}
	}


	/**
	 * 通用上传请求（单个文件）
	 */
	@PostMapping("/upload")
	public RespBase<FileAttach> uploadFile(MultipartFile file) throws Exception
	{
		RespBase<FileAttach> ret=new RespBase<FileAttach>();
		try
		{
			FileAttach fileAttach=this.saveFile(file);
			ret.setData(fileAttach);
			return ret;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret.setSuccess(false);
			ret.setMessage("上传失败+"+e.getMessage());
			return ret;

		}
	}
	/*处理单个文件保存至服务器指定目录，返回保存后的附件对象*/
	public FileAttach saveFile(MultipartFile file) {
		// TODO Auto-generated method stub
		String filename=file.getOriginalFilename();//获取文件原始文件名
		String ext=filename.substring(filename.lastIndexOf("."));//获取文件扩展名
		String newFileName=System.currentTimeMillis()+ext;//以系统时间生成新的文件名，防止文件名重复	
		File dir=new File(this.uploadPath); 
		if(!dir.exists()){
			//如保存目录不存在自动创建
			dir.mkdir(); 
		}
		String destPath=this.uploadPath+File.separator+newFileName;//文件上传的destination path
		File destFile=new File(destPath);		
		try {			
			FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);//copy文件
			//保存记录
			FileAttach fileAttach =new FileAttach();
			fileAttach.setFileName(filename);
			fileAttach.setFilePath(newFileName);
			fileAttach.setExt(ext);
			fileAttachMapper.insert(fileAttach);
			return fileAttach;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return null;
	}
	
	/**
	 * 通用上传请求（多个文件）

	@PostMapping("/uploadFiles")
	public RespBase<List<FileAttach>> uploadFiles(MultipartFile[] files) throws Exception
	{
		RespBase<List<FileAttach>> ret=new RespBase<List<FileAttach>>();
		List<FileAttach> paths=new ArrayList<FileAttach>();//返回上传成功文件列表
		try
		{
			for(MultipartFile item :files) {
				FileAttach fileAttach=this.saveFile(item);
				paths.add(fileAttach);
			}			
			ret.setData(paths);
			return ret;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret.setSuccess(false);
			ret.setMessage("上传失败+"+e.getMessage());
			return ret;

		}
	}
	*/
	
}
