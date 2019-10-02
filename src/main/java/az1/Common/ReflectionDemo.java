package az1.Common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionDemo {
    public static class TestClass {
        private Integer a;
        public long b;
        private TestClass() {
            a = 1;
            b = 5;
        }

        public TestClass(Integer a, long b) {
            this.a = a;
            this.b = b;
        }

        public void PrintMessage(String msg) {
            System.out.println("Message from az1.Common.ReflectionDemo$TestClass: " + msg);
        }

        public Integer GetA() {
            return a;
        }

        public long GetB() {
            return b;
        }
    }
    public static void PrintInfo(Class aClass) {
        System.out.println("Info for: " + aClass.getName());
        System.out.println("Constructors:");
        for (Constructor constructor : aClass.getConstructors()) {
            System.out.println(constructor);
        }
        System.out.println("Methods:");
        for (Method method : aClass.getMethods()) {
            System.out.println(method);
        }
        System.out.println("Fields:");
        for (Field field : aClass.getFields()) {
            System.out.println(field);
        }
    }
    static Constructor GetConstructor(String className, Class<?>... paramTypes) throws ClassNotFoundException, NoSuchMethodException {
        Class aClass = Class.forName(className);
        return aClass.getConstructor(paramTypes);
    }
    static Object GetInstance(Constructor constructor, Object... initArgs) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return constructor.newInstance(initArgs);
    }
    static Method GetMethod(Object instance, String methodName, Class<?>... paramTypes) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        return instance.getClass().getMethod(methodName, paramTypes);
    }
    static Object Invoke(Object instance, Method method, Object... initArgs) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        return method.invoke(instance, initArgs);
    }
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        PrintInfo(TestClass.class);
        System.out.println("--------------------------");
        Constructor constructor = GetConstructor("az1.Common.ReflectionDemo$TestClass", Integer.class, long.class);
        Object instance = GetInstance(constructor, 100, 105);
        System.out.println(Invoke(instance, GetMethod(instance, "PrintMessage", String.class), "Hi."));
        System.out.println(Invoke(instance, GetMethod(instance, "GetA")));
        System.out.println(Invoke(instance, GetMethod(instance, "GetB")));
    }
}
