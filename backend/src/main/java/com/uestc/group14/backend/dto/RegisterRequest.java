package com.uestc.group14.backend.dto;




import com.uestc.group14.backend.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 注册请求DTO（社会人士）
 */
@Data
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RegisterRequest extends User {

    /** 确认密码 */
    private String confirmPassword;

    /** 真实姓名 */
    private String realName;

    /** 页码 */
    private Integer pageNo;

    /** 页大小 */
    private Integer pageSize;
}