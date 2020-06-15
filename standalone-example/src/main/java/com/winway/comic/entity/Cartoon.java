package com.winway.comic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 漫画表实体类
 * </p>
 *
 * @author sam.feng
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("cartoon")
public class Cartoon implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 种类
     */
    private String type;
    /**
     * 标题
     */
    private String title;
    /**
     * 封面
     */
    private String cover;


}
