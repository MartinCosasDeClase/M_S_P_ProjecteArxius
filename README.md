# Memoria del Proyecto: Generador de Páginas Web Estáticas

## Descripción del Proyecto

En este proyecto debíamos crear un generador de páginas web estáticas leyendo un JSON y utilizando plantillas Thymeleaf para la creación de estas. Entre otros objetivos del proyecto, también se incluía la creación de un archivo `.ini` para las configuraciones básicas de la página, la elaboración de un esquema JSON que valide el JSON creado, añadir un archivo RSS y, como ampliación, utilizar la serialización para simular la memoria caché.

## Ficheros de Entrada

A continuación, veremos los diferentes ficheros antes mencionados junto con una pequeña explicación de su función.

### `Config.ini`
Este archivo se utiliza para obtener la información más básica de la página. En este caso, solo nos da el nombre de la página y el autor.

### `JSON`
Este archivo JSON contiene toda la información, así como las imágenes que se muestran en la web.

### `JSON Schema`
Este archivo se encarga de validar que el JSON mencionado cumpla con una estructura uniforme para así evitar posibles errores.

## Librerías, Clases y Dependencias

### Librerías

Hablando sobre las librerías utilizadas, la más importante para este proyecto ha sido Java I/O, ya que es la librería que se encarga de la simulación de la memoria caché, escribiendo y leyendo el archivo `.dat` con las siguientes funciones.

### Clases

En este proyecto he utilizado un sistema de clases que parte de un archivo **root**, el cual contiene dos listas de objetos: **Clases** y **SubClases**, que son los encargados de almacenar la información del JSON. Gracias a utilizar este tipo de estructura, se me ha facilitado la creación del archivo `.dat`.

### Ejemplos

---

## Descripción de las Plantillas Thymeleaf

Las plantillas utilizadas se encargan de la creación de las diferentes páginas del proyecto utilizando variables asignadas desde el **main** para esta función. Mi proyecto consta de tres plantillas que se encargan de crear el `index.html` y luego las otras dos páginas que distribuyen y muestran la información.

### Ejemplos

---

## Ficheros de Salida

### Añadir RSS

---

## Páginas Creadas

A continuación veremos el resultado final del proyecto.

---

## Problemas Resueltos y No Resueltos

Durante la elaboración del proyecto me he encontrado con varios problemas, tanto a la hora de crear las páginas como intentando simular la caché.

### Problema 1

Al inicio del proyecto, cuando intenté generar las primeras páginas, tuve problemas con las variables de Thymeleaf debido en parte a que no tenía muy claro cómo funcionaban y a que no estaba pasando los valores correctamente. Después de investigar un poco más sobre el funcionamiento de estas y realizar varias pruebas, pude solucionar el problema y mostrar todos los datos conforme yo quería.

### Problema 2

Durante la elaboración de la caché, tuve un problema muy consistente, el cual era que, por algún motivo, al serializar directamente sobre el main funcionaba correctamente, pero a la hora de deserializar daba siempre error y no generaba los archivos. La solución que encontré fue extraer las líneas de código a dos funciones y realizar este proceso en funciones separadas. Después de este cambio, el problema se solucionó y la caché se genera con total normalidad.
