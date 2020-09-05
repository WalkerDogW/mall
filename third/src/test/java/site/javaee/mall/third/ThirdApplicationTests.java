package site.javaee.mall.third;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class ThirdApplicationTests {
    @Autowired
    private OSSClient ossClient;

    @Test
    void saveFile() throws FileNotFoundException {
        // download file to local
//        ossClient.getObject(new GetObjectRequest("mall-file-oss", "test.jpg"), ResourceUtils.getFile("classpath:test.jpg"));
        PutObjectResult putObjectResult = ossClient.putObject("mall-file-oss", "test.jpg", ResourceUtils.getFile("classpath:test.jpg"));

    }

    @Test
    void deleteFile() {
        ossClient.deleteObject("mall-file-oss", "test.jpg");

    }


    @Test
    void testOss() throws FileNotFoundException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI4G75nk9Pozu4DS816zpB";
        String accessKeySecret = "MLsnyQ4eugIDR4MtV63CsZBIVUs52p";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传文件流。
        File file = ResourceUtils.getFile("classpath:test.jpg");
        InputStream inputStream = new FileInputStream(file);
        ossClient.putObject("mall-file-oss", "test.jpg", inputStream);

// 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传成功");
    }

    @Test
    void contextLoads() {
    }

}
