package com.stopsnearme.app.ws;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * 
@RunWith(SpringRunner.class)
@SpringBootTest
public class MobileAppWsApplicationTests {

	@Test
	public void contextLoads() {
	}

}
 */

@RestController
class TestController {

    @GetMapping("/users")
    public String testRateLimiting() {
        return "Request Successful!";
    }
}
