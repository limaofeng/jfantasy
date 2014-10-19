package com.fantasy.swp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.fantasy.file.FileManager;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.swp.data.HibernateData;
import com.fantasy.swp.data.SimpleData;

public class WebPageManager/* implements InitializingBean, ApplicationListener<ApplicationEvent> */{

	private static WebPageManager instance;
	/**
	 * 线程池
	 */
	private ExecutorService executor;
	/**
	 * 线程池大小
	 */
	private int threadPoolSize = 10;
	/**
	 * 定时任务
	 */
	private ScheduledExecutorService scheduler;
	/**
	 * 执行周期
	 */
	private long period = 30000L;

	private boolean reopening = false;
	private boolean rebuilding = false;

//	private Map<String, PageRebuilder> pageRebuilders = new HashMap<String, PageRebuilder>();

	private Map<String, FileManager> fileManagers = new HashMap<String, FileManager>();

	private Map<String, PageWriter> pageWriters = new HashMap<String, PageWriter>();

	@SuppressWarnings("unchecked")
	private Map<String, Class> pageDataClazzs = new HashMap<String, Class>();
	{
		pageDataClazzs.put("simple", SimpleData.class);
		pageDataClazzs.put("hibernate-query", HibernateData.class);
	}

	public void afterPropertiesSet() {
		// if (fileManagers.isEmpty())
		// throw new Exception(" fileManagers is empty");
		// if (pageWriters.isEmpty())
		// throw new Exception(" pageWriters is empty");
	}

	public static WebPageManager getInstance() {
		if (instance == null) {
			if (SpringContextUtil.getApplicationContext() == null) {
				instance = new WebPageManager();
				instance.afterPropertiesSet();
			} else {
				instance = SpringContextUtil.getBeanByType(WebPageManager.class);
			}
		}
		return instance;
	}

	/**
	 * 初始化方法
	 * 
	 * @功能描述
	 */
	public void open() {
		this.executor = Executors.newFixedThreadPool(this.threadPoolSize);
		this.scheduler = Executors.newSingleThreadScheduledExecutor();
		// 定时刷新页面
		// this.scheduler.scheduleWithFixedDelay(new PageRebuilder(),this.period, this.period, TimeUnit.MILLISECONDS);
	}

	/**
	 * 关闭方法
	 * 
	 * @功能描述
	 */
	public void close() {
		if (this.executor != null) {
			this.executor.shutdown();
		}
		if (this.scheduler != null) {
			this.scheduler.shutdown();
		}
	}

	/**
	 * 通过名称刷新指定的模板
	 * 
	 * @功能描述
	 * @param name
	 */
	public void rebuild(String name) {
//		if (!pageRebuilders.containsKey(name)) {
//			throw new RuntimeException(name + " @Page ");
//		}
//		pageRebuilders.get(name).rebuild();
	}

//	private PageData getPageData(Element element) {
//		String dataType = element.attributeValue("type");
//		String key = element.attributeValue("key");
//		if (StringUtil.isBlank(dataType)) {
//			return (PageData) ClassUtil.newInstance(element.attributeValue("class"));
//		}
//		if ("simple".equals(dataType)) {
//			return SimpleData.getData(key, element.getTextTrim());
//		} else if ("hibernate-query".equals(dataType)) {
//			HibernateData data = new HibernateData();
//			data.setKey(key);
//			data.setHql(element.selectSingleNode("//data/param[@name='hql']").getText());
//			data.setResultType(element.selectSingleNode("//data/param[@name='resultType']").getText());
//			return data;
//		} else {
//			System.out.println("不支持的类型" + dataType);
//			return null;
//		}
//	}

//	public void onApplicationEvent(ApplicationEvent event) {
//		// 加载配置文件
//		Document document = Dom4jUtil.reader(WebPageManager.class.getResource("/webpage.xml"));
//		Element root = document.getRootElement();
//		// 添加公共数据
//		DataMap globalDatas = new DataMap();
//
//		List<Element> _globalDatas = root.selectNodes("/web-pages/global-datas/data");
//
//		for (Element element : _globalDatas) {
//			globalDatas.add(getPageData(element));
//		}
//		// 添加页面配置
//		List<Element> pages = root.selectNodes("/web-pages/page");
//
//		for (Element element : pages) {
//			WebPage page = new WebPage();
//			page.setGlobalDatas(globalDatas);
//			page.setName(element.attributeValue("name"));
//			page.setTemplate(element.attributeValue("template"));
//			page.setFreemarkerManager(null);
//			// 添加页面数据
//			List<Element> datas = element.selectNodes("//page/datas/data");
//			for (Element data : datas) {
//				page.addData(getPageData(data));
//			}
//			// 添加页面数据
//			page.setPageWriter(createWriter((Element) element.selectSingleNode("//page/writer")));
//			// 添加绑定
//			System.out.println("-----------------------------");
//			List<Element> binds = element.selectNodes("//page/binds/bind");
//			for (Element bind : binds) {
//				System.out.println("绑定类型:" + bind.attributeValue("type") + "\t绑定对象类型" + bind.getName());
//			}
//
//		}
//	}

//	private PageWriter createWriter(Element element) {
//		String type = element.attributeValue("type");
//		PageWriter writer = null;
//		if ("simple".equals(type)) {
//			writer = new SimpleWriter();
//			writer.setFileManager(null);
//			writer.setPageUrl(createPageUrl((Element) element.selectSingleNode("//writer/param[@name='url']/url")));
//		}
//
//		System.out.println("文件生成器:" + element.attributeValue("creator"));
//		System.out.println("文件理器:" + element.attributeValue("fileManager"));
//
//		List<Element> params = element.selectNodes("//writer/param");
//		for (Element param : params) {
//			System.out.println("属性:{name:" + param.attributeValue("name") + ",text:" + param.getText() + "}");
//		}
//		return null;
//	}

//	private PageUrl createPageUrl(Element element) {
//		String type = element.attributeValue("type");
//		if ("simple".equals(type)) {
//			return new SimplePageUrl(element.getTextTrim());
//		}
//		return null;
//	}

	public ExecutorService getExecutor() {
		return this.executor;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public void setIndexReopenPeriod(long period) {
		this.period = period;
	}

	public boolean isReopening() {
		return this.reopening;
	}

	public void setReopening(boolean reopening) {
		this.reopening = reopening;
	}

	public boolean isRebuilding() {
		return this.rebuilding;
	}

	public void setRebuilding(boolean rebuilding) {
		this.rebuilding = rebuilding;
	}

	public static void main(String[] args) {
		
		
		
		
		
	}

}
