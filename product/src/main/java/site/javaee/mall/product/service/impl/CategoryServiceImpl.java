package site.javaee.mall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import site.javaee.mall.common.utils.PageUtils;

import site.javaee.mall.product.dao.CategoryDao;
import site.javaee.mall.product.entity.CategoryBrandRelationEntity;
import site.javaee.mall.product.entity.CategoryEntity;
import site.javaee.mall.product.service.CategoryBrandRelationService;
import site.javaee.mall.product.service.CategoryService;
import site.javaee.mall.common.utils.Query;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //查出所有分类
//        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.in("cat_id", 1,1433,1434);
//        List<CategoryEntity> categoryEntities = baseMapper.selectList(queryWrapper);
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        //组装成树形结构
        List<CategoryEntity> level1Menus = categoryEntities.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentCid() == 0;
        }).map((menu) -> {
            menu.setChildren(getChildren(menu, categoryEntities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return level1Menus;


    }


    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream().filter((categoryEntity) -> {
//            System.out.println("categoryEntity:"+categoryEntity.getParentCid()+"\t"+categoryEntity);
//            System.out.println("root:"+root.getCatId()+"\t"+root);
//            System.out.println(categoryEntity.getParentCid() == root.getCatId());
            return categoryEntity.getParentCid().longValue() == root.getCatId().longValue();
        }).map((menu) -> {
            menu.setChildren(getChildren(menu, all));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;

    }


    @Override
    public void removeMenuByIds(List<Long> asList) {
//        Todo
//        1、检查当前删除的菜单是否被引用
//        逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    /**
     * 级联更新所有关联的数据
     *
     * @param category
     */
    @Override
    @Transactional
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category);
    }

    /**
     * 找到CatelogId的完整路径
     *
     * @param catelogId
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        findParentPath(catelogId, paths);
        Collections.reverse(paths);
        return paths.toArray(new Long[paths.size()]);
    }

    private void findParentPath(Long catelogId, List<Long> paths) {
        //收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
    }
}