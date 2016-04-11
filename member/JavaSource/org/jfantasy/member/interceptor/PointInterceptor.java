package org.jfantasy.member.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.jfantasy.member.bean.Member;
import org.jfantasy.member.bean.Point;
import org.jfantasy.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Aspect
public class PointInterceptor {

    @Autowired
    private MemberService memberService;

    @After("execution(public * org.jfantasy.member.service.PointService.save(org.jfantasy.member.bean.Point))")
    @Transactional
    public void memberScore(JoinPoint point) {
        Point pointBean = (Point) point.getArgs()[0];
        Member member = pointBean.getMember();
        Integer oldScore = member.getDetails().getScore();
        oldScore = oldScore == null ? 0 : oldScore;
        if (Point.Status.add == pointBean.getStatus()) {
            oldScore += pointBean.getScore();
        } else if (Point.Status.pay == pointBean.getStatus()) {
            oldScore = oldScore - pointBean.getScore();
        }
        member.getDetails().setScore(oldScore);
        memberService.save(member);
    }

}
