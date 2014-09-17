
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class Test {
	public static Integer num;

	// private final static Log logger = LogFactory.getLog(MailSendService.class);

	public static int digu(int start, int next, int num) {
		next = start + next;
		start = next - start;
		System.out.println(num + '>' + next);
		return next + num++ < 30 ? digu(start, next, num) : 0;
	}

	public synchronized void print() {
		System.out.println(Thread.currentThread().getId() + "：进入v d v");
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getId() + "：离开");
	}

	@org.junit.Test
	public void main() throws URISyntaxException, IOException, ClassNotFoundException {
//		final Test test = new Test();
//		for (int i = 0; i < 5; i++)
//			new Thread(new Runnable() {
//
//				public void run() {
//					test.print();
//				}
//
//			}).start();
//		String s = RegexpUtil.replace("xxx.do?id=123&name=limaf", "", new RegexpUtil.AbstractReplaceCallBack() {
//
//			@Override
//			public String doReplace(String text, int index, Matcher matcher) {
//				return null;
//			}
//
//		});

		// Stack stack = new Stack();
		// stack.push("d");
		// stack.push("a");
		// stack.push("c");
		// stack.push("b");
		//
		//
		// int stacksize = stack.size();
		// for(int i = 0;i<stacksize;i++){
		//
		// System.out.println(stack.pop());
		// }
		//
		// System.out.println(stack.size());
		//
		//
		//
		// int[] array = { 1, 2, 3, 3, 2, 1, 5 ,1};
		// Map<String,Integer> ss = new HashMap<String, Integer>();
		// List lis = new ArrayList();//存放不重复的值
		// Set set = new HashSet();//判断
		//
		// for(int a : array){
		// if(!ss.containsKey(a+"")){
		// ss.put(a+"", 0);
		// }
		// ss.put(a+"", ss.get(a+"")+1);
		// // if(set.add(a)){
		// // lis.add(a+"");
		// // }else{
		// // lis.remove(a+"");
		// // }
		// }
		// for(Map.Entry<String, Integer> entry : ss.entrySet()){
		// //System.out.println(entry.getKey()+"\t"+entry.getValue());
		// if(entry.getValue() == 1){
		// //System.out.println(entry.getKey());
		// }
		// }

		// System.out.println(Class.forName("com.fantasy.framework.util.asm.WorkLoadObject1").getDeclaredMethods());
		// System.out.println(Class.forName("com.fantasy.framework.util.asm.WorkLoadObject").getDeclaredMethods());

		// int start = 1,next = 1,i=2;
		// int num = 30;
		/*
		 * while(i++<30){ next = start + next; start = next - start; System.out.println(i + ">" + next); }
		 */

		// System.out.println(digu(1,1,0));

		// URL url = new URL("file://C:/workspace/itsm/WebContent/WEB-INF/lib/fantasy.jar");

		// System.out.println(url.toURI());

		// URL url = DefaultEngine.class.getResource("background");
		// System.out.println(url);
		// Iterator<URL> strutsPlugin = ClassLoaderUtil.getResources("background", DefaultEngine.class, false);
		// while (strutsPlugin.hasNext()) {
		// // URL url = strutsPlugin.next();
		// // url.openStream();
		// System.out.println(strutsPlugin.next());
		// }
		//
		//
		// File file = new File("C:/workspace/itsm/WebContent/WEB-INF/lib/fantasy.jar", "/struts-plugin.xml");
		//
		// System.out.println(file.exists());
		// System.out.println(file.isDirectory());
		// System.out.println(file.isFile());
		// System.out.println(file.listFiles());

		/*
		 * logger.debug("asdasdasdasd"); logger.error("error"); System.out.println(logger); System.out.println(logger.isDebugEnabled());
		 *
		 * ExpressionParser parser = new SpelExpressionParser(); Expression exp = parser.parseExpression("name + list.toString()"); // Map<String,Object> context = new HashMap<String, Object>(); TestBean bean = new TestBean(); // bean.setName("name"); List<String> list = new ArrayList<String>(); bean.setArray(new String[]{"1","2","3"}); bean.setList(Arrays.asList(bean.getArray())); System.out.println(exp.getValue(new StandardEvaluationContext(bean),String.class));
		 */
	}

	static class TestBean {

		private String name;
		private String[] array;
		private List<String> list;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String[] getArray() {
			return array;
		}

		public void setArray(String[] array) {
			this.array = array;
		}

		public List<String> getList() {
			return list;
		}

		public void setList(List<String> list) {
			this.list = list;
		}

	}
}
