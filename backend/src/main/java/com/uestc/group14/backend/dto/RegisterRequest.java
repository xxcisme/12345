package com.uestc.group14.backend.dto;




import com.uestc.group14.backend.Entity.UserEntity;
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
public class RegisterRequest extends UserEntity {

    /** 确认密码 */
    private String confirmPassword;

    /** 真实姓名 */
    private String realName;
    /** 学校编号 */
    private String schoolCode;

    /** 班级名称 */
    private String className;

    /** 页码 */
    private Integer pageNo;

    /** 页大小 */
    private Integer pageSize;
}