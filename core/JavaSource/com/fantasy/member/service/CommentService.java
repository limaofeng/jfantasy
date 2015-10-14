package com.fantasy.member.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.member.bean.Comment;
import com.fantasy.member.dao.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    public Comment save(Comment comment) {
        return commentDao.save(comment);
    }

    public Pager<Comment> findPager(Pager<Comment> pager, List<PropertyFilter> filters) {
        return this.commentDao.findPager(pager, filters);
    }

    public Comment get(Long id) {
        return this.commentDao.get(id);
    }

    public void deltele(Long... ids) {
        for (Long id : ids) {
            this.commentDao.delete(id);
        }
    }

}
