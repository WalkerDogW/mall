package site.javaee.mall.product.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.javaee.mall.product.entity.AttrEntity;
import site.javaee.mall.product.service.AttrService;
import site.javaee.mall.common.utils.PageUtils;
import site.javaee.mall.common.utils.R;
import site.javaee.mall.product.vo.AttrRespVo;
import site.javaee.mall.product.vo.AttrVo;


/**
 * 商品属性
 *
 * @author WalkerDogW
 * @email WalkerDogW@gmail.com
 * @date 2020-07-31 11:00:50
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/{attrType}/list/{catelogId}")
    public R catelogList(@RequestParam Map<String, Object> params,
                         @PathVariable("catelogId") Long catelogId,
                         @PathVariable("attrType") String attrType) {
        PageUtils page = attrService.queryBaseAttr(params, catelogId,attrType);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId) {
//        AttrEntity attr = attrService.getById(attrId);
        AttrRespVo attrRespVo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
//    @RequestMapping("/save")
//    //@RequiresPermissions("product:attr:save")
//    public R save(@RequestBody AttrEntity attr) {
//        attrService.save(attr);
//
//        return R.ok();
//    }
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:update")
    public R save(@RequestBody AttrVo attrVo) {
        attrService.saveAttr(attrVo);

        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
//    public R update(@RequestBody AttrEntity attr) {
    public R update(@RequestBody AttrVo attrVo) {
//        attrService.updateById(attrVo);
        attrService.updateAttr(attrVo);
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
