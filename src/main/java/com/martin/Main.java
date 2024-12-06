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
        List<Clases> clases = new ArrayList<>();
        List<SubClases> subClases = new ArrayList<>();
        Root root = new Root();
        TemplateEngine templateEngine = new TemplateEngine();
        ProcessingReport report = null;
        String path = "src/main/resources/json/clases.json";
        try {
            File rootFile = new File("src/main/resources/cache/root.dat");
            if(!rootFile.exists()) {

                String schemaPath = "src/main/resources/json/schema.json";

                JsonNode mySchema = JsonLoader.fromFile(new File(schemaPath));
                JsonNode jsonOk = JsonLoader.fromFile(new File(path));

                JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.byDefault();
                JsonSchema jsonSchema = jsonSchemaFactory.getJsonSchema(mySchema);
                report = jsonSchema.validate(jsonOk);

                root = getRoot(path);
                serializar(rootFile, root);
                clases = cargarClases(root);
                subClases = cargarSubClase(root);

            } else {
                root = desserializar(rootFile);
                clases = cargarClases(root);
                subClases = cargarSubClase(root);
            }
            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("templates/");
            templateResolver.setSuffix(".html");

            templateEngine.setTemplateResolver(templateResolver);

            if ( report == null ||  report.isSuccess() || root != null) {
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
                String rss = "src/main/resources/xml/rss.xml";
                rssCreator(clases,rss);

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
    public static void serializar(File rootFile, Root root){
        try (ObjectOutputStream oOS = new ObjectOutputStream(new FileOutputStream(rootFile));) {
            oOS.writeObject(root);
            System.out.println("SERIALIZADO");
        } catch (IOException e) {
            System.err.println("Error en la serialización");
        }
    }
    public static Root desserializar(File root){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(root))) {
            System.out.println("DESSERIALIZADO");
            return (Root) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ERROR DESSERIALIZANDO");
            return null;
        }
    }

    public static void rssCreator(List<Clases> clases, String rss){
         try {
        FileWriter fileWriter = new FileWriter(rss);
        BufferedWriter rssWriter = new BufferedWriter(fileWriter);
        rssWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        rssWriter.write("<rss version=\"2.0\">\n");
        rssWriter.write("<channel>\n");
        rssWriter.write("<link>src/main/resources/html/index.html</link>\n");
        for (Clases classes : clases) {
            rssWriter.write("<item>\n");
            rssWriter.write("<title>" + classes.name + "</title>\n");
            rssWriter.write("<link>src/main/resources/html/clasesDeatail/detalles_clase_" + classes.name + ".html</link>\n");
            rssWriter.write("<description>" + classes.descripcion + "</description>\n");
            rssWriter.write("</item>\n");
        }
        rssWriter.write("</channel>\n");
        rssWriter.write("</rss>\n");
        rssWriter.close();
        fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}