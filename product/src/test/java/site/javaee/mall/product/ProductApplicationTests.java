package site.javaee.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.javaee.mall.product.entity.BrandEntity;
import site.javaee.mall.product.service.BrandService;
import site.javaee.mall.product.service.CategoryService;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Slf4j
class ProductApplicationTests {

    @Autowired
    BrandService brandService;
    @Autowired
    private CategoryService categoryService;


    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("华为");
//        brandService.save(brandEntity);
//        System.out.println("保存成功");

//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("HUAWEI");
//        boolean result = brandService.updateById(brandEntity);
//        System.out.println(result);

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach((item) -> {
            System.out.println(item);
        });

    }


    @Test
    void findCatelogPath(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径：{}", Arrays.toString(catelogPath));
    }

}
