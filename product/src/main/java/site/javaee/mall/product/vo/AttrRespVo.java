package site.javaee.mall.product.vo;

import lombok.Data;
import site.javaee.mall.product.entity.AttrEntity;

@Data
public class AttrRespVo extends AttrEntity {
    /**
     * 所属分组id
     */
    private Long attrGroupId;

    /**
     * 所属分类名称
     */
    private String catelogName;


    /**
     * 所属分组名称
     */
    private String groupName;

}
