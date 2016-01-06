package org.jfantasy.framework.util.json;

import org.jfantasy.framework.util.json.bean.User;

import java.util.Date;

public class Test {

	@org.junit.Test
	public void run() {
		
		User user = new User();
		user.setName("chris");
		user.setCreateDate(new Date());

		/*
		Article article = new Article();
		article.setTitle("title");
		article.setUser(user);
		
		Set<Article> articles = Sets.newHashSet(article);
		user.setArticles(articles);
		
		String userJson = Jacksons.me().readAsString(user);
		String articleJson = Jacksons.me().readAsString(article);
		
		System.out.println(userJson);
		System.out.println(articleJson);
		
		String mixInUser = Jacksons.me().addMixInAnnotations(User.class, MixInUser.class).readAsString(user);
		System.out.println(mixInUser);
		
		// 需要在user PO上加 @JsonFilter("myFilter")注解，并且注释掉上面的代码, 
		// 要不然上面的代码会报没有给"myFilter"提供filterProvider 错误
		String filterUser = Jacksons.me().filter("myFilter", "name").readAsString(user);
		System.out.println(filterUser);
		*/
	}
}
