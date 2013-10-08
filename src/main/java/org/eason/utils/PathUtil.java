package org.eason.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * <p>Title: PathUtil</p>
 * <p>Description: 路径管理类</p>
 * @author   eason
 * @date     2013-1-25
 */
public class PathUtil {
	
	/** web项目根目录 */
	private static String webRoot = null;
	
	/** web项目class目录 */
	private static String classPath = null;

	static {
		classPath = Thread.currentThread().getContextClassLoader()
				.getResource("").getFile();
		File file = new File(classPath);
		classPath = file.getAbsolutePath();
		
		try {
			classPath = URLDecoder.decode(classPath, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
		if (classPath.contains("WEB-INF")) {
			webRoot = Thread.currentThread().getContextClassLoader()
				.getResource("../../").getPath();
		} else {
			webRoot = System.getProperty("user.dir");
			webRoot = webRoot + "/";
		}
		try {
			webRoot = URLDecoder.decode(webRoot, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取Web根目录，如果没有部署的话就是工程根目录
	 * @return the webRoot
	 */
	public static String getWebRoot() {
		return webRoot;
	}
	
	/**
	 * 获取Web根目录，如果没有部署的话就是工程根目录
	 * @return the currentPath
	 */
	public static String getPath() {
		return webRoot;
	}
	
	public static String getPath(String relativeFilePath) {
		return webRoot + relativeFilePath;
	}

	public static File getFile(String relativeFilePath) {
		return new File(webRoot + relativeFilePath);
	}

	/**
	 * 获取类资源的根路径
	 * @return the currentPath
	 */
	public static String getClassPath() {
		return classPath;
	}
	
	public static String getClassPath(String relativeFilePath) {
		return classPath + relativeFilePath;
	}

	public static File getClassPathFile(String relativeFilePath) {
		return new File(classPath + relativeFilePath);
	}
	
	/**
	 * 获取类所在的程序的classes目录路径 或者 所在jar的目录
	 * @return
	 */
	public static String getJarOrClassPath() {
		URL url = PathUtil.class.getProtectionDomain().getCodeSource().getLocation();
		String filePath = null;
		try {
			filePath = URLDecoder.decode(url.getPath(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (filePath.endsWith(".jar")){
			filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		}
		File file = new File(filePath);
		filePath = file.getAbsolutePath();
		return filePath;
	}

	public static String getRealPath() {
		String realPath = PathUtil.class.getClassLoader().getResource("").getFile();
		File file = new File(realPath);
		realPath = file.getAbsolutePath();
		try {
			realPath = URLDecoder.decode(realPath, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return realPath;
	}
	
	public final static void main(String[] args){
		System.out.println(PathUtil.getJarOrClassPath());
		System.out.println(PathUtil.getRealPath());
	}
}

