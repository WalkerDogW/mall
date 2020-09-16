package site.javaee.mall.product.service.impl;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.transaction.annotation.Transactional;
import site.javaee.mall.common.utils.PageUtils;

import site.javaee.mall.common.utils.Query;
import site.javaee.mall.product.dao.AttrDao;
import site.javaee.mall.product.entity.AttrAttrgroupRelationEntity;
import site.javaee.mall.product.entity.AttrEntity;
import site.javaee.mall.product.entity.AttrGroupEntity;
import site.javaee.mall.product.entity.CategoryEntity;
import site.javaee.mall.product.service.AttrAttrgroupRelationService;
import site.javaee.mall.product.service.AttrGroupService;
import site.javaee.mall.product.service.AttrService;
import site.javaee.mall.product.service.CategoryService;
import site.javaee.mall.product.vo.AttrRespVo;
import site.javaee.mall.product.vo.AttrVo;

@Slf4j
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrGroupService attrGroupService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryBaseAttr(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        //如果分类id不为0则返回指定分类
        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");

        //如果有关键字查询，则进行模糊查询
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((wrapper) -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        PageUtils pageUtils = new PageUtils(page);

        //从查询出来的记录中封装返回对象
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVoList = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //查询分类名字和属性分组名字
            String categoryName = categoryService.getById(attrRespVo.getCatelogId()).getName();
            attrRespVo.setCatelogName(categoryName);
            AttrAttrgroupRelationEntity attrId = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrRespVo.getAttrId()));
            if (attrId != null) {
                Long attrGroupId = attrId.getAttrGroupId();
                String attrGroupName = attrGroupService.getById(attrGroupId).getAttrGroupName();
                attrRespVo.setGroupName(attrGroupName);
            }
            return attrRespVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVoList);
        return pageUtils;
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attrVo) {
        //保存基本数据
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.save(attrEntity);

        //保存关联关系
        AttrAttrgroupRelationEntity attrAndGroup = new AttrAttrgroupRelationEntity();
        attrAndGroup.setAttrId(attrEntity.getAttrId());
        attrAndGroup.setAttrGroupId(attrVo.getAttrGroupId());
        attrAttrgroupRelationService.save(attrAndGroup);
    }
}