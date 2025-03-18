# Documentación de Operaciones de Procesamiento de Imágenes

Este documento describe las **operaciones matemáticas** que se realizan en cada uno de los métodos de la clase `Operaciones`. Cada sección explica la **fórmula** o el **procedimiento** matemático que subyace a la implementación, usando notación LaTeX de forma clara y legible.

---

## 1. Escala de Grises

**Método:** `escalaDeGrises(BufferedImage imagen)`

> **Idea principal**  
> Convertir un color (RGB) a un tono de gris utilizando el promedio de los canales de color.

### Fórmula usada

\[
\text{gray} = \frac{R + G + B}{3}
\]

Cada píxel resultante \((r_{\text{out}}, g_{\text{out}}, b_{\text{out}})\) se define como:

\[
r_{\text{out}} = g_{\text{out}} = b_{\text{out}} = \text{gray}
\]

---

## 2. Imagen Binaria

**Método:** `imagenBinaria(BufferedImage imagen, int umbral)`

> **Idea principal**  
> Se convierte cada píxel a binario (0 o 255) según un umbral fijo.

### Fórmula usada

1. **Cálculo del promedio** (similar al de escala de grises):

\[
\text{gray} = \frac{R + G + B}{3}
\]

2. **Asignación binaria** según el umbral \(\text{umbral}\):

\[
\text{pixelBinario} =
\begin{cases}
255, & \text{si } \text{gray} > \text{umbral},\\
0,   & \text{en otro caso}.
\end{cases}
\]

---

## 3. Negativo

**Método:** `negativo(BufferedImage imagen)`

> **Idea principal**  
> Invertir cada componente de color restándolo de 255.

### Fórmula usada

Dado un píxel con valores \((R, G, B)\), el resultado es:

\[
R_{\text{neg}} = 255 - R, \quad
G_{\text{neg}} = 255 - G, \quad
B_{\text{neg}} = 255 - B
\]

---

## 4. Ecualización de Histograma (en Escala de Grises)

**Método:** `ecualizarHistogramaGrises(BufferedImage imagen)`

> **Idea principal**  
> Distribuir mejor los niveles de gris de la imagen para usar todo el rango dinámico \([0..255]\).

### Pasos matemáticos

1. **Cálculo del histograma**:

\[
\text{histograma}[i] = \#\{\text{píxeles con nivel } i\}, 
\quad i \in [0..255].
\]

   Aquí \(i\) representa el nivel de gris \(\frac{R + G + B}{3}\).

2. **Cálculo de la función de distribución acumulada (CDF)**:

\[
\text{cdf}[i] = \sum_{j=0}^{i} \text{histograma}[j]
\]

3. **Búsqueda de \(\text{cdfMin}\)** (primer valor de \(\text{cdf}\) > 0):

\[
\text{cdfMin} = \min \bigl\{ \text{cdf}[i] \mid \text{cdf}[i] > 0 \bigr\}
\]

4. **Cálculo de la tabla de búsqueda (LUT)**:

Sea \(N\) el total de píxeles (\(\text{ancho} \times \text{alto}\)):

\[
\text{lut}[i] 
= \text{round}\!\Bigl(
   \frac{(\text{cdf}[i] - \text{cdfMin}) \times 255}{N - \text{cdfMin}}
\Bigr)
\]

5. **Aplicación de la LUT** a cada píxel \(\bigl(\text{gray} \to \text{lut}[\text{gray}]\bigr)\).

---

## 5. Filtro de Suavizado (Promedio)

**Método:** `filtroMedia(BufferedImage imagen, int tamañoMascara)`

> **Idea principal**  
> Para cada píxel, se promedian los valores de su vecindario de tamaño \(\text{tamañoMascara} \times \text{tamañoMascara}\).

### Fórmula

Sea \(M = \text{tamañoMascara}\). Para un píxel \((x,y)\), su valor resultante \(R'(x,y)\) (aplicado al canal R; análogo para G y B) es:

\[
R'(x,y) 
= \frac{1}{M^2}
  \sum_{u=-\frac{M-1}{2}}^{\frac{M-1}{2}}
  \sum_{v=-\frac{M-1}{2}}^{\frac{M-1}{2}}
  R(x+u,y+v)
\]

donde se aplica **clamp** en los bordes (si \(x+u\) sale del rango de la imagen, se limita a los bordes).

---

## 6. Filtro de Suavizado (Mediana)

**Método:** `filtroMediana(BufferedImage imagen, int tamañoMascara)`

> **Idea principal**  
> En lugar de promediar, se ordenan los valores del vecindario y se elige la mediana.

### Procedimiento

1. Se recogen todos los valores (R, G, B) de la ventana \(\text{tamañoMascara} \times \text{tamañoMascara}\).
2. Se **ordenan**.
3. Se elige el valor de la **posición central** (mediana).

Si la ventana es de tamaño \(M\), esta contiene \(M^2\) valores. La mediana se halla en el índice \(\left\lfloor \tfrac{M^2}{2} \right\rfloor\) (en base 0) tras el ordenamiento.

---

## 7. Filtros de Detección de Bordes

Estos filtros utilizan **operadores de convolución** para realzar discontinuidades en la imagen.

### 7.1 Filtro Sobel

**Método:** `filtroSobel(BufferedImage imagen)`

**Máscaras (kernels)**:

\[
\text{kernelX} = 
\begin{bmatrix}
-1 & 0 & 1 \\
-2 & 0 & 2 \\
-1 & 0 & 1
\end{bmatrix}, 
\quad
\text{kernelY} = 
\begin{bmatrix}
-1 & -2 & -1 \\
0 & 0 & 0 \\
1 & 2 & 1
\end{bmatrix}
\]

Para cada canal (R, G, B), se calcula:

\[
G_x = \sum_{i=-1}^{1} \sum_{j=-1}^{1} 
      \bigl[\,I(x+i,y+j) \times \text{kernelX}[i+1][j+1]\bigr]
\]

\[
G_y = \sum_{i=-1}^{1} \sum_{j=-1}^{1} 
      \bigl[\,I(x+i,y+j) \times \text{kernelY}[i+1][j+1]\bigr]
\]

La **magnitud** del gradiente es:

\[
G = \sqrt{G_x^2 + G_y^2}
\]

(Con un `clamp` final a \([0,255]\)).

---

### 7.2 Filtro Prewitt

**Método:** `filtroPrewitt(BufferedImage imagen)`

**Máscaras (kernels)**:

\[
\text{kernelX} = 
\begin{bmatrix}
-1 & 0 & 1 \\
-1 & 0 & 1 \\
-1 & 0 & 1
\end{bmatrix},
\quad
\text{kernelY} =
\begin{bmatrix}
-1 & -1 & -1 \\
0 & 0 & 0 \\
1 & 1 & 1
\end{bmatrix}
\]

El proceso de convolución es análogo al de Sobel, y la magnitud se halla como:

\[
G = \sqrt{G_x^2 + G_y^2}
\]

---

### 7.3 Filtro Laplaciano

**Método:** `filtroLaplaciano(BufferedImage imagen)`

**Máscara (kernel)**:

\[
\text{kernel} = 
\begin{bmatrix}
0 & -1 & 0 \\
-1 & 4 & -1 \\
0 & -1 & 0
\end{bmatrix}
\]

La convolución es:

\[
L = \sum_{i=-1}^{1} \sum_{j=-1}^{1}
    \bigl[\,I(x+i,y+j) \times \text{kernel}[i+1][j+1]\bigr]
\]

(Con `clamp` final a \([0,255]\)).

---

## 8. Morfología

**Método principal:** `morfologia(BufferedImage imagen, int[][] structElem, boolean esErosion)`

Se aplican **operaciones morfológicas** (erosión y dilatación) usando un **elemento estructurante** (\(\text{structElem}\)).

### 8.1 Erosión

**Método:** `erosion3x3(BufferedImage imagen)`

- El elemento estructurante es de 3x3 con todo 1.
- Para cada píxel, se toma el **mínimo** dentro de la región donde \(\text{structElem} = 1\).

### 8.2 Dilatación

**Método:** `dilatacion3x3(BufferedImage imagen)`

- El elemento estructurante es de 3x3 con todo 1.
- Para cada píxel, se toma el **máximo** dentro de la región donde \(\text{structElem} = 1\).

### Fórmulas generales

Sea \(A\) la imagen y \(B\) el elemento estructurante:

1. **Erosión** \((A \ominus B)\):

\[
(A \ominus B)(x) = \min_{\,b \in B} \;A(x + b)
\]

2. **Dilatación** \((A \oplus B)\):

\[
(A \oplus B)(x) = \max_{\,b \in B} \;A(x - b)
\]

En la implementación digital, se realiza **clamp** en los bordes de la imagen y se toma en cuenta que \(b \in B\) significa aquellos píxeles donde \(B\) vale 1.

---

## 9. Filtros "Simulados" de Frecuencia

### 9.1 Filtro Paso Bajo

**Método:** `filtroPasoBajo(BufferedImage imagen)`

Se usa un **filtro de media** (5x5) para atenuar las frecuencias altas.

### 9.2 Filtro Paso Alto

**Método:** `filtroPasoAlto(BufferedImage imagen)`

Se calcula la diferencia entre la imagen original y una imagen suavizada, y luego se suma un offset (128) para centrar los valores en \([0..255]\).

#### Fórmula

Sea \(I\) la imagen original y \(B\) la imagen suavizada (por ejemplo, con un filtro de media 3x3). Entonces, el píxel resultante \(H\) es:

\[
H = I - B + 128
\]

(Con `clamp` final a [0..255]).

---

## 10. Referencias Generales

- Gonzalez, R. C., & Woods, R. E. (2008). *Digital Image Processing*. Prentice Hall.  
- Jain, A. K. (1989). *Fundamentals of Digital Image Processing*. Prentice Hall.

En cada caso, la implementación en Java realiza estas operaciones directamente sobre la matriz de píxeles (`BufferedImage`), controlando bordes con “clamp” y usando métodos auxiliares para extraer y componer valores RGB.

---
