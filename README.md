# Procesamiento de Imágenes 2D ✨

Este proyecto consiste en una aplicación de escritorio en Java para realizar operaciones de procesamiento de imágenes en 2D. Incluye una interfaz gráfica (Swing) que permite **abrir**, **guardar** y **cerrar** imágenes, así como aplicar **filtros** y **transformaciones** como binarización, escala de grises, suavizado, realce, morfología, etc.

## 💡 **Descripción General**
Este es un sistema de procesamiento de imágenes en 2D con interfaz gráfica construida con **Swing**, que permite:
- Abrir, mostrar, guardar imágenes
- Convertir imágenes a escala de grises, binarizarlas, aplicar negativos
- Visualizar histogramas de intensidad
- Aplicar filtros (media, mediana, Sobel, Prewitt, etc.)
- Aplicar transformaciones morfológicas (erosión, dilatación, apertura, cierre, esqueletonización)

---

## 🧱 Estructura de Clases

| Clase             | Rol                                                                 |
|------------------|----------------------------------------------------------------------|
| `Main`           | Punto de entrada. Carga la interfaz `Menu`                           |
| `Menu`           | Ventana principal con menú, imagen y lógica de interacción           |
| `Operaciones`    | Procesamiento de imágenes (filtros, transformaciones, morfología)    |
| `HistogramaPanel`| Panel que dibuja gráficamente un histograma de intensidades          |

---

## 🎨 Interfaz Gráfica (`Menu`)
Se trata de una **ventana Swing `JFrame`** con un menú rico en funcionalidades:

**Menús disponibles:**
- **Archivo**: Abrir, guardar, guardar como, cerrar
- **Edición**: Deshacer, rehacer
- **Imagen**: Escala de grises, binarización, negativo, histograma
- **Filtros**:
  - Suavizado: Media, Mediana
  - Realce: Sobel, Prewitt, Laplaciano
  - Morfológicos: Erosión, Dilatación, Apertura, Cierre, Esqueletonización
  - Frecuencia: Filtro paso bajo, alto y banda

💡 El uso de `JMenuBar`, `JMenu`, `JMenuItem`, `ActionListener` está bien organizado.

Incluye soporte para:
- Redimensionar la imagen al tamaño del panel
- Pila de deshacer/rehacer con `Stack<BufferedImage>`

---

## 🧪 Procesamiento de Imagen (`Operaciones`)
Contiene todos los algoritmos implementados:

### 🔧 Operaciones Básicas
- Escala de grises
- Negativo
- Imagen binaria (según umbral)

### 📊 Histograma
- Cálculo de histograma
- Visualización con `HistogramaPanel`

### ✨ Filtros de suavizado
- Media: con máscara de tamaño arbitrario
- Mediana: igual, usando ordenamiento por canal

### 🪓 Filtros de bordes
- **Sobel** y **Prewitt**: detección de bordes por convolución
- **Laplaciano**: detección de bordes mediante segunda derivada

### ⚙️ Morfología Binaria
- Erosión y dilatación 3x3
- Apertura = erosión + dilatación
- Cierre = dilatación + erosión
- Esqueletonización: por diferencias morfológicas iterativas

### 🌀 Filtros de frecuencia
- Paso bajo: usando filtro de media
- Paso alto: original - difuminado
- Paso banda: simulado, retornando imagen sin procesar (mejorable)

---

## 📊 Histograma (`HistogramaPanel`)
Clase que **extiende `JPanel`** y sobrescribe `paintComponent` para dibujar:
- Ejes X/Y
- Barras verdes representando la frecuencia
- Etiquetas en los ejes
- Títulos

Diseño cuidado y escalable. Muy visual.

---

## ⚙️ Detalles técnicos destacados

✅ Buen uso de programación funcional (`Function`, `@FunctionalInterface`)

✅ Uso de `BufferedImage` y manipulación a nivel de pixel

✅ Separación clara entre UI (`Menu`) y lógica (`Operaciones`)

✅ Manejo de errores con `JOptionPane`

✅ `Undo/Redo` correctamente implementado con copia profunda (`copyImage`)

✅ `ParallelStream` usado en algunos métodos como escala de grises para eficiencia

---

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

---

### 🎯 **Prepara la imagen antes del filtro**

| Filtro                  | Preprocesamiento recomendado                        | Caso de uso típico                                         | Qué observar 🔎                                         |
|------------------------|-----------------------------------------------------|------------------------------------------------------------|----------------------------------------------------------|
| **Escala de grises**   | Imagen a color con muchos tonos                     | Convertir imagen a una sola dimensión de intensidad        | Se eliminan colores, solo queda la información de luz    |
| **Binarización**       | Escala de grises                                   | Segmentar zonas claras/oscura (texto, formas, OCR)        | Todo se vuelve blanco o negro según umbral               |
| **Negativo**           | Cualquier imagen                                    | Efecto visual inverso, análisis médico o artístico         | Invierten luces y sombras; zonas claras ↔️ oscuras        |
| **Histograma**         | Escala de grises para más claridad                  | Ver distribución de intensidad; útil en contraste y umbral | Picos y valles indican concentración de valores grises   |
| **Filtro de media**    | Imagen con ruido o texturas                         | Suavizar imagen, difuminar zonas, reducción de ruido leve | Imagen se ve más borrosa, bordes pierden nitidez         |
| **Filtro de mediana**  | Imagen con "sal y pimienta", píxeles aislados      | Eliminar ruido impulsivo sin dañar bordes                 | Bordes preservados, ruido blanco/negro desaparece         |
| **Filtro Sobel**       | Escala de grises o binaria                          | Detectar bordes en direcciones diagonales                 | Se resaltan contornos fuertes                            |
| **Filtro Prewitt**     | Escala de grises o binaria                          | Alternativa a Sobel (más suave)                           | Bordes con algo menos de contraste                       |
| **Filtro Laplaciano**  | Escala de grises                                   | Detectar todos los bordes sin importar dirección          | Muestra contornos internos con alta sensibilidad         |
| **Erosión**            | Imagen binaria                                     | Eliminar ruido blanco, adelgazar objetos                  | Zonas blancas se achican; bordes retroceden              |
| **Dilatación**         | Imagen binaria                                     | Rellenar huecos, unir componentes                         | Zonas blancas se expanden                                |
| **Apertura**           | Imagen binaria con puntos blancos pequeños         | Suaviza contornos y elimina pequeños puntos aislados      | Formas más limpias, sin ruido blanco                     |
| **Cierre**             | Imagen binaria con huecos pequeños                 | Cierra agujeros dentro de objetos blancos                 | Huecos desaparecen, formas se rellenan                  |
| **Esqueletonización**  | Escala de grises → Binarización                    | Obtener forma delgada de objetos (análisis estructural)   | Figuras reducidas a su “esqueleto” o centro estructural  |
| **Filtro paso bajo**   | Imagen con texturas o ruido uniforme               | Suavizar o eliminar detalle fino                          | Imagen borrosa, menos detalle pero más homogénea         |
| **Filtro paso alto**   | Imagen con bajo contraste o detalles suaves        | Resaltar bordes o detalles finos                          | Realce de texturas, bordes más notorios                 |
| **Filtro paso banda**  | Imagen con detalles medios; aplicar post paso bajo | Ver estructura intermedia (ni ruido ni fondo plano)       | Texturas intermedias resaltadas; ni muy suaves ni duros |

---

## Documentación adicional 📄

Para mayor detalle sobre la **implementación** y la **teoría** de las operaciones de imagen, revisa el documento **[DocOperaciones.pdf](https://github.com/JoseDavila24/ImageProcessor2D/blob/d9a11d2c0cf3bf4678475188a5536f656083ef44/DocOperaciones.pdf)** incluido en el repositorio. Allí encontrarás descripciones más técnicas de cada filtro y su lógica interna (El codigo fuente de la documentacion esta en [Operaciones.tex](https://github.com/JoseDavila24/ImageProcessor2D/blob/d9a11d2c0cf3bf4678475188a5536f656083ef44/Operaciones.tex) ).

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

## Referencias

Jercyae. (n.d.). GitHub - jercyae7/editor-de-imagenes: manipulacion de imagenes. GitHub. https://github.com/jercyae7/editor-de-imagenes.git

Torres, A. D. (1996). Procesamiento digital de imágenes. Perfiles Educativos, 72. https://www.redalyc.org/pdf/132/13207206.pdf
