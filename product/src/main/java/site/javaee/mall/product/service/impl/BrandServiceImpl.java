package site.javaee.mall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.javaee.mall.common.utils.PageUtils;

import site.javaee.mall.product.dao.BrandDao;
import site.javaee.mall.product.entity.BrandEntity;
import site.javaee.mall.product.service.BrandService;
import site.javaee.mall.common.utils.Query;

@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //获取key
        String key = (String) params.get("key");
        IPage<BrandEntity> page = null;
        if (!StringUtils.isEmpty(key)) {
            QueryWrapper<BrandEntity> brandQueryWrapper = new QueryWrapper<>();
            brandQueryWrapper.eq("brand_id", key).or().like("name", key);

            page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    brandQueryWrapper
            );
        } else {
            page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    new QueryWrapper<BrandEntity>()
            );
        }


        return new PageUtils(page);
    }

}