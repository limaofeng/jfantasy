package org.jfantasy.springboot.dao;

import org.jfantasy.springboot.bean.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleJPADao extends JpaRepository<Article,Long> {
}
