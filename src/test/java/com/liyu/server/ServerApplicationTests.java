package com.liyu.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

@Slf4j
@RunWith(SpringRunner.class)
//@SpringBootTest
public class ServerApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void md5Test() {
        String str = "123456ccdd";
        String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
        log.info("md5 abc:" + md5);
    }

    @Test
    public void obToJsonTest() {
//        APIResponse aaaa = APIResponse.failed("aaaa");
//        log.info(JSON.toJSONString(aaaa));
    }

}
