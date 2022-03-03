package com.example.demo;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);

            if (!f.isAccessible()) {
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);
            if (wasPrivate) {
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Item createItem(Long id) {
        Item item = new Item();
        item.setId(id);
        item.setPrice(BigDecimal.valueOf(10));
        item.setName("item" + id);
        item.setDescription("This is the description of item" + id);
        return item;
    }

    public static User createUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("user" + id);
        user.setPassword("userpw" + id);
        return user;
    }
}
