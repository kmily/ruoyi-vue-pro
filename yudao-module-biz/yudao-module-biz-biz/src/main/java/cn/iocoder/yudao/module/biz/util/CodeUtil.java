package cn.iocoder.yudao.module.biz.util;

import java.util.UUID;

public class CodeUtil {
	
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString().replaceAll("-", "");
		return uuidStr;
	}


}
