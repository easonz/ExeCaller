package org.eason.utils;

import java.io.File;
import java.net.URL;

public class RescourcesUtils {
	private static String projectPath = null;
	private static String rescourcesPath = null;
	private static String currentPath = null;

	static {
		URL resUrl = Thread.currentThread().getContextClassLoader().getResource(".");
		if (resUrl != null) {
			rescourcesPath = resUrl.getPath();
			currentPath = rescourcesPath;
		} else {
			rescourcesPath = System.getProperty("user.dir");
			currentPath = System.getProperty("user.dir");
		}
		if (currentPath.contains("WEB-INF")) {

			projectPath = Thread.currentThread().getContextClassLoader()
					.getResource("../../").getPath();
		} else {

			projectPath = System.getProperty("user.dir");
		}
	}

	public static File getFile(String relativeFilePath) {
		return new File(rescourcesPath + File.separator + relativeFilePath);
	}

	/**
	 * @param projectPath the projectPath to set
	 */
	public static void setProjectPath(String projectPath) {
		RescourcesUtils.projectPath = projectPath;
	}

	/**
	 * @return the projectPath
	 */
	public static String getProjectPath() {
		return projectPath;
	}

	/**
	 * @param rescourcesPath1 the rescourcesPath1 to set
	 */
	public static void setRescourcesPath(String rescourcesPath) {
		RescourcesUtils.rescourcesPath = rescourcesPath;
	}

	/**
	 * @return the rescourcesPath1
	 */
	public static String getRescourcesPath() {
		return rescourcesPath;
	}

}
