package com.gzh.springboot.domain;

public interface Const {
	public static class Category {
		public static final short D_SLAVE = 1;// 从机类型，主机Id必填
		public static final short D_INDEPENDENT = 2;// 管理中心设备，电话必填
		public static final short D_MANAGEMENT = 3;// 独立类型，其他忽略不填
		public static final short D_WIRED = 4;// 有线设备，必填id
		public static final short D_WIRELESS = 5;// 无线设备，电话必填
	}

	public static class Sip {
		public static final short OPEN = 2;
		public static final short CLOSE = 1;
	}
}
