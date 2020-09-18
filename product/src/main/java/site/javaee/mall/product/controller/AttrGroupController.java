package site.javaee.mall.product.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.javaee.mall.product.entity.AttrAttrgroupRelationEntity;
import site.javaee.mall.product.entity.AttrEntity;
import site.javaee.mall.product.entity.AttrGroupEntity;
import site.javaee.mall.product.service.AttrAttrgroupRelationService;
import site.javaee.mall.product.service.AttrGroupService;
import site.javaee.mall.common.utils.PageUtils;
import site.javaee.mall.common.utils.R;
import site.javaee.mall.product.service.AttrService;
import site.javaee.mall.product.service.CategoryService;
import site.javaee.mall.product.vo.AttrGroupRelationVo;
import site.javaee.mall.product.vo.AttrGroupWithAttrsVo;


/**
 * 属性分组
 *
 * @author WalkerDogW
 * @email WalkerDogW@gmail.com
 * @date 2020-07-31 11:00:50
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 属性分组关联着的属性
     */
    @RequestMapping("/{attrGroupId}/attr/relation")
    //@RequiresPermissions("product:attrgroup:list")
    public R attrRelationList(@PathVariable("attrGroupId") Long attrGroupId) {
        List<AttrEntity> attrEntities = attrService.getRelationAttr(attrGroupId);
        return R.ok().put("data", attrEntities);
    }

    /**
     * 查找分组可以关联但还没有关联的属性
     */
    @RequestMapping("/{attrGroupId}/noattr/relation")
    //@RequiresPermissions("product:attrgroup:list")
    public R attrNoRelationList(@PathVariable("attrGroupId") Long attrGroupId,
                                @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getNoRelationAttr(params, attrGroupId);
        return R.ok().put("page", page);
    }

    /**
     * 删除关联关系
     */
    @RequestMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody List<AttrGroupRelationVo> attrGroupRelationVos) {
//        attrService.removeByIds();
        attrService.deleteRelation(attrGroupRelationVos);
        return R.ok();
    }


    /**
     * 新增关联关系
     */
    @RequestMapping("/attr/relation")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody List<AttrGroupRelationVo> attrGroupRelationVos) {

        attrAttrgroupRelationService.saveBatch(attrGroupRelationVos);
        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrGroupService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 分类列表
     */
    @RequestMapping("/list/{category}")
    //@RequiresPermissions("product:attr:list")
    public R categoryList(@RequestParam Map<String, Object> params, @PathVariable Long category) {
//        PageUtils page = attrService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params, category);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] CatelogPath = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(CatelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }


    /**
     * 获取分类下所有分组及其关联的属性
     */
    @RequestMapping("{catelogId}/withattr")
    //@RequiresPermissions("product:attrgroup:delete")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId" )Long catelogId) {
        //1、查出当前分类下的所有分组
        //2、查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos =  attrGroupService.getAttrGroupWithAttrsBycatelogId(catelogId);
        return R.ok().put("data",attrGroupWithAttrsVos);
    }



}
