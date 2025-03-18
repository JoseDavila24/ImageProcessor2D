# Procesamiento de Imágenes 2D ✨

Este proyecto consiste en una aplicación de escritorio en Java para realizar operaciones de procesamiento de imágenes en 2D. Incluye una interfaz gráfica (Swing) que permite **abrir**, **guardar** y **cerrar** imágenes, así como aplicar **filtros** y **transformaciones** como binarización, escala de grises, suavizado, realce, morfología, etc.

## Características principales ⚡

- **Interfaz gráfica** construida con Java Swing (clase `Menu`).
- **Operaciones de edición**: deshacer (undo) y rehacer (redo).
- **Transformaciones de imagen**:  
  - Conversión a escala de grises.  
  - Conversión a binaria (blanco/negro) con umbral configurable.  
  - Negativo.  
  - Histograma (ecualización en escala de grises).  
- **Filtros**:
  - Suavizado (filtro de media y mediana).
  - Detección de bordes (Sobel, Prewitt, Laplaciano).
  - Filtros morfológicos (erosión, dilatación, etc.).
  - Filtros simulados de frecuencia (paso bajo, paso alto, paso banda).
- **Pilas de deshacer y rehacer** para gestionar los cambios realizados.

## Estructura del proyecto 📂

El proyecto se compone principalmente de dos clases:

1. **Menu**  
   Se encarga de la interfaz gráfica con menús, acciones y la gestión de:  
   - Abrir, guardar y cerrar imágenes.  
   - Menús para aplicar distintos filtros.  
   - Pila de acciones (undo, redo).  
   - Redimensionado automático de la imagen dentro de un `JScrollPane`.

2. **Operaciones**  
   Contiene métodos estáticos que realizan las transformaciones y filtros sobre un objeto `BufferedImage`. Por ejemplo:
   - Conversión a escala de grises (`escalaDeGrises`).
   - Negativo (`negativo`).
   - Filtro de media y mediana (`filtroMedia`, `filtroMediana`).
   - Detección de bordes (`filtroSobel`, `filtroPrewitt`, `filtroLaplaciano`).
   - Morfología matemática binaria (`erosion3x3`, `dilatacion3x3`).
   - Filtros “simulados” de frecuencia (`filtroPasoBajo`, `filtroPasoAlto`, `filtroPasoBanda`).

## Documentación adicional 📄

Para mayor detalle sobre la **implementación** y la **teoría** de las operaciones de imagen, revisa el documento [**operaciones.pdf**](https://github.com/JoseDavila24/ImageProcessor2D/blob/f368046371d7366a0909ab3b7afcc800d46b392a/operaciones.pdf) incluido en el repositorio. Allí encontrarás descripciones más técnicas de cada filtro y su lógica interna.

## Requisitos previos ✅

- **Java 8** o posterior (JDK o JRE) instalado en tu sistema.
- (Opcional) Un IDE como IntelliJ IDEA, Eclipse o NetBeans para abrir y compilar el proyecto con mayor facilidad.
- **Git** para clonar el repositorio (si planeas descargarlo desde GitHub).

## Clonar y ejecutar en Windows

### Opción A: Ejecutar directamente el **ImageProcessor2D.jar** ⚙️

1. **Descarga o copia** el archivo `ImageProcessor2D.jar` en una carpeta de tu elección.
2. **Asegúrate** de tener Java correctamente instalado y accesible desde la línea de comandos (o que los archivos JAR se abran con Java por defecto).
3. **Abre** una ventana de **Símbolo del sistema** (CMD) o **PowerShell** y navega hasta la carpeta donde tengas el `ImageProcessor2D.jar`. Por ejemplo:
   ```bash
   cd C:\Users\TuUsuario\Documents\ImageProcessor2D
   ```
4. **Ejecútalo** con el siguiente comando:
   ```bash
   java -jar ImageProcessor2D.jar
   ```
   Esto abrirá la ventana de la aplicación con la interfaz gráfica.

   > **Alternativa:** Si en tu sistema Windows los archivos JAR se abren con doble clic (y Java está correctamente asociado), simplemente haz doble clic sobre `ImageProcessor2D.jar`.

### Opción B: Compilar manualmente los archivos `.java` ⚙️

Si prefieres compilar el proyecto en lugar de usar el `.jar`:

1. **Clona el repositorio** (si aún no lo has hecho):
   ```bash
   git clone https://github.com/JoseDavila24/ImageProcessor2D.git
   ```
2. **Navega** hasta la carpeta del proyecto:
   ```bash
   cd ImageProcessor2D
   ```
3. **Compila** los archivos `.java`:
   ```bash
   javac Menu.java Operaciones.java
   ```
4. **Ejecuta** la clase principal (asegúrate de que sea `Main` o `Menu`, según tu código real):
   ```bash
   java Main
   ```

## Uso de la aplicación 🚀

1. **Abrir imagen**: Ve al menú `Archivo > Abrir imagen`. Aparecerá un diálogo para seleccionar el archivo de imagen.
2. **Aplicar filtros o transformaciones**: 
   - Encuéntralos en los menús `Imagen` o `Filtros`.
   - Algunos filtros (p. ej., binarización) solicitarán un valor numérico (tamaño de máscara, umbral, etc.).
3. **Deshacer/Rehacer**:  
   - `Edición > Deshacer` para revertir el último cambio.
   - `Edición > Rehacer` para restaurar un cambio deshecho.
4. **Guardar la imagen**:
   - `Archivo > Guardar imagen` para guardar en el archivo actual.
   - `Archivo > Guardar como...` para elegir un nombre/ruta diferente.
5. **Cerrar** la imagen (sin salir de la aplicación) con `Archivo > Cerrar`.

## Capturas de pantalla 📸

A continuación se muestran algunas capturas del programa en funcionamiento. Si prefieres verlas en GitHub, haz clic en cada imagen:

#### 1. Ventana principal
![Ventana principal](https://github.com/JoseDavila24/ImageProcessor2D/blob/6edac930c2067a2ad0d4568c4b0adb4e0fe5f4cd/img/Ventana%20Principal.png?raw=true)

#### 2. Menú de opciones
![Menú de opciones](https://github.com/JoseDavila24/ImageProcessor2D/blob/6edac930c2067a2ad0d4568c4b0adb4e0fe5f4cd/img/Menu_de_opciones.png?raw=true)

#### 3. Abrir imagen
![Abrir imagen](https://github.com/JoseDavila24/ImageProcessor2D/blob/6edac930c2067a2ad0d4568c4b0adb4e0fe5f4cd/img/Abrir_imagen.png?raw=true)

#### 4. Aplicar un filtro
![Aplicar un filtro](https://github.com/JoseDavila24/ImageProcessor2D/blob/6edac930c2067a2ad0d4568c4b0adb4e0fe5f4cd/img/Aplicar_un_filtro.png?raw=true)

## Personalización 🔧

- En la clase `Menu`, puedes añadir o eliminar ítems de menú para adaptar la interfaz a tus necesidades.
- En la clase `Operaciones`, puedes añadir nuevos métodos que implementen distintos tipos de filtros o transformaciones y luego crear los `JMenuItem` correspondientes en `Menu` para invocarlos.

## Contribuciones 🙌

¡Las contribuciones son bienvenidas! Para aportar al proyecto:

1. Haz un **fork** de este repositorio.
2. Crea una rama (`git checkout -b mi-nueva-funcionalidad`).
3. Realiza tus modificaciones y confirma los cambios (`git commit -m "Agrego nuevo filtro de XYZ"`).
4. Envía tus cambios a tu repositorio remoto (`git push origin mi-nueva-funcionalidad`).
5. Abre un **Pull Request** en este repositorio con tus cambios.

## Licencia 📜

Este proyecto está licenciado bajo los términos de la Licencia MIT.  
Por favor, revisa el archivo [LICENSE](https://github.com/JoseDavila24/ImageProcessor2D/blob/main/LICENSE) para más detalles.
