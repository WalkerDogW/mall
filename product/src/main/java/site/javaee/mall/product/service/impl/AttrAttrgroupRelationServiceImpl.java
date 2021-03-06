package site.javaee.mall.product.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.javaee.mall.common.utils.PageUtils;

import site.javaee.mall.common.utils.Query;
import site.javaee.mall.product.dao.AttrAttrgroupRelationDao;
import site.javaee.mall.product.entity.AttrAttrgroupRelationEntity;
import site.javaee.mall.product.service.AttrAttrgroupRelationService;
import site.javaee.mall.product.vo.AttrGroupRelationVo;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveBatch(List<AttrGroupRelationVo> attrGroupRelationVos) {
        List<AttrAttrgroupRelationEntity> relationEntities = new ArrayList<>();
        for (int i = 0; i < attrGroupRelationVos.size(); i++) {
            AttrGroupRelationVo attrGroupRelationVo = attrGroupRelationVos.get(i);
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(attrGroupRelationVo, relationEntity);
            relationEntities.add(relationEntity);
        }

        this.saveBatch(relationEntities);
    }
}