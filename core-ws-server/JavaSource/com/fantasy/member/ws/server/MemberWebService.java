package com.fantasy.member.ws.server;

import com.fantasy.common.service.AreaService;
import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.framework.util.common.ImageUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.member.bean.Member;
import com.fantasy.member.bean.MemberDetails;
import com.fantasy.member.service.MemberService;
import com.fantasy.member.ws.IMemberService;
import com.fantasy.member.ws.dto.MemberDTO;
import com.fantasy.member.ws.dto.MemberDetailsDTO;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.bean.enums.Sex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class MemberWebService implements IMemberService {

    private final static Log logger = LogFactory.getLog(MemberWebService.class);

    @Resource
    private MemberService memberService;

    @Resource
    private AreaService areaService;//地区信息

    @Resource
    private transient FileUploadService fileUploadService;

    @Override
    public MemberDTO register(MemberDTO member) {
        return toMemberDTO(memberService.register(toMember(member)));
    }

    @Override
    public MemberDTO findUniqueByUsername(String username) {
        return toMemberDTO(memberService.findUniqueByUsername(username));
    }

    @Override
    public boolean isPasswordValid(String encPass, String rawPass) {
        PasswordEncoder encoder = SpringSecurityUtils.getPasswordEncoder();
        return encoder.isPasswordValid(encPass, rawPass, null);
    }

    @Override
    public void login(String username) {
        memberService.login(memberService.findUniqueByUsername(username));
    }

    @Override
    public MemberDTO update(MemberDTO memberDTO) {
        Member member = toMember(memberDTO);
        this.memberService.save(member);
        return toMemberDTO(member);
    }

    private MemberDTO toMemberDTO(Member member){
        //memberDTO 对象
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setUsername(member.getUsername());
        memberDTO.setPassword(member.getPassword());
        memberDTO.setNickName(member.getNickName());
        memberDTO.setEnabled(member.isEnabled());
        memberDTO.setAccountNonExpired(member.isAccountNonExpired());
        memberDTO.setAccountNonLocked(member.isAccountNonLocked());
        memberDTO.setLockTime(member.getLockTime());
        memberDTO.setLastLoginTime(member.getLastLoginTime());
        //详细
        MemberDetailsDTO detailsDTO = new MemberDetailsDTO();
        MemberDetails details = member.getDetails();
        detailsDTO.setMemberId(details.getMemberId());
        detailsDTO.setName(details.getName());
        detailsDTO.setSex(details.getSex() == null ? null : details.getSex().toString());
        detailsDTO.setBirthday(details.getBirthday());
        detailsDTO.setMobile(details.getMobile());
        detailsDTO.setTel(details.getTel());
        detailsDTO.setEmail(details.getEmail());
        detailsDTO.setMailValid(details.getMailValid());
        detailsDTO.setMobileValid(details.getMobileValid());
        detailsDTO.setWebsite(details.getWebsite());
        detailsDTO.setDescription(details.getDescription());
        detailsDTO.setVip(details.getVip());
        detailsDTO.setScore(details.getScore());
        //会员头像
        FileDetail fileDetail =  details.getAvatar();
        if(fileDetail!=null){
           detailsDTO.setAvatar(fileDetail.getAbsolutePath());
        }
        memberDTO.setDetails(detailsDTO);
        return memberDTO;
    }

    private Member toMember(MemberDTO memberDTO){
        //memberDTO 对象
        Member member = this.memberService.findUniqueByUsername(memberDTO.getUsername());
        if(member==null){
            member = new Member();
            member.setUsername(memberDTO.getUsername());
        }
        member.setPassword(memberDTO.getPassword());
        member.setNickName(memberDTO.getNickName());
        member.setEnabled(memberDTO.isEnabled());
        member.setAccountNonExpired(memberDTO.isAccountNonExpired());
        member.setAccountNonLocked(memberDTO.isAccountNonLocked());
        member.setLockTime(memberDTO.getLockTime());
        member.setLastLoginTime(memberDTO.getLastLoginTime());
        //详细
        MemberDetailsDTO detailsDTO = memberDTO.getDetails();
        MemberDetails details =member.getDetails();
        if(details==null){
            details = new MemberDetails();
            member.setDetails(details);
        }
        details.setName(detailsDTO.getName());
        if("female".equals(detailsDTO.getSex())){
            details.setSex(Sex.female);//女
        }else{
            details.setSex(Sex.male);//男
        }
        details.setBirthday(detailsDTO.getBirthday());
        details.setMobile(detailsDTO.getMobile());
        details.setTel(detailsDTO.getTel());
        details.setEmail(detailsDTO.getEmail());
        details.setMailValid(detailsDTO.getMailValid());
        details.setMobileValid(detailsDTO.getMobileValid());
        details.setWebsite(detailsDTO.getWebsite());
        details.setDescription(detailsDTO.getDescription());
        details.setVip(detailsDTO.getVip());
        details.setScore(detailsDTO.getScore());
        //会员头像
        // detailsDTO.getAvatar().startsWith("/") 判断文件是否需要上传。有 ‘/’表示 还是原来的图片。不需要重新生成
        if (StringUtil.isNotBlank(detailsDTO.getAvatar()) && !detailsDTO.getAvatar().startsWith("/")) {
            if (logger.isDebugEnabled()) {
                logger.debug("上传用户头像(base64编码的图片):" + StringUtil.ellipsis(detailsDTO.getAvatar(),20,"..."));
            }
            BufferedImage bufferedImage = ImageUtil.getImage(detailsDTO.getAvatar());
            try {
                File file = FileUtil.tmp();
                ImageUtil.write(bufferedImage, file);
                String mimeType = FileUtil.getMimeType(file);
                String fileName = file.getName() + "." + mimeType.replace("image/", "");
                FileDetail fileDetail = fileUploadService.upload(file, mimeType, fileName, "avatar");
                logger.debug("头像上传成功:" + fileDetail);
                details.setAvatarStore(JSON.serialize(new FileDetail[]{fileDetail}));
                //删除临时文件
                FileUtil.delFile(file);
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return member;
    }

}
