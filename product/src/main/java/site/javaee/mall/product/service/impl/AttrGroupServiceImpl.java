package site.javaee.mall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.javaee.mall.common.utils.PageUtils;

import site.javaee.mall.common.utils.Query;
import site.javaee.mall.product.dao.AttrGroupDao;
import site.javaee.mall.product.entity.AttrEntity;
import site.javaee.mall.product.entity.AttrGroupEntity;
import site.javaee.mall.product.service.AttrAttrgroupRelationService;
import site.javaee.mall.product.service.AttrGroupService;
import site.javaee.mall.product.service.AttrService;
import site.javaee.mall.product.vo.AttrGroupWithAttrsVo;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {


    @Autowired
    private  AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long category) {
        String key = (String) params.get("key");
        //select * from pms_attr_group where catelog_id= ? and (attr_group_id = %key% or attr_group_name like %key%)
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<AttrGroupEntity>();
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((wrapper -> {
                wrapper.like("attr_group_id", key).or().like("attr_group_name", key);
            }));
        }
        if (category != 0) {
            queryWrapper.eq("catelog_id", category);
        }
        IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsBycatelogId(Long catelogId) {
        //查询分类的属性分组
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id",catelogId);
        List<AttrGroupEntity> attrGroups = this.list(queryWrapper);
        //根据分组查所有属性
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos = attrGroups.stream().map((attrGroup) -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(attrGroup, attrGroupWithAttrsVo);
            List<AttrEntity> attrEntities = attrService.getRelationAttr(attrGroupWithAttrsVo.getAttrGroupId());
            attrGroupWithAttrsVo.setAttrs(attrEntities);
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());
        return attrGroupWithAttrsVos;
    }
}