package com.winway.comic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 漫画类别对应表，漫画类别多对多关系实体类
 * </p>
 *
 * @author sam.feng
 * @since 2020-06-15
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("cartoon_type")
public class CartoonType implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 漫画id
     */
    private Integer cartoonId;
    /**
     * 类别id
     */
    private Integer typeId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
