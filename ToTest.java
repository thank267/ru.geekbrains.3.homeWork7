import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ToTest {

    public static void main(String[] args) 
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Start(new Tested());

    }

    public static void Start(Tested toTest) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Method[] declearedMethods = toTest.getClass().getDeclaredMethods();

        Map<String, Integer> suiteMap = new HashMap<String, Integer>();

        Map<Integer, List<Method>> testMethods = new TreeMap<Integer, List<Method>>();
        
    
        for (Method method : declearedMethods) {

            if (method.isAnnotationPresent(BeforeSuite.class)) {
                Integer count = suiteMap.getOrDefault("BeforeSuite", 0);
                suiteMap.put("BeforeSuite", ++count);
            }

            if (method.isAnnotationPresent(AfterSuite.class)) {
                Integer count = suiteMap.getOrDefault("AfterSuite", 0);
                suiteMap.put("AfterSuite", ++count);
            }

            if (method.isAnnotationPresent(Test.class)) {

                testMethods.computeIfAbsent(method.getAnnotation(Test.class).priority(),
                    k -> new ArrayList<>()
                ).add(method);
               
            }

        }
        
        if (suiteMap.getOrDefault("BeforeSuite",0)!=1)
        {
            throw new RuntimeException("Methods with the annotation BeforeSuite is not equal to one");
        }
        
        if (suiteMap.getOrDefault("AfterSuite", 0) != 1) {
            throw new RuntimeException("Methods with the annotation AfterSuite is not equal to one");
        }

        for (Method method : declearedMethods) {

            if (method.isAnnotationPresent(BeforeSuite.class)) {

                method.invoke(toTest);

            }

        }

        for (List<Method> methods : testMethods.values()) {
            
            for (Method method : methods) {
    
                    method.invoke(toTest);

            }
           
        }
        
        for (Method method : declearedMethods) {

            if (method.isAnnotationPresent(AfterSuite.class)) {

                method.invoke(toTest);

            }

        }

    }

}

class Tested {

    @AfterSuite
    void afterSuite() {
        System.out.println("afterSuite method");
    }

    @Test
    void test00() {
        System.out.println("test00 method");
    }

    @Test
    void test0() {
        System.out.println("test0 method");
    }

    @Test(priority = 10)
    void test10() {
        System.out.println("test10 method");
    }

    @Test(priority = 5)
    void test5() {
        System.out.println("test5 method");
    }

    @Test(priority = 5)
    void test55() {
        System.out.println("test55 method");
    }

    @Test(priority = 1)
    void test1() {
        System.out.println("test1 method");
    }

    @Test(priority = 3)
    void test3() {
        System.out.println("test3 method");
    }
   
    void testEmpty() {
        System.out.println("testEmpty method");
    }

    @BeforeSuite
    void beforeSuite() {
        System.out.println("beforeSuite method");
    }


}
