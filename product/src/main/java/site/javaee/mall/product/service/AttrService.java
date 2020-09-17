package site.javaee.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.javaee.mall.common.utils.PageUtils;
import site.javaee.mall.product.entity.AttrEntity;
import site.javaee.mall.product.vo.AttrGroupRelationVo;
import site.javaee.mall.product.vo.AttrRespVo;
import site.javaee.mall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author WalkerDogW
 * @email WalkerDogW@gmail.com
 * @date 2020-07-31 11:00:50
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attrVo);

    PageUtils queryBaseAttr(Map<String, Object> params, Long catelogId, String attrType);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attrVo);

    List<AttrEntity> getRelationAttr(Long attrGroupId);

    void deleteRelation(List<AttrGroupRelationVo> attrGroupRelationVos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrGroupId);
}

