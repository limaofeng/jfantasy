package org.jfantasy.member.interceptor;

import org.aspectj.lang.annotation.Aspect;
import org.jfantasy.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PointInterceptor {

    @Autowired
    private MemberService memberService;

    /*
    public void memberScore(JoinPoint point) {
        Point pointBean = (Point) point.getArgs()[0];
        Member member = pointBean.getMember();
        Integer oldScore = null;
        oldScore = oldScore == null ? 0 : oldScore;
        if (Point.Status.add == pointBean.getStatus()) {
            oldScore += pointBean.getScore();
        } else if (Point.Status.pay == pointBean.getStatus()) {
            oldScore = oldScore - pointBean.getScore();
        }
        member.getDetails().setScore(oldScore);
        memberService.save(member);
    }*/

}
