package site.javaee.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.javaee.mall.common.utils.PageUtils;
import site.javaee.mall.product.entity.BrandEntity;
import site.javaee.mall.product.entity.CategoryBrandRelationEntity;
import site.javaee.mall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author WalkerDogW
 * @email WalkerDogW@gmail.com
 * @date 2020-07-31 11:00:50
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrand(BrandEntity brandEntity);

    void updateCategory(CategoryEntity category);

    List<BrandEntity> getBrandsByCatId(Long catId);
}

