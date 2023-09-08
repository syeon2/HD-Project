package personal.hdproject.util.generator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeGenerator {
	public static Date getCurrentTime() {
		return Date.from(
			LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date getMinuteInFuture(Integer minute) {
		return Date.from(
			LocalDateTime.now().plusMinutes(minute).atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date getDayInFuture(Integer day) {
		return Date.from(
			LocalDateTime.now().plusDays(day).atZone(ZoneId.systemDefault()).toInstant());
	}
}
