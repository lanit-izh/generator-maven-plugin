package com.consol.citrus.generate.javadsl;

import com.consol.citrus.Generator;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.util.FileUtils;
import io.swagger.codegen.v3.ClientOptInput;
import io.swagger.codegen.v3.DefaultGenerator;
import io.swagger.codegen.v3.config.CodegenConfigurator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class SwaggerJavaModelGenerator extends Generator {

    @Override
    public void create() {
        CodegenConfigurator config = new CodegenConfigurator();
        config.setLibrary("resteasy");
        config.addAdditionalProperty("useBeanValidation", true);
        config.addAdditionalProperty("enable303", true);
        DefaultGenerator generator = new DefaultGenerator();
        Class<?> clazz = DefaultGenerator.class;

        if (swaggerResource.startsWith("file:")) {
            swaggerResource = swaggerResource.substring(5);
        }

        try {
            config.setInputSpec(FileUtils.readToString(new File(swaggerResource)));
        } catch (IOException e) {
            throw new CitrusRuntimeException("Failed to parse Swagger Open API specification: " + swaggerResource, e);
        }

        config.setLang("java");
        config.setOutputDir(baseDir);
        config.setModelPackage(packageName + ".models");

        ClientOptInput input = config.toClientOptInput();

        generator.opts(input);

        System.setProperty("generateApis", "false");
        System.setProperty("generateModels", "true");
        System.setProperty("supportingFiles", "false");
        System.setProperty("modelTests", "false");
        System.setProperty("modelDocs", "false");
        System.setProperty("apiTests", "false");
        System.setProperty("apiDocs", "false");

        try {
            Field field = clazz.getDeclaredField("generateSwaggerMetadata");
            field.setAccessible(true);
            field.set(generator, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        generator.generate();
    }
}
