# Procesamiento de Imágenes 2D

Este proyecto consiste en una aplicación de escritorio en Java para realizar operaciones de procesamiento de imágenes en 2D. Incluye una interfaz gráfica (Swing) que permite abrir, guardar y cerrar imágenes, así como aplicar distintos filtros y transformaciones (binarización, escala de grises, filtros de suavizado, realce, morfología, etc.).

## Características principales

- **Interfaz gráfica** construida con Java Swing (clase `Menu`).
- **Operaciones de edición** como deshacer (undo) y rehacer (redo).
- **Transformaciones de imagen**: 
  - Conversión a escala de grises.
  - Conversión a binaria (blanco y negro) con umbral configurable.
  - Negativo.
  - Histograma (ecualización en escala de grises).
- **Filtros**:
  - Suavizado (filtro de media y mediana).
  - Detección de bordes (Sobel, Prewitt, Laplaciano).
  - Filtros morfológicos (erosión, dilatación, etc.).
  - Filtros simulados de frecuencia (paso bajo, paso alto, paso banda).
- **Pilas de deshacer y rehacer** que permiten gestionar los cambios realizados.

## Estructura del proyecto

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

## Requisitos previos

- **Java 8** o posterior (JDK o JRE) instalado en tu sistema.
- (Opcional) Un IDE como IntelliJ IDEA, Eclipse o NetBeans para abrir y compilar el proyecto con mayor facilidad.
- **Git** para clonar el repositorio (si planeas descargarlo desde GitHub).

## Clonar y ejecutar en Windows

### Opción A: Ejecutar directamente el **ImageProcessor2D.jar**

1. **Descarga o copia** el archivo `ImageProcessor2D.jar` en una carpeta de tu elección.
2. **Asegúrate** de tener Java correctamente instalado y accesible desde la línea de comandos (o al menos que los archivos JAR se abran con Java por defecto).
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

### Opción B: Compilar manualmente los archivos `.java`

Si prefieres compilar el proyecto en lugar de usar el `.jar`:

1. **Clonar el repositorio** (si aún no lo has hecho):  
   ```bash
   git clone https://github.com/usuario/mi-proyecto-imagenes.git
   ```
   (Reemplaza la URL con la de tu repositorio real.)

2. **Navegar hasta** la carpeta del proyecto:
   ```bash
   cd mi-proyecto-imagenes
   ```

3. **Compilar** los archivos `.java`:
   ```bash
   javac Menu.java Operaciones.java
   ```

4. **Ejecutar** la clase principal:
   ```bash
   java Menu
   ```

## Uso de la aplicación

1. **Abrir imagen**: Ve al menú `Archivo > Abrir imagen`. Se abrirá un diálogo para seleccionar el archivo de imagen desde tu computadora.
2. **Aplicar filtros o transformaciones**: 
   - Dirígete a los menús `Imagen` o `Filtros`. 
   - Selecciona la transformación o el filtro que desees (por ejemplo, `Imagen > Escala de grises`).
   - Para ciertos filtros (como binarización o mediana/media) la aplicación te solicitará un valor numérico (tamaño de máscara, umbral, etc.).
3. **Deshacer/Rehacer**:  
   - Para deshacer el último cambio, ve a `Edición > Deshacer`.
   - Para rehacer, ve a `Edición > Rehacer`.
4. **Guardar la imagen**:
   - `Archivo > Guardar imagen` para guardar en el archivo actual.
   - `Archivo > Guardar como...` para guardar con un nombre/ruta diferente.
5. **Cerrar** la imagen (sin cerrar la aplicación) con `Archivo > Cerrar`.

## Personalización

- En la clase `Menu`, puedes añadir o eliminar ítems de menú para adaptar la interfaz a tus necesidades.
- En la clase `Operaciones`, puedes añadir nuevos métodos que implementen distintos tipos de filtros o transformaciones y luego añadir los correspondientes `JMenuItem` en `Menu` para poder aplicarlos desde la interfaz.

## Contribuciones

¡Las contribuciones son bienvenidas! Para aportar al proyecto:

1. Haz un **fork** de este repositorio.
2. Crea una rama (`git checkout -b mi-nueva-funcionalidad`).
3. Realiza tus modificaciones y confirma los cambios (`git commit -m "Agrego nuevo filtro de XYZ"`).
4. Envía tus cambios a tu repositorio remoto (`git push origin mi-nueva-funcionalidad`).
5. Abre un **Pull Request** en este repositorio con tus cambios.

## Licencia
Este proyecto está licenciado bajo los términos de la Licencia MIT.
Por favor, revisa el archivo LICENSE para más detalles.
