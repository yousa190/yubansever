package com.yuban.shop;

import com.yuban.shop.pojo.dto.UserDto;
import com.yuban.shop.pojo.entity.UserData;
import com.yuban.shop.pojo.vo.UserVo;
import com.yuban.shop.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testUserDtoAndVo() {
        // 创建测试用户
        UserData userData = new UserData();
        userData.setUsername("testuser");
        userData.setEmail("test@example.com");
        userData.setPhone("12345678901");
        userData.setSex(1);
        userData.setLevel(1);
        userData.setCreatedAt(new Date());
        userData.setUpdatedAt(new Date());
        userData.setLastLogin(new Date());

        // 保存用户
        boolean saveResult = userService.save(userData);
        assertTrue(saveResult);

        // 测试获取用户VO
        UserVo userVo = userService.getUserById(userData.getUserId().longValue());
        assertNotNull(userVo);
        assertEquals(userData.getUsername(), userVo.getUsername());
        assertEquals(userData.getEmail(), userVo.getEmail());

        // 测试更新用户信息
        UserDto userDto = new UserDto();
        userDto.setUserId(userData.getUserId());
        userDto.setUsername("updateduser");
        userDto.setEmail("updated@example.com");
        userDto.setPhone("09876543210");
        userDto.setUpdatedAt(new Date());

        boolean updateResult = userService.updateUserInfo(userDto);
        assertTrue(updateResult);

        // 验证更新结果
        UserData updatedUser = userService.getById(userData.getUserId());
        assertEquals("updateduser", updatedUser.getUsername());
        assertEquals("updated@example.com", updatedUser.getEmail());

        // 测试获取所有用户
        List<UserVo> allUsers = userService.getAllUsers();
        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());
    }
}