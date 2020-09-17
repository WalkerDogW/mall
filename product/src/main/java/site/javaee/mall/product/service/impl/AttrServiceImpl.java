package site.javaee.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import site.javaee.mall.common.constant.ProductConstant;
import site.javaee.mall.common.utils.PageUtils;

import site.javaee.mall.common.utils.Query;
import site.javaee.mall.product.dao.AttrAttrgroupRelationDao;
import site.javaee.mall.product.dao.AttrDao;
import site.javaee.mall.product.entity.AttrAttrgroupRelationEntity;
import site.javaee.mall.product.entity.AttrEntity;
import site.javaee.mall.product.entity.AttrGroupEntity;
import site.javaee.mall.product.entity.CategoryEntity;
import site.javaee.mall.product.service.AttrAttrgroupRelationService;
import site.javaee.mall.product.service.AttrGroupService;
import site.javaee.mall.product.service.AttrService;
import site.javaee.mall.product.service.CategoryService;
import site.javaee.mall.product.vo.AttrGroupRelationVo;
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
@Autowired
private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryBaseAttr(Map<String, Object> params, Long catelogId, String attrType) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_type", "base".equalsIgnoreCase(attrType) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
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
            if (attrId != null && attrId.getAttrGroupId() != null) {
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

        //保存关联关系(销售属性不用关联分组)
        if (attrVo.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity attrAndGroup = new AttrAttrgroupRelationEntity();
            attrAndGroup.setAttrId(attrEntity.getAttrId());
            attrAndGroup.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationService.save(attrAndGroup);
        }
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        //所属分组信息,
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            Long attrGroupId = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId)).getAttrGroupId();
            attrRespVo.setAttrGroupId(attrGroupId);
            AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrGroupId);
            if (attrGroupEntity != null) {
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }

        //所属分类信息
        Long catelogId = attrRespVo.getCatelogId();
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        if (categoryEntity != null) {
            attrRespVo.setCatelogName(categoryEntity.getName());
        }

        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        attrRespVo.setCatelogPath(catelogPath);


        return attrRespVo;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.updateById(attrEntity);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //修改分组关联
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationEntity.setAttrId(attrVo.getAttrId());
            UpdateWrapper<AttrAttrgroupRelationEntity> updateWrapper = new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrVo.getAttrId());

            attrAttrgroupRelationService.update(relationEntity, updateWrapper);
        }

    }


    /**
     * 根据分组id查找关联的所有基本属性
     * @param attrGroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        List<AttrEntity> attrEntities=null;

        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_group_id",attrGroupId);
        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationService.list(queryWrapper);
        List<Long> attrIds = relationEntities.stream().map((relationEntitie) -> {
            return relationEntitie.getAttrId();
        }).collect(Collectors.toList());
        if(attrIds == null || attrIds.size()!=0){
             attrEntities = this.listByIds(attrIds);
        }

        return attrEntities;
    }

    /**
     * 获取当前分组没有关联的属性
     * @param params
     * @param attrGroupId
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrGroupId) {
        //1、当前分组只能关联自己所属的分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //2、当前分组只能关联别的分组没有引用的绑定
        //2.1、当前分类下的其他分组
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id",catelogId).ne("attr_groupId",attrGroupId);
        List<AttrGroupEntity> attrGroups = attrGroupService.list(queryWrapper);
        //2.2、这些分组关联的属性
        List<Long> attrGroupIds = attrGroups.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.in("attr_group_id",attrGroupIds);
        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationService.list(queryWrapper2);
        //2.3、从当前分类的所有属性中移除这些属性
        List<Long> attrIds = relationEntities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        QueryWrapper<AttrEntity> queryWrapper3 = new QueryWrapper<>();
        queryWrapper.eq("catelog_id",catelogId).notIn("attr_id",attrIds);
//        List<AttrEntity> attrEntities = this.list(queryWrapper3);
        String key = (String)params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper3.and((wrapper)->{
                wrapper.eq("attr_id",key).like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper3);
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }

    @Override
    public void deleteRelation(List<AttrGroupRelationVo> attrGroupRelationVos) {
        UpdateWrapper<AttrAttrgroupRelationEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("attr_id",1L).eq("attr_group_id",1L);

        List<AttrAttrgroupRelationEntity> relationEntities = attrGroupRelationVos.stream().map((vo) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(vo, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        attrAttrgroupRelationDao.deleteBatchRelation(relationEntities);
    }
}