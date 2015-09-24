package com.fantasy.framework.util.json;

import java.util.Date;
import java.util.Set;

import org.dc.bean.Article;
import org.dc.bean.User;
import org.dc.mixin.MixInUser;

import com.google.common.collect.Sets;

public class Test {
	public static void main(String args[]) {
		
		User user = new User();
		user.setName("chris");
		user.setCreateDate(new Date());
		
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
	}
}
