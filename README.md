# Memoria del Proyecto: Generador de Páginas Web Estáticas

---
#### Autor: Martín Sánchez Pedrero
#### Curso: 2º DAM
---
## Índice

1. [Descripción del proyecto](#descripción-del-proyecto)  
2. [Ficheros de entrada](#ficheros-de-entrada)  
3. [Librerías, Clases y Dependencias](#librerías-clases-y-dependencias)  
4. [Plantilla Thymeleaf](#descripción-de-las-plantillas-thymeleaf)  
5. [Ficheros de salida](#ficheros-de-salida)  
6. [Problemas resueltos y no resueltos](#problemas-resueltos-y-no-resueltos)
    
---
## Descripción del Proyecto

En este proyecto debíamos crear un generador de páginas web estáticas leyendo un JSON y utilizando plantillas Thymeleaf para la creación de estas. Entre otros objetivos del proyecto, también se incluía la creación de un archivo `.ini` para las configuraciones básicas de la página, la elaboración de un esquema JSON que valide el JSON creado, añadir un archivo RSS y, como ampliación, utilizar la serialización para simular la memoria caché.

## Ficheros de Entrada

A continuación, veremos los diferentes ficheros antes mencionados junto con una pequeña explicación de su función.

### `Config.ini`
Este archivo se utiliza para obtener la información más básica de la página. En este caso, solo nos da el nombre de la página y el autor.

```java
tittle = Clases Dnd
subTittle = Martín Sánchez Pedrero
```

### `JSON`
Este archivo JSON contiene toda la información, así como las imágenes que se muestran en la web.

Fragmento de la lista de Clases:
```json
"clases": [
    {
      "name": "Picaro",
      "mini-description": "Especialistas en sigilo, engano y ataques certeros con danio critico.",
      "imagen": "../img/Picaro.jpg"
    },
    {
      "name": "Mago",
      "mini-description": "Eruditos de las artes arcanas, con acceso a una amplia gama de hechizos.",
      "imagen": "../img/magoDND.jpg"
    },
    {
      "name": "Bardo",
      "mini-description": "Artistas versatiles que inspiran, encantan y manipulan con magia y musica.",
      "imagen": "../img/bardo.jpeg"
    },
```

Fragmento de la lista de SubClases:

```json
"sub-clases": [
    {
      "name-clase": "Picaro",
      "name-subclase": "Asesino",
      "mini-description": "Especialista en ataques sorpresa y eliminaciones rapidas, aprovecha el sigilo para maximizar danio critico.",
      "imagen": "../../img/picaroAsesino.png"
    },
    {
      "name-clase": "Picaro",
      "name-subclase": "Ladron",
      "mini-description": "Versatil y agil, destaca en robar objetos, desactivar trampas y explorar zonas peligrosas con facilidad.",
      "imagen": "../../img/PicaroLadron.png"
    },
    {
      "name-clase": "Picaro",
      "name-subclase": "Soul Blade",
      "mini-description": "Manipula energia magica para imbuir sus ataques con poderes psiquicos, creando versatilidad en combate.",
      "imagen": "../../img/PicaroSoulBlade.jpg"
    },
    {
      "name-clase": "Picaro",
      "name-subclase": "Mente Maestra",
      "mini-description": "Un estratega que controla el campo de batalla y apoya a sus aliados con tacticas y enganos.",
      "imagen": "../../img/PicaroMenteMaestra.avif"
    },
    {
      "name-clase": "Picaro",
      "name-subclase": "Fantasma",
      "mini-description": "Un pigaro etereo que puede atravesar paredes y evitar deteccion, perfecto para infiltracion.",
      "imagen": "../../img/PicaroFantasma.jpg"
    },
```

### `JSON Schema`
Este archivo se encarga de validar que el JSON mencionado cumpla con una estructura uniforme para así evitar posibles errores.

```json
{
  "$schema": "https://json-schema.org/draft-07/schema#",
  "clases":{
    "type": "array",
    "items": {
      "type": "object ",
      "properties": {
        "name": {
          "description": "Nombre identificativo de la clase",
          "type": "string"
        },
        "mini-description": {
          "description": "Una pequeña descripcion de la clase",
          "type": "string"
        },
        "imagen": {
          "description": "Enlace a una imagen",
          "type": "string"
        }
      },
      "required": ["name","mini-description"]
    }
  },
  "sublases": {
    "type": "array",
    "items": {
      "type": "object",
      "properties": {
        "name-clase": {
          "description": "Nombre que identifica la clase",
          "type": "string"
        },
        "name-subclase": {
          "description": "Nombre de la Subclase",
          "type": "string"
        },
        "mini-description": {
          "description": "Una pequeña descripcion de la clase",
          "type": "string"
        },
        "imagen": {
          "description": "Enlace a una imagen",
          "type": "string"
        }
      },
      "required": ["name-clase","name-subclase","mini-description"]
    }
  }
}
```

## Librerías, Clases y Dependencias

### Librerías

Hablando sobre las librerías utilizadas, vo a profundizar Java I/O, ya que es la librería que se encarga de la simulación de la memoria caché (lo cual es una ampliación), escribiendo y leyendo el archivo `.dat` con las siguientes funciones.

```java
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
````

A demás de esa también he utilizado:  
- **thymeleaf**: Esta libreia es la encargada de generar los html estaticos.
- **jackson-databind y jackson-core**: Se encarga del parseo de el `json` a los POJOS.
- **json-schema-validator**: Esta libreria es la que valida del `json` con el `schema`.

### Clases

En este proyecto he utilizado un sistema de clases que parte de un archivo **root**, el cual contiene dos listas de objetos: **Clases** y **SubClases**, que son los encargados de almacenar la información del JSON. Gracias a utilizar este tipo de estructura, se me ha facilitado la creación del archivo `.dat`.

#### Clases:

Root

```java
package com.martin;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Root implements Serializable {
    @JsonProperty("clases")
    List<Clases> clases;
    @JsonProperty("sub-clases")
    List<SubClases> subClases;

    public Root() {
    }

    public Root(List<Clases> clases, List<SubClases> subClases) {
        this.clases = clases;
        this.subClases = subClases;
    }

    public List<Clases> getClases() {
        return clases;
    }

    public void setClases(List<Clases> clases) {
        this.clases = clases;
    }

    public List<SubClases> getSubClases() {
        return subClases;
    }

    public void setSubClases(List<SubClases> subClases) {
        this.subClases = subClases;
    }
}
```

Clases

```java
package com.martin;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Clases implements Serializable {
    @JsonProperty("name")
    String name;
    @JsonProperty("mini-description")
    String descripcion;
    @JsonProperty("imagen")
    String imagen;

    public Clases() {
    }

    public Clases(String name, String descripcion, String imagen) {
        this.name = name;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Clases{" +
                "name='" + name + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}

```

SubClases

```java
package com.martin;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SubClases implements Serializable {
    @JsonProperty("name-clase")
    String id;
    @JsonProperty("name-subclase")
    String name;
    @JsonProperty("mini-description")
    String descripcion;
    @JsonProperty("imagen")
    String imagen;

    public SubClases() {
    }

    public SubClases(String id, String name, String descripcion, String imagen) {
        this.id = id;
        this.name = name;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "SubClases{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}

```

#### Dependencias

```xml
<dependencies>
        <dependency>
            <groupId>org.thymeleaf</groupId>
            <artifactId>thymeleaf</artifactId>
            <version>3.1.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.18.1</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>2.18.1</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.32</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.6</version>
        </dependency>
        <dependency>
            <groupId>com.github.java-json-tools</groupId>
            <artifactId>json-schema-validator</artifactId>
            <version>2.2.14</version>
        </dependency>

```
---

## Descripción de las Plantillas Thymeleaf

Las plantillas utilizadas se encargan de la creación de las diferentes páginas del proyecto utilizando variables asignadas desde el **main** para esta función. Mi proyecto consta de tres plantillas que se encargan de crear el `index.html` y luego las otras dos páginas que distribuyen y muestran la información.

A continuación adjunto una de las plantillas.

```html
<!DOCTYPE html>
<html lang="es" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Nivel 19 - Clases</title>
    <link rel="stylesheet" href="../css/style1.css">
</head>
<body>
<header>
    <h1>NIVEL </h1><img src="../img/LEVEL19.png" width="90px" alt="IMG">
</header>

<main>
    <section>
        <ul>
            <li th:each="clase : ${clases}">
                <div>
                    <img th:src="@{${clase.imagen}}" alt="IMAGEN" width="5%">
                </div>
                <h3 th:text="${clase.name}"></h3>

                <p th:text="${clase.descripcion}">Descripcion de la clase</p>
                <a th:href="@{'clasesDetails/detalles_clase_'+ ${clase.name} + '.html'}">Ver detalles de subclases</a>
            </li>
        </ul>
    </section>
</main>
</body>
</html>

```
---

## Ficheros de Salida

### RSS

Con el rss me han surgido muchas dudas que al final no he conseguido resolver. De todas formas adunto capturas de la creacion e implmentacion.

#### Generación del RSS:

![image](https://github.com/user-attachments/assets/dd9ce521-3887-41fa-9fe6-720c7a2692d8)

### Archivo RSS:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0">
<channel>
<link>src/main/resources/html/index.html</link>
<item>
<title>Picaro</title>
<link>src/main/resources/html/clasesDeatail/detalles_clase_Picaro.html</link>
<description>Especialistas en sigilo, engano y ataques certeros con danio critico.</description>
</item>
<item>
<title>Mago</title>
<link>src/main/resources/html/clasesDeatail/detalles_clase_Mago.html</link>
<description>Eruditos de las artes arcanas, con acceso a una amplia gama de hechizos.</description>
</item>
<item>
<title>Bardo</title>
<link>src/main/resources/html/clasesDeatail/detalles_clase_Bardo.html</link>
<description>Artistas versatiles que inspiran, encantan y manipulan con magia y musica.</description>
</item>
<item>
<title>Guerrero</title>
<link>src/main/resources/html/clasesDeatail/detalles_clase_Guerrero.html</link>
<description>Maestros de armas y tacticas de combate, expertos en resistencia y danio fisico.</description>
</item>
<item>
<title>Barbaro</title>
<link>src/main/resources/html/clasesDeatail/detalles_clase_Barbaro.html</link>
<description>Guerreros salvajes y feroces, especializados en la fuerza bruta y la resistencia.</description>
</item>
</channel>
</rss>

```

### Implementación en la plantilla:

![image](https://github.com/user-attachments/assets/b88dce9e-2569-4bcc-9278-56c6333aa515)


### Resultado

![image](https://github.com/user-attachments/assets/5c004faf-976b-4df6-af11-1907f9b8ce0c)

---

## Páginas Creadas

En este apartado veremos el resultado final de las paginas generadas

Listado de Clases:
![Clases](https://github.com/MartinCosasDeClase/M_S_P_ProjecteArxius/blob/master/imgResultado/indexResult.png)

Listado de SubClases:
![Listado de SubClases](https://github.com/MartinCosasDeClase/M_S_P_ProjecteArxius/blob/master/imgResultado/subClasesListResult.png)

Descripcion de las SubClases:
![Descripción SubClases](https://github.com/MartinCosasDeClase/M_S_P_ProjecteArxius/blob/master/imgResultado/descripcionSubClaseResult.png)

---

## Problemas Resueltos y No Resueltos

Durante la elaboración del proyecto me he encontrado con varios problemas, tanto a la hora de crear las páginas como intentando simular la caché.

### Problema 1

Al inicio del proyecto, cuando intenté generar las primeras páginas, tuve problemas con las variables de Thymeleaf debido en parte a que no tenía muy claro cómo funcionaban y a que no estaba pasando los valores correctamente. Después de investigar un poco más sobre el funcionamiento de estas y realizar varias pruebas, pude solucionar el problema y mostrar todos los datos conforme yo quería.

### Problema 2

Durante la elaboración de la caché, tuve un problema muy consistente, el cual era que, por algún motivo, al serializar directamente sobre el main funcionaba correctamente, pero a la hora de deserializar daba siempre error y no generaba los archivos. La solución que encontré fue extraer las líneas de código a dos funciones y realizar este proceso en funciones separadas. Después de este cambio, el problema se solucionó y la caché se genera con total normalidad.

### Problema 3

Este problema es con el RSS a que no acaba de quedarme clara su implemetación. De todas formas la solucion parcial que he encontrado me ha llevado a generarlo y, aunque erroneamente, a implementarlo.
