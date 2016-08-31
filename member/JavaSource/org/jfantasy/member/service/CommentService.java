package org.jfantasy.member.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.bean.Comment;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.dao.CommentDao;
import org.jfantasy.member.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private MemberDao memberDao;

    public Comment save(Comment comment) {
        Member member = memberDao.get(comment.getMemberId());
        comment.setUsername(member.getUsername());
        comment.setShow(true);
        return commentDao.save(comment);
    }

    public Pager<Comment> findPager(Pager<Comment> pager, List<PropertyFilter> filters) {
        return this.commentDao.findPager(pager, filters);
    }

    public Comment get(Long id) {
        return this.commentDao.get(id);
    }

    public Comment updateShow(Long id, Boolean show) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setShow(show);
        return this.commentDao.update(comment, true);
    }

    public void deltele(Long... ids) {
        for (Long id : ids) {
            this.commentDao.delete(id);
        }
    }

}
