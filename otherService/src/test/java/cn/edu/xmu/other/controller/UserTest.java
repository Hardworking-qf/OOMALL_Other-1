package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.other.OtherServiceApplication;
import cn.edu.xmu.other.controller.OtherController;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author XQChen
 * @version 创建时间：2020/11/30 下午1:31
 */
@SpringBootTest(classes = OtherServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllUserStateTest(){};

    private static final Logger logger = LoggerFactory.getLogger(OtherController.class);

    private String testInput;
    private String expectedOutput;

    public UserTest() {
        try {
            testInput = new String(Files.readAllBytes(Paths.get("src/test/resources/testInput/User.json")));
            expectedOutput = new String(Files.readAllBytes(Paths.get("src/test/resources/expectedOutput/User.json")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 注册用户成功测试
     * @throws Exception
     */
    @Test
    public void signUpUserTest01() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/1");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/1");

        JSONAssert.assertEquals(responseString, expectString, new CustomComparator(JSONCompareMode.LENIENT,
                new Customization("data.id", (o1, o2) -> true),
                new Customization("data.gmtCreate", (o1, o2) -> true)));
    }

    /***
     * 用户名已被注册
     * @throws Exception
     */
    @Test
    public void signUpUserTest02() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/2");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/2");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 手机号已被注册
     * @throws Exception
     */
    @Test
    public void signUpUserTest03() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/3");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/3");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 邮箱已被注册
     * @throws Exception
     */
    @Test
    public void signUpUserTest04() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/4");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/4");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 用户名为空
     * @throws Exception
     */
    @Test
    public void signUpUserTest05() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/5");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/5");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 密码为空
     * @throws Exception
     */
    @Test
    public void signUpUserTest06() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/6");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/6");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 真实姓名为空
     * @throws Exception
     */
    @Test
    public void signUpUserTest07() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/7");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/7");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 手机号为空
     * @throws Exception
     */
    @Test
    public void signUpUserTest08() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/8");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/8");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 手机号格式不正确
     * @throws Exception
     */
    @Test
    public void signUpUserTest09() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/9");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/9");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 邮箱为空
     * @throws Exception
     */
    @Test
    public void signUpUserTest10() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/10");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/10");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 邮箱格式不正确
     * @throws Exception
     */
    @Test
    public void signUpUserTest11() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/11");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/11");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 生日为空
     * @throws Exception
     */
    @Test
    public void signUpUserTest12() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/12");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/12");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 生日格式不正确
     * @throws Exception
     */
    @Test
    public void signUpUserTest13() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/13");

        this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest());
    }

    /***
     * 生日范围错误
     * @throws Exception
     */
    @Test
    public void signUpUserTest14() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/14");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/14");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 性别为空
     * @throws Exception
     */
    @Test
    public void signUpUserTest15() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/15");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/15");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }

    /***
     * 性别不正确
     * @throws Exception
     */
    @Test
    public void signUpUserTest16() throws Exception {
        String input = JacksonUtil.parseSubnodeToString(testInput, "/signUp/16");

        String responseString = this.mvc.perform(post("/other/users")
                .contentType("application/json;charset=UTF-8")
                .content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String expectedString = JacksonUtil.parseSubnodeToString(expectedOutput, "/signUp/16");

        JSONAssert.assertEquals(responseString, expectedString, true);
    }
}
