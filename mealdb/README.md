# MealDB 

## Unit Testing

- if we create `resources` folder under `src/test/` , then it can be retrieved during unit testing
via following options:

  - Using __classloader__.
  
    ```java
      class Test {
          void test () {
            String resourceName = "example_resource.txt";
             
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(resourceName).getFile());
            String absolutePath = file.getAbsolutePath();
            System.out.println(absolutePath);
            assertTrue(absolutePath.endsWith("/example_resource.txt"));
          }
      }
    ```
  - Using `Paths.get` instroduced in `JAVA 7`
  
    ```java
      class Test {
        void test() {
          Path resourceDirectory = Paths.get("src","test","resources");
          String absolutePath = resourceDirectory.toFile().getAbsolutePath();
          System.out.println(absolutePath);
          Assert.assertTrue(absolutePath.endsWith("src/test/resources"));
        }
      }
    ```
