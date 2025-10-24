package com.emiyaoj.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class BeanUtilTest {

    static class Source {
        private String name;
        private int age;
        // getters/setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }

    static class Target {
        private String name;
        private int age;
        // getters/setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }

    @Test
    void copyToList_basic() {
        Source s1 = new Source();
        s1.setName("Alice");
        s1.setAge(20);
        Source s2 = new Source();
        s2.setName("Bob");
        s2.setAge(30);
        List<Source> src = Arrays.asList(s1, s2, null);

        List<Target> targets = BeanUtil.copyToList(src, Target.class);

        Assertions.assertNotNull(targets);
        Assertions.assertEquals(2, targets.size());
        Assertions.assertEquals("Alice", targets.get(0).getName());
        Assertions.assertEquals(20, targets.get(0).getAge());
        Assertions.assertEquals("Bob", targets.get(1).getName());
        Assertions.assertEquals(30, targets.get(1).getAge());
    }

    @Test
    void copyToList_emptyOrNull() {
        Assertions.assertTrue(BeanUtil.copyToList(null, Target.class).isEmpty());
        Assertions.assertTrue(BeanUtil.copyToList(List.of(), Target.class).isEmpty());
    }
}

