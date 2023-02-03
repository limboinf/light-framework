package com.limbo.light.web.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.limbo.light.web.dao.enums.DeleteState;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类
 *
 * @author limbo
 * @date 2023/02/03
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public class LightBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID 自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 插入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新状态
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除状态，0：正常，1：逻辑删除
     */
    @TableField(value = "del")
    @TableLogic(value = "0", delval = "1")
    private DeleteState deleteState;

}
