package com.consol.citrus.generate;

import com.consol.citrus.generate.javadsl.SwaggerJavaModelGenerator;
import com.consol.citrus.generate.javadsl.UtilsClassGenerator;
import com.consol.citrus.utils.CleanupUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.Collections;

public class SwaggerJavaModelGeneratorTest {
    private String baseDir = Paths.get(".").toAbsolutePath().normalize().toString();
    private String modelsDir = baseDir + "/src/main/java/com/consol/citrus/models";

    private final CleanupUtils cleanupUtils = new CleanupUtils();

    @AfterMethod
    public void cleanUp(){
       cleanupUtils.deleteFiles(modelsDir, Collections.singleton("*"));
    }

    //@Test
    public void testCreateModelAsClient() {
        SwaggerJavaModelGenerator modelGenerator = new SwaggerJavaModelGenerator();

        modelGenerator.setBaseDir(baseDir);
        modelGenerator.setPackageName("com.consol.citrus");
        modelGenerator.setSwaggerResource("file:" + baseDir + "/src/test/resources/swagger/petstore.json");

        modelGenerator.create();
    }

    @Test
    public void testCreateUtilsClass() {
        UtilsClassGenerator utilsClassGenerator = new UtilsClassGenerator();

        utilsClassGenerator.setBaseDir(baseDir + "/src/test/java");
        utilsClassGenerator.setPackageName("com.consol.citrus");

        utilsClassGenerator.create();
    }
}
