package com.fantasy.member.ws.server;

import com.fantasy.common.service.AreaService;
import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.framework.util.common.ImageUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.ws.util.WebServiceUtil;
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
        return toMemberDTO(memberService.register(WebServiceUtil.toBean(member, Member.class)));
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
        MemberDetailsDTO detailsDTO = memberDTO.getDetails();
        Member member = this.memberService.findUniqueByUsername(memberDTO.getUsername());
        //昵称
        member.setNickName(memberDTO.getNickName());
        //会员详细
        MemberDetails details = member.getDetails();
        //姓名
        details.setName(detailsDTO.getName());
        //性别
        if (Sex.female.toString().equals(detailsDTO.getSex())) {
            details.setSex(Sex.female);
        } else {
            details.setSex(Sex.male);
        }
        //生日
        details.setBirthday(detailsDTO.getBirthday());
        //移动电话
        details.setMobile(detailsDTO.getMobile());
        //固定电话
        details.setTel(detailsDTO.getTel());
        //邮箱
        details.setEmail(detailsDTO.getEmail());
        //描述信息
        details.setDescription(detailsDTO.getDescription());
        //保存头像
        if (StringUtil.isNotBlank(detailsDTO.getAvatar()) && !detailsDTO.getAvatar().startsWith("/")) {
            if (logger.isDebugEnabled()) {
                logger.debug("上传用户头像(base64编码的图片):" + detailsDTO.getAvatar());
            }
            BufferedImage bufferedImage = ImageUtil.getImage(detailsDTO.getAvatar());
            try {
                File file = FileUtil.tmp();
                ImageUtil.write(bufferedImage, file);
                String mimeType = FileUtil.getMimeType(file);
                String fileName = file.getName() + "." + mimeType.replace("image/", "");
                FileDetail fileDetail = fileUploadService.upload(file, mimeType, fileName, "avatar");
                logger.debug("头像上传成功:" + fileDetail);
                details.setAvatarStore(JSON.serialize(fileDetail));
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        this.memberService.save(member);
        return toMemberDTO(member);
    }

    private MemberDTO toMemberDTO(Member member){
        return WebServiceUtil.toBean(member, MemberDTO.class);
    }

}
