package com.example.filedemo;

import com.example.filedemo.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class FileDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileDemoApplication.class, args);

		ScheduledJob job = new ScheduledJob();
		Timer jobScheduler = new Timer();


		jobScheduler.scheduleAtFixedRate(job, 15000, 10000);

		try {
			Thread.sleep(900000);
		} catch(InterruptedException ex) {
			//
		}
		jobScheduler.cancel();



	}
}
class ScheduledJob extends TimerTask {

	public void run() {
//        System.out.println(new Date());
		Calendar cal = Calendar.getInstance();
		long todayMil = cal.getTimeInMillis();     // 현재 시간(밀리 세컨드)
		long oneDayMil = 24 * 60 * 60 * 1000;            // 일 단위

		Calendar fileCal = Calendar.getInstance();
		Date fileDate = null;

		String filePath = "C:\\shared";

		File path = new File(filePath);

		File[] list = path.listFiles();            // 파일 리스트 가져오기


		for (int j = 0; j < list.length; j++) {


			// 파일의 마지막 수정시간 가져오기
			fileDate = new Date(list[j].lastModified());
			System.out.println(fileDate);

			// 현재시간과 파일 수정시간 시간차 계산(단위 : 밀리 세컨드)
			fileCal.setTime(fileDate);
			long diffMil = todayMil - fileCal.getTimeInMillis();

			//날짜로 계산
			int diffDay = (int) (diffMil / oneDayMil);


			// 1일이 지난 파일 삭제
			if (diffMil > 10 && list[j].exists()) {
				list[j].delete();
				System.out.println(list[j].getName() + " 파일을 삭제했습니다.");
			}
		}
	}
}
