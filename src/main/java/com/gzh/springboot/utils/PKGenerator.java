package com.gzh.springboot.utils;

import org.apache.log4j.Logger;

/**
 * 主键生成器。该类使用单例模式。
 */
public class PKGenerator {

	
	protected static Logger log = Logger.getLogger(PKGenerator.class.getName());
	/**
	 * 存储主机IP左移48位后的值。
	 */
	private long key;
	/**
	 * 静态不变单例变量。
	 */
	private static final PKGenerator instance = new PKGenerator();

	/**
	 * 获得当前主机IP的后3位（如192.168.0.112中的112），左移48位后存入变量lastTransIP中 。
	 * 
	 * @roseuid 3EF6A8F0023D
	 */
	private PKGenerator() {
		makeKey(0);
		//makeKey(localHostIP());
	}
	
	/*private static int localHostIP(){
		try {
			InetAddress address  = InetAddress.getLocalHost();
			String strIP = address.getHostAddress();
			int lastPoint = strIP.lastIndexOf(".");
			return Integer.parseInt(strIP.substring(lastPoint + 1));
		} catch (UnknownHostException e) {
			System.out.println("No Host");
		}
		return 0;
	}*/
	/**
	 * 生成key
	 * 
	 * @param seed
	 */
	public synchronized void makeKey(int seed) {
		long lastTransIP = ((long) seed) << 48;
		long longTime = System.currentTimeMillis();
		key = longTime | lastTransIP;
	}

	/**
	 * @return com.adtech.util.PKGenerator
	 * @roseuid 3EF6A8F00251
	 */
	public static PKGenerator getInstance() {
		return instance;
	}

	/**
	 * 返回唯一的键。获得当前时间的毫秒数位与lastTransIP的值。
	 * 
	 * @return long
	 * @roseuid 3EF6A8F00265
	 */
	public synchronized long getKey() {
		return ++key;
	}

	public static long generateKey() {
		return instance.getKey();
	}
	public static final long guestKeyMask = 1L << 47;
	public static final long tempGroupKeyMask = guestKeyMask;

	// 产生访客id
	public static long generateGuestKey() {
		return instance.getKey() | guestKeyMask;
	}
}
