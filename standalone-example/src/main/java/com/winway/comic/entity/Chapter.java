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
 * 漫画章节表实体类
 * </p>
 *
 * @author sam.feng
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("chapter")
public class Chapter implements Serializable {

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
     * 章节数
     */
    private Integer num;
    /**
     * 章节名称
     */
    private String name;
    /**
     * 封面，图片英语逗号,隔开,按顺序播放
     */
    private String content;


}
