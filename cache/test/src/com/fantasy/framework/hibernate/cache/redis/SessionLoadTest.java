package com.fantasy.framework.hibernate.cache.redis;


import com.fantasy.test.bean.Article;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class SessionLoadTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void testLoad(){
        Session session = sessionFactory.openSession();
        try {

            session.beginTransaction();

            Article article = (Article) session.get(Article.class, 18L);

            System.out.println("title=" + article.getTitle());

            session.getTransaction().commit();

        }catch(Exception e) {

            e.printStackTrace();

            session.getTransaction().rollback();

        }finally {

            session.close();

        }

        try {

            session = sessionFactory.openSession();

            session.beginTransaction();

            Article article = (Article) session.get(Article.class, 18L);

            System.out.println("title=" + article.getTitle());


            session.getTransaction().commit();

        }catch(Exception e) {

            e.printStackTrace();

            session.getTransaction().rollback();

        }finally {

            session.close();


        }
    }

}
