package site.javaee.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.javaee.mall.product.entity.BrandEntity;
import site.javaee.mall.product.service.BrandService;

import java.util.List;

@SpringBootTest
class ProductApplicationTests {

    @Autowired
    BrandService brandService;



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

}
