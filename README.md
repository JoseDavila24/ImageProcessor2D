# **Procesamiento de Imágenes 2D en Java**

## **Introducción**

El procesamiento de imágenes es una disciplina fundamental en el campo de la visión por computadora y la gráfica computacional. En esta actividad, se ha desarrollado un software en Java con una interfaz gráfica de usuario (GUI) para la manipulación y análisis de imágenes en 2D.

El objetivo principal es implementar diversas técnicas de procesamiento digital de imágenes, como conversión a escala de grises, binarización, aplicación de filtros espaciales y morfológicos, así como análisis en el dominio de la frecuencia.

El desarrollo del software se llevó a cabo en GitHub, donde se encuentra disponible el código fuente junto con documentación detallada sobre su funcionamiento. Además, en el repositorio se incluye un archivo PDF que explica las operaciones necesarias para implementar los distintos filtros y transformaciones aplicadas a las imágenes.

**Repositorio en GitHub:**  
[https://github.com/JoseDavila24/ImageProcessor2D.git](https://github.com/JoseDavila24/ImageProcessor2D.git)


## **Características Principales**

- Interfaz gráfica desarrollada con Java Swing (`Menu.java`)
- Edición con funcionalidades de deshacer y rehacer
- Transformaciones de imagen:
  - Conversión a escala de grises (optimizada con paralelización)
  - Conversión a binaria con umbral configurable
  - Imagen negativa
  - Histograma con representación visual interactiva
- Filtros espaciales:
  - Suavizado: media, mediana
  - Realce: Sobel, Prewitt, Laplaciano
- Filtros morfológicos:
  - Erosión, dilatación
  - Apertura y cierre
  - Esqueletonización iterativa
- Filtros simulados de frecuencia:
  - Paso bajo (media)
  - Paso alto (original - suavizado)
  - Paso banda (combinación)
- Pilas de deshacer/rehacer para edición no destructiva

---

## **Estructura del Proyecto**

### 1. Menu.java

- Control de interfaz gráfica (menús, botones, imágenes)
- Aplicación de filtros mediante funciones
- Manejo de eventos
- Control de imagen cargada
- Pila de deshacer/rehacer
- Visualización escalada en `JScrollPane`

### 2. Operaciones.java

Contiene todos los filtros implementados como métodos estáticos sobre objetos `BufferedImage`, incluyendo:

- `escalaDeGrises()`
- `imagenBinaria(umbral)`
- `negativo()`
- `filtroMedia(tamaño)`, `filtroMediana(tamaño)`
- `filtroSobel()`, `filtroPrewitt()`, `filtroLaplaciano()`
- `erosion3x3()`, `dilatacion3x3()`, `apertura3x3()`, `cierre3x3()`, `esqueletonizacion3x3()`
- `filtroPasoBajo()`, `filtroPasoAlto()`, `filtroPasoBanda()`

### 3. HistogramaPanel.java

- Componente personalizado para visualizar el histograma de la imagen actual.
- Etiquetas en el eje X (niveles de gris) y eje Y (frecuencia).

---

## **Requisitos Previos**

- Java 8 o superior instalado
- IDE como IntelliJ IDEA, Eclipse o NetBeans (opcional)
- Git (opcional)

---

## **Clonar y Ejecutar en Windows**

### Opción A: Ejecutar el archivo JAR

1. Descarga o copia `ImageProcessor2D.jar`  
2. Asegúrate de tener Java instalado  
3. Abre una terminal (CMD o PowerShell) y navega al directorio donde se encuentra el JAR:
   ```
   cd C:\Ruta\al\proyecto
   java -jar ImageProcessor2D.jar
   ```
   También puedes hacer doble clic si Java está bien configurado.

### Opción B: Compilar manualmente

1. Clona el repositorio:
   ```
   git clone https://github.com/JoseDavila24/ImageProcessor2D.git
   cd ImageProcessor2D
   ```
2. Compila:
   ```
   javac Menu.java Operaciones.java HistogramaPanel.java Main.java
   ```
3. Ejecuta:
   ```
   java Main
   ```

---

## **Uso de la Aplicación**

1. **Abrir imagen**: Menú "Archivo > Abrir imagen".
2. **Aplicar filtros**: Menús "Imagen" o "Filtros".
3. **Filtros con parámetros**: Se solicita valor de entrada (umbral, tamaño).
4. **Deshacer/Rehacer**: Menú "Edición".
5. **Guardar imagen**: "Archivo > Guardar" o "Guardar como..."
6. **Cerrar imagen**: "Archivo > Cerrar".

---

## **Capturas de Pantalla**

(Insertar imágenes reales si deseas aquí, o reemplaza las descripciones por las imágenes en Word)

1. **Ventana principal**  
2. **Menú de filtros activos**
3. **Histograma con leyendas**
4. **Aplicación de filtro de bordes**

---

## **Personalización del Proyecto**

- Puedes agregar nuevos filtros en `Operaciones.java`
- En `Menu.java`, añade el `JMenuItem` correspondiente y conéctalo al filtro
- Ideal para experimentar con otros modelos de color o filtros de convolución

---

## **Contribuciones**

Para colaborar con el proyecto:

1. Realiza un fork del repositorio
2. Crea una nueva rama para tu funcionalidad
3. Realiza tus cambios y confirma (`commit`)
4. Haz un pull request con tu propuesta

---

## **Licencia**

Este proyecto está bajo licencia MIT. Consulta el archivo `LICENSE` en el repositorio.

---

## **Referencias**

- Jercyae. GitHub - editor de imágenes:  
  https://github.com/jercyae7/editor-de-imagenes.git

- Torres, A. D. (1996). Procesamiento digital de imágenes.  
  Perfiles Educativos, 72.  
  https://www.redalyc.org/pdf/132/13207206.pdf

---

## **Conclusión**

El uso de GitHub en este proyecto resultó fundamental, no solo para gestionar el código y la documentación, sino también para acostumbrarme a un entorno ampliamente utilizado en el desarrollo de software.

Como estudiante de Ingeniería en Sistemas Computacionales, familiarizarme con herramientas de control de versiones es esencial, ya que facilita la colaboración, el seguimiento de cambios y la organización de proyectos. Esta experiencia me ha permitido mejorar mis habilidades en el manejo de repositorios, lo que será de gran utilidad en futuros desarrollos dentro del ámbito de TI.
