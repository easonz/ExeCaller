package org.eason.execaller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	private static Logger logger = LoggerFactory.getLogger(Main.class);
	private ApplicationContext app;
	private PQDIFGenerator generater;

	public void init() {
		logger.info("spring init start : {}", new Date().toString());
		app = new ClassPathXmlApplicationContext("applicationContext.xml");
		logger.info("spring init end : {}", new Date().toString());
	}

	public void start() {
		init();
		generater = app.getBean(PQDIFGenerator.class);
		generater.start();
		// 检测pqdif-generate-cfg.ini，如果文件被修改，则重启应用
	}

	public static void main(String[] args) {
		logger.info("Main class start!");
		new Main().start();
	}
}
