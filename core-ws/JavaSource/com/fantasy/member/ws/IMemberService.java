package com.fantasy.member.ws;

import com.fantasy.member.ws.dto.MemberDTO;

public interface IMemberService {

    /**
     * 注册
     * 调用方法时 username、password必填. <br/>
     * 为了防止用户名重复。调用前请使用 findUniqueByUsername 方法检查用户名是否存在
     *
     * @param member 前台会员对象
     * @return 注册成功返回对象
     */
    public MemberDTO register(MemberDTO member);

    /**
     * 查询用户对象
     * 通过用户名查询用户对象
     *
     * @param username 用户名
     * @return MemberDTO
     */
    public MemberDTO findUniqueByUsername(String username);

    /**
     * 判断密码是否一致
     * 示例：isPasswordValid('123456','2D13FDSDDDF')
     *
     * @param encPass 原始密码，用户输入密码
     * @param rawPass 数据库保存的密码，可能被编码过
     * @return
     */
    public boolean isPasswordValid(String encPass, String rawPass);

    /**
     * 用户登录后的后续操作
     * <p/>
     * 该方法非用户登录方法，而是登录初始化方法.<br/>
     * 如：用户登录成功后更新最后登录时间。
     *
     * @param username 用户名
     */
    public void login(String username);


    /**
     * 用户修改
     *
     * @param member 会员对象
     * @return MemberDTO
     * 会员修改.<br/>
     */
    public MemberDTO update(MemberDTO member);


}
