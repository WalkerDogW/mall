package site.javaee.mall.product.dao;

import org.apache.ibatis.annotations.Param;
import site.javaee.mall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.javaee.mall.product.entity.CategoryEntity;

/**
 * 品牌分类关联
 * 
 * @author WalkerDogW
 * @email WalkerDogW@gmail.com
 * @date 2020-07-31 11:00:50
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    void updateCategory(@Param("category")CategoryEntity category);
}
