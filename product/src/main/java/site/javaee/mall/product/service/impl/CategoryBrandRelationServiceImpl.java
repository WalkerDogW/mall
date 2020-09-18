package site.javaee.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.javaee.mall.common.utils.PageUtils;

import site.javaee.mall.product.dao.BrandDao;
import site.javaee.mall.product.dao.CategoryBrandRelationDao;
import site.javaee.mall.product.dao.CategoryDao;
import site.javaee.mall.product.entity.BrandEntity;
import site.javaee.mall.product.entity.CategoryBrandRelationEntity;
import site.javaee.mall.product.entity.CategoryEntity;
import site.javaee.mall.product.service.BrandService;
import site.javaee.mall.product.service.CategoryBrandRelationService;
import site.javaee.mall.common.utils.Query;

@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private BrandService brandService;

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        //查询详细名字
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateCategory(CategoryEntity category) {
        this.baseMapper.updateCategory(category);
    }

    @Override
    public void updateBrand(BrandEntity brandEntity) {
        CategoryBrandRelationEntity categoryBrand = new CategoryBrandRelationEntity();
        categoryBrand.setBrandId(brandEntity.getBrandId());
        categoryBrand.setBrandName(brandEntity.getName());

        UpdateWrapper<CategoryBrandRelationEntity> categoryBrandUpdateWrapper = new UpdateWrapper<>();
        categoryBrandUpdateWrapper.eq("brand_id",brandEntity.getBrandId());

        this.update(categoryBrand,categoryBrandUpdateWrapper);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {
        QueryWrapper<CategoryBrandRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id",catId);
        List<CategoryBrandRelationEntity> relationEntities = this.list(queryWrapper);
        List<BrandEntity> brandEntities = relationEntities.stream().map((item) -> {
            Long brandId = item.getBrandId();
            BrandEntity brandEntity = brandService.getById(brandId);
            return brandEntity;
        }).collect(Collectors.toList());
        return brandEntities;
    }
}