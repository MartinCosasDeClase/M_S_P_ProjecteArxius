package com.martin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Clases> clases = null;
        List<SubClases> subClases = null;
        Root root = null;
        TemplateEngine templateEngine = new TemplateEngine();
        ProcessingReport report = null;
        try {
            File rootFile = new File("src/main/resources/cache/root.dat");
            if(!rootFile.exists()) {
                String path = "src/main/resources/json/clases.json";
                String schemaPath = "src/main/resources/json/schema.json";

                JsonNode mySchema = JsonLoader.fromFile(new File(schemaPath));
                JsonNode jsonOk = JsonLoader.fromFile(new File(path));

                JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.byDefault();
                JsonSchema jsonSchema = jsonSchemaFactory.getJsonSchema(mySchema);
                report = jsonSchema.validate(jsonOk);

                root = getRoot(path);
                clases = cargarClases(root);
                subClases = cargarSubClase(root);

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/main/resources/cache/root.dat"))) {
                    oos.writeObject(root);
                    System.out.println("SERIALIZADO");
                } catch (IOException e) {
                    System.err.println("Error en la serialización");
                    ;
                }
            } else {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("root.dat"))) {
                     root = (Root) ois.readObject();
                    clases = cargarClases(root);
                    subClases = cargarSubClase(root);
                    System.out.println("DESSERIALIZADO");
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println(root.toString());

                }
            }
            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("templates/");
            templateResolver.setSuffix(".html");

            templateEngine.setTemplateResolver(templateResolver);

            if ( report == null ||  report.isSuccess()) {
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

                for (SubClases subClase : subClases) {
                    Context contextSubclase = new Context();
                    contextSubclase.setVariable("subCalse", subClase);
                    contextSubclase.setVariable("nombre", subClase.name);
                    contextSubclase.setVariable("descripcion", subClase.descripcion);
                    contextSubclase.setVariable("imagen", subClase.imagen);
                    String subClaseHTML = templateEngine.process("template_subclase", contextSubclase);
                    String fileNameSubClase = "src/main/resources/html/subClasesDetails/detalles_sub_clase_" + subClase.name + ".html";

                    writeHTML(fileNameSubClase, subClaseHTML);
                }

            }
        }catch (IOException | ProcessingException e){
            System.err.println("Problema durante la ejecución");
        }
    }

    private static Root getRoot(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(path);
            return objectMapper.readValue(file, Root.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Clases> cargarClases(Root root) {
        try {
            return new ArrayList<>(root.getClases());
        } catch (Exception e) {
            System.err.println("Errorr al parsear");
            return null;
        }
    }

    public static List<SubClases> cargarSubClase(Root root) {
        try {
            return new ArrayList<>(root.getSubClases());
        } catch (Exception e) {
            System.err.println("Errorr al parsear");
            return null;
        }
    }

    public static void writeHTML(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Error de escritura");
        }
    }
}