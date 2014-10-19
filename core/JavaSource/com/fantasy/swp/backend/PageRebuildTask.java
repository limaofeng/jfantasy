package com.fantasy.swp.backend;

import com.fantasy.swp.WebPage;

/**
 * 重新生成页面
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-28 下午11:46:35
 * @version 1.0
 */
public class PageRebuildTask implements Runnable {

	private WebPage page;

	public PageRebuildTask(WebPage page) {
		this.page = page;
	}

	public void run() {
		this.page.process();
	}

}
