package com.martin;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String path = "src/main/resources/json/clases.json";

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Root root = getRoot(path);
        List<Clases> clases = cargarClases(root);
        List<SubClases> subClases = cargarSubClase(root);

        if (clases != null && subClases != null) {
            Context context = new Context();
            context.setVariable("clases", clases);

            String contHTML = templateEngine.process("template1", context);
            writeHTML("src/main/resources/html/index.html", contHTML);

            for (Clases clase : clases) {
                System.out.println(clase.getName());
                Context contextClase = new Context();
                contextClase.setVariable("clase", clase);
                contextClase.setVariable("subClases", subClases.stream().filter(n -> n.getId().equals(clase.getName())));
                String claseHTML = templateEngine.process("template2", contextClase);
                String fileNameClase = "src/main/resources/html/clasesDetails/detalles_clase_" + clase.getName() + ".html";

                writeHTML(fileNameClase, claseHTML);

                System.out.println("-----------------");
            }

            for (SubClases subClase: subClases){
                Context contextSubclase = new Context();
                contextSubclase.setVariable("subCalse",subClase);
                contextSubclase.setVariable("nombre",subClase.name);
                contextSubclase.setVariable("descripcion",subClase.descripcion);
                contextSubclase.setVariable("imagen",subClase.imagen);
                String subClaseHTML = templateEngine.process("template_subclase",contextSubclase);
                String fileNameSubClase = "src/main/resources/html/subClasesDetails/detalles_sub_clase_"+ subClase.name + ".html";

                writeHTML(fileNameSubClase,subClaseHTML);
            }

        } else {
            System.err.println("ERROR AL CARGAR.");
        }
    }

    private static Root getRoot(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(path);
            return objectMapper.readValue(file, Root.class);

        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Clases> cargarClases(Root root) {
        try {
            return new ArrayList<>(root.getClases());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<SubClases> cargarSubClase(Root root) {
        try {
            return new ArrayList<>(root.getSubClases());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeHTML(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
