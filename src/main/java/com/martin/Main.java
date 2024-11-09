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
import java.util.Objects;

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

        if (clases != null) {
            Context context = new Context();
            context.setVariable("clases", clases);

            String contHTML = templateEngine.process("template1", context);
            writeHTML("src/main/resources/html/index.html", contHTML);

            for (SubClases subClase : Objects.requireNonNull(subClases)) {
                if (subClase != null) {
                    System.out.println(subClase);
                    Context contextDetails = new Context();
                    contextDetails.setVariable("subClase", subClase);
                    contextDetails.setVariable("nameClase", subClase.getId());
                    contextDetails.setVariable("nameSubclase", subClase.getName());
                    contextDetails.setVariable("descripcion", subClase.getDescripcion());
                    contextDetails.setVariable("imagen", subClase.getImagen());

                    String detallesHTML = templateEngine.process("template2", contextDetails);
                    String fileName = "src/main/resources/html/detalles_" + subClase.getName() + ".html";

                    writeHTML(fileName, detallesHTML);
                }
            }

            for (SubClases subClase : Objects.requireNonNull(subClases)) {
                if (subClase != null) {
                    Context contextDetails = new Context();
                    contextDetails.setVariable("subClase", subClase);
                    contextDetails.setVariable("nameClase", subClase.getId());
                    contextDetails.setVariable("nameSubclase", subClase.getName());
                    contextDetails.setVariable("descripcion", subClase.getDescripcion());
                    contextDetails.setVariable("imagen", subClase.getImagen());
                    System.out.println(subClase.getName());
                    String detallesSubHTML = templateEngine.process("template_subclase", contextDetails);
                    String fileNameSubClase = "src/main/resources/html/detalles_subclase_" + subClase.getName() + ".html";

                    writeHTML(fileNameSubClase, detallesSubHTML);
                }
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
