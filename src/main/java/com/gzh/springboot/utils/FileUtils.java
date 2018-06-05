package com.gzh.springboot.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;


import sun.misc.BASE64Decoder;

/**
 * 文件操作工具类 实现文件的创建、删除、复制、压缩、解压以及目录的创建、删除、复制、压缩解压等功能
 * 
 * @author ThinkGem
 * @version 2013-06-21
 */
@SuppressWarnings({"restriction","unused"})
public class FileUtils{

	private static Logger log = LoggerFactory.getLogger(FileUtils.class);
	
	
	

	/**
	 * 复制单个文件，如果目标文件存在，则不覆盖
	 * 
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String descFileName) {
		return FileUtils.copyFileCover(srcFileName, descFileName, false);
	}

	/**
	 * 复制单个文件
	 * 
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @param coverlay
	 *            如果目标文件已存在，是否覆盖
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFileCover(String srcFileName, String descFileName, boolean coverlay) {
		File srcFile = new File(srcFileName);
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			log.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
			return false;
		}
		// 判断源文件是否是合法的文件
		else if (!srcFile.isFile()) {
			log.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
			return false;
		}
		File descFile = new File(descFileName);
		// 判断目标文件是否存在
		if (descFile.exists()) {
			// 如果目标文件存在，并且允许覆盖
			if (coverlay) {
				log.debug("目标文件已存在，准备删除!");
				if (!FileUtils.delFile(descFileName)) {
					log.debug("删除目标文件 " + descFileName + " 失败!");
					return false;
				}
			} else {
				log.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
				return false;
			}
		} else {
			if (!descFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建目录
				log.debug("目标文件所在的目录不存在，创建目录!");
				// 创建目标文件所在的目录
				if (!descFile.getParentFile().mkdirs()) {
					log.debug("创建目标文件所在的目录失败!");
					return false;
				}
			}
		}

		// 准备复制文件
		// 读取的位数
		int readByte = 0;
		InputStream ins = null;
		OutputStream outs = null;
		try {
			// 打开源文件
			ins = new FileInputStream(srcFile);
			// 打开目标文件的输出流
			outs = new FileOutputStream(descFile);
			byte[] buf = new byte[1024];
			// 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
			while ((readByte = ins.read(buf)) != -1) {
				// 将读取的字节流写入到输出流
				outs.write(buf, 0, readByte);
			}
			log.debug("复制单个文件 " + srcFileName + " 到" + descFileName + "成功!");
			return true;
		} catch (Exception e) {
			log.debug("复制文件失败：" + e.getMessage());
			return false;
		} finally {
			// 关闭输入输出流，首先关闭输出流，然后再关闭输入流
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException oute) {
					oute.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException ine) {
					ine.printStackTrace();
				}
			}
		}
	}

	/**
	 * 复制整个目录的内容，如果目标目录存在，则不覆盖
	 * 
	 * @param srcDirName
	 *            源目录名
	 * @param descDirName
	 *            目标目录名
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectory(String srcDirName, String descDirName) {
		return FileUtils.copyDirectoryCover(srcDirName, descDirName, false);
	}

	/**
	 * 复制整个目录的内容
	 * 
	 * @param srcDirName
	 *            源目录名
	 * @param descDirName
	 *            目标目录名
	 * @param coverlay
	 *            如果目标目录存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectoryCover(String srcDirName, String descDirName, boolean coverlay) {
		File srcDir = new File(srcDirName);
		// 判断源目录是否存在
		if (!srcDir.exists()) {
			log.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
			return false;
		}
		// 判断源目录是否是目录
		else if (!srcDir.isDirectory()) {
			log.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
			return false;
		}
		// 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
		String descDirNames = descDirName;
		if (!descDirNames.endsWith(File.separator)) {
			descDirNames = descDirNames + File.separator;
		}
		File descDir = new File(descDirNames);
		// 如果目标文件夹存在
		if (descDir.exists()) {
			if (coverlay) {
				// 允许覆盖目标目录
				log.debug("目标目录已存在，准备删除!");
				if (!FileUtils.delFile(descDirNames)) {
					log.debug("删除目录 " + descDirNames + " 失败!");
					return false;
				}
			} else {
				log.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
				return false;
			}
		} else {
			// 创建目标目录
			log.debug("目标目录不存在，准备创建!");
			if (!descDir.mkdirs()) {
				log.debug("创建目标目录失败!");
				return false;
			}

		}

		boolean flag = true;
		// 列出源目录下的所有文件名和子目录名
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 如果是一个单个文件，则直接复制
			if (files[i].isFile()) {
				flag = FileUtils.copyFile(files[i].getAbsolutePath(), descDirName + files[i].getName());
				// 如果拷贝文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 如果是子目录，则继续复制目录
			if (files[i].isDirectory()) {
				flag = FileUtils.copyDirectory(files[i].getAbsolutePath(), descDirName + files[i].getName());
				// 如果拷贝目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
			return false;
		}
		log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
		return true;

	}

	/**
	 * 
	 * 删除文件，可以删除单个文件或文件夹
	 * 
	 * @param fileName
	 *            被删除的文件名
	 * @return 如果删除成功，则返回true，否是返回false
	 */
	public static boolean delFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			log.debug(fileName + " 文件不存在!");
			return true;
		} else {
			if (file.isFile()) {
				return FileUtils.deleteFile(fileName) == 0 ? true : false;
			} else {
				return FileUtils.deleteDirectory(fileName);
			}
		}
	}

	public static void delImg(HttpServletRequest request, String imgUrl) {
		if (StringUtils.isNotBlank(imgUrl)) {
			String imgPath = request.getSession().getServletContext().getRealPath("/") + imgUrl;
			File delfile = new File(imgPath);
			// 判断目录或文件是否存在
			if (delfile.isFile() && delfile.exists()) {
				delfile.delete();
			}
		}
	}

	/**
	 * 
	 * 删除单个文件
	 * 
	 * @param fileName
	 *            被删除的文件名
	 * @return 如果删除成功，则返回true，否则返回false
	 */
	public static Integer deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				log.debug("删除单个文件 " + fileName + " 成功!");
				return 0;
			} else {
				log.debug("删除单个文件 " + fileName + " 失败!");
				return 1;
			}
		} else {
			log.debug(fileName + " 文件不存在!");
			return 2;
		}
	}

	/**
	 * 
	 * 删除目录及目录下的文件
	 * 
	 * @param dirName
	 *            被删除的目录所在的文件路径
	 * @return 如果目录删除成功，则返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dirName) {
		String dirNames = dirName;
		if (!dirNames.endsWith(File.separator)) {
			dirNames = dirNames + File.separator;
		}
		File dirFile = new File(dirNames);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			log.debug(dirNames + " 目录不存在!");
			return true;
		}
		boolean flag = true;
		// 列出全部文件及子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = FileUtils.deleteFile(files[i].getAbsolutePath()) == 0 ? true : false;
				// 如果删除文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = FileUtils.deleteDirectory(files[i].getAbsolutePath());
				// 如果删除子目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			log.debug("删除目录失败!");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			log.debug("删除目录 " + dirName + " 成功!");
			return true;
		} else {
			log.debug("删除目录 " + dirName + " 失败!");
			return false;
		}

	}

	/**
	 * 创建单个文件
	 * 
	 * @param descFileName
	 *            文件名，包含路径
	 * @return 如果创建成功，则返回true，否则返回false
	 */
	public static boolean createFile(String descFileName) {
		File file = new File(descFileName);
		if (file.exists()) {
			log.debug("文件 " + descFileName + " 已存在!");
			return false;
		}
		if (descFileName.endsWith(File.separator)) {
			log.debug(descFileName + " 为目录，不能创建目录!");
			return false;
		}
		if (!file.getParentFile().exists()) {
			// 如果文件所在的目录不存在，则创建目录
			if (!file.getParentFile().mkdirs()) {
				log.debug("创建文件所在的目录失败!");
				return false;
			}
		}

		// 创建文件
		try {
			if (file.createNewFile()) {
				log.debug(descFileName + " 文件创建成功!");
				return true;
			} else {
				log.debug(descFileName + " 文件创建失败!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(descFileName + " 文件创建失败!");
			return false;
		}

	}

	/**
	 * 创建目录
	 * 
	 * @param descDirName
	 *            目录名,包含路径
	 * @return 如果创建成功，则返回true，否则返回false
	 */
	public static Integer createDirectory(String descDirName) {
		String descDirNames = descDirName;
		if (!descDirNames.endsWith(File.separator)) {
			descDirNames = descDirNames + File.separator;
		}
		File descDir = new File(descDirNames);
		if (descDir.exists()) {
			log.debug("目录 " + descDirNames + " 已存在!");
			return 1;
		}
		// 创建目录
		if (descDir.mkdirs()) {
			log.debug("目录 " + descDirNames + " 创建成功!");
			return 0;
		} else {
			log.debug("目录 " + descDirNames + " 创建失败!");
			return 2;
		}

	}

	/**
	 * 文件重命名
	 * 
	 * @param oldname
	 *            原来的文件名
	 * @param newname
	 *            新文件名
	 * @return Integer 返回类型 0:重命名成功 1：重命名文件不存在 2：目录下已存在与文件名相同名称的文件
	 *         3：新文件名和旧文件名相同...
	 */
	public static Integer renameFile(String pathOldname, String pathNewname) {
		if (!pathOldname.equals(pathNewname)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
			File oldfile = new File(pathOldname);
			File newfile = new File(pathNewname);
			if (!oldfile.exists()) {
				return 1;// 重命名文件不存在
			}
			if (newfile.exists()) {// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
				log.debug("目录 " + pathNewname + "已经存在！");
				return 2;
			} else {
				oldfile.renameTo(newfile);
				return 0;
			}
		} else {
			log.debug("新文件名和旧文件名相同...");
			return 3;
		}
	}

	/**
	 * @方法名 validateType
	 * @说明 (根据文件名称截取后缀判断文件名格式是否符合)
	 * @param 参数
	 * @param filename
	 *            文件名称
	 * @param 参数
	 * @param imgExts
	 *            合格的后缀数组
	 * @param 参数
	 * @return 设定文件
	 * @return Boolean 返回类型
	 * @throws 异常
	 */
	public static Boolean validateType(String filename, String[] imgExts) {
		Arrays.sort(imgExts);
		// 得到上传文件的扩展名
		String fileExt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
		int zt = Arrays.binarySearch(imgExts, fileExt);
		if (zt < 0) {
			return false;
		}
		return true;
	}

	/**
	 * 保存文件
	 * 
	 * @param file
	 *            上传的文件
	 * @param fileType
	 *            文件类型
	 * @param path
	 *            存放路径
	 * @return
	 */
	public static File saveFile(MultipartFile file, String fileType, String path) {
		// 得到上传文件的扩展名
		String fileName = file.getOriginalFilename();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		String newfileName = UUID.randomUUID().toString() + "." + fileExt;
		File depdfile = new File(path + File.separator + newfileName);
		if (!depdfile.exists()) {
			depdfile.mkdirs();
			try {
				file.transferTo(depdfile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return depdfile;
	}

	public static String saveFile(MultipartFile file, String path) {
		// 得到上传文件的扩展名
		String fileName = file.getOriginalFilename();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		String newfileName = fileName.substring(0, fileName.lastIndexOf(".")) + "." + fileExt;
		File depdfile = new File(path + File.separator + newfileName);

		if (!depdfile.exists()) {
			depdfile.mkdirs();
			try {
				file.transferTo(depdfile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return newfileName;
	}

	public static File saveFile(File file, String path) {
		// 得到上传文件的扩展名
		String fileName = file.getName();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		String newfileName = new Date().getTime() + "." + fileExt;
		File depdfile = new File(path + File.separator + newfileName);
		if (!depdfile.exists()) {
			depdfile.mkdirs();
			try {
				file.createNewFile();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return depdfile;
	}

	/**
	 * 
	 * @param file
	 *            Base64流
	 * @param path
	 *            保存路径
	 * @return
	 */
	public static String saveFile(String file, String path) {
		try {
			// Base64解码
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] b = decoder.decodeBuffer(file);
			for (int j = 0; j < b.length; ++j) {
				if (b[j] < 0) {// 调整异常数据
					b[j] += 256;
				}
			}
			String imgName = UUID.randomUUID().toString() + ".jpg";
			String imgFilePath = path + imgName;
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return imgName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 保存文件
	 * 
	 * @param 上传的图片文件
	 * @param request
	 *            请求
	 * @param bytes
	 *            字符流
	 * @return
	 */

	public static String addFile(HttpServletRequest request, byte[] bytes) {
		// 图片路径
		String docUrlStr = "";

		try {
			// 字符流转换为图片
			// 获取地址
			String realPath = request.getSession().getServletContext().getRealPath("/") + "uploadfile/app/";
			File destFile = new File(realPath);
			if (!destFile.exists()) {
				destFile.mkdirs();
			}
			// 获得日期
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmSS");
			// 图片名字
			String imgName = sDateFormat.format(new Date()) + ".jpg";
			// 图片路径
			String imgFilePath = realPath + imgName;

			// 定义输入流
			OutputStream oStream = new FileOutputStream(imgFilePath);

			// 写入流
			oStream.write(bytes);

			// 清空、关闭资源
			oStream.flush();
			oStream.close();

			docUrlStr = imgFilePath;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return docUrlStr;
	}


}
