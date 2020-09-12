package site.javaee.mall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.javaee.mall.common.utils.PageUtils;

import site.javaee.mall.common.utils.Query;
import site.javaee.mall.product.dao.AttrGroupDao;
import site.javaee.mall.product.entity.AttrGroupEntity;
import site.javaee.mall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long category) {
        if (category == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), new QueryWrapper<AttrGroupEntity>());
            return new PageUtils(page);
        }else{
            String key = (String)params.get("key");
            //select * from pms_attr_group where catelog_id= ? and (attr_group_id = %key% or attr_group_name like %key%)
            QueryWrapper<AttrGroupEntity> attrGroupEntityQueryWrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id",category);
            if(!StringUtils.isEmpty(key)){
                attrGroupEntityQueryWrapper.and((wrapper->{
                    wrapper.like("attr_group_id",key).or().like("attr_group_name",key);
                }));
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), attrGroupEntityQueryWrapper);
            return new PageUtils(page);
        }
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

}