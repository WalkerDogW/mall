package site.javaee.mall.product.vo;

import lombok.Data;
import site.javaee.mall.product.entity.AttrEntity;

@Data
public class AttrVo  extends AttrEntity {
    /**
     * 所属分组
     */
    private Long attrGroupId;




}
