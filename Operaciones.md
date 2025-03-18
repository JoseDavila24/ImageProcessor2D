# Documentación de Operaciones de Procesamiento de Imágenes

Este documento describe las **operaciones matemáticas** que se realizan en cada uno de los métodos de la clase `Operaciones`. Cada sección explica la **fórmula** o el **procedimiento** matemático que subyace a la implementación.

---

## 1. Escala de Grises

**Método:** `escalaDeGrises(BufferedImage imagen)`

**Idea principal:**
Convertir un color (RGB) a un tono de gris utilizando el promedio de los canales de color.

### Fórmula usada

$$
\text{gray} = \frac{R + G + B}{3}
$$

Cada píxel resultante \((r_{\text{out}}, g_{\text{out}}, b_{\text{out}})\) se define como:

$$
r_{\text{out}} = g_{\text{out}} = b_{\text{out}} = \text{gray}
$$

---

## 2. Imagen Binaria

**Método:** `imagenBinaria(BufferedImage imagen, int umbral)`

**Idea principal:**
Se convierte cada píxel a binario (0 o 255) según un umbral fijo.

### Fórmula usada

1. Cálculo del promedio (similar al de escala de grises):

   $$
   \text{gray} = \frac{R + G + B}{3}
   $$

2. Asignación binaria según el umbral \(\text{umbral}\):

   $$
   \text{pixelBinario} =
   \begin{cases} 
     255, & \text{si } \text{gray} > \text{umbral} \\
     0,   & \text{en otro caso}
   \end{cases}
   $$

---

## 3. Negativo

**Método:** `negativo(BufferedImage imagen)`

**Idea principal:**
Invertir cada componente de color restándolo de 255.

### Fórmula usada

Dado un píxel con valores \((R, G, B)\), el resultado es:

$$
R_{\text{neg}} = 255 - R, \quad
G_{\text{neg}} = 255 - G, \quad
B_{\text{neg}} = 255 - B
$$

---

## 4. Ecualización de Histograma (en Escala de Grises)

**Método:** `ecualizarHistogramaGrises(BufferedImage imagen)`

**Idea principal:**
Distribuir mejor los niveles de gris de la imagen para usar todo el rango dinámico \([0..255]\).

### Pasos matemáticos

1. **Cálculo del histograma**:

   $$
   \text{histograma}[i] = \text{(número de píxeles con nivel } i), \quad i \in [0..255]
   $$

   Aquí \(i\) representa el nivel de gris \(\frac{R + G + B}{3}\).

2. **Cálculo de la función de distribución acumulada (CDF)**:

   $$
   \text{cdf}[i] = \sum_{j=0}^{i} \text{histograma}[j]
   $$

3. **Búsqueda de \(\text{cdfMin}\)** (primer valor de \(\text{cdf}\) > 0):

   $$
   \text{cdfMin} = \min \{ \text{cdf}[i] \mid \text{cdf}[i] > 0 \}
   $$

4. **Cálculo de la tabla de búsqueda (LUT)**:

   Sea \(N\) el total de píxeles (\(ancho \times alto\)):

   $$
   \text{lut}[i] = \text{round}\!\Bigl(\frac{(\text{cdf}[i] - \text{cdfMin}) \times 255}{N - \text{cdfMin}}\Bigr)
   $$

5. **Aplicación de la LUT** a cada píxel (\(\text{gray} \to \text{lut}[\text{gray}]\)).

---

## 5. Filtro de Suavizado (Promedio)

**Método:** `filtroMedia(BufferedImage imagen, int tamañoMascara)`

**Idea principal:**
Para cada píxel, se promedian los valores de su vecindario de tamaño \(\text{tamañoMascara} \times \text{tamañoMascara}\).

### Fórmula

Sea \(M = \text{tamañoMascara}\). Para un píxel \((x,y)\), su valor resultante \(R'(x,y)\) (aplicado al canal R; el procedimiento es igual para G y B) es:

$$
R'(x,y) = \frac{1}{M^2} \sum_{u=-\frac{M-1}{2}}^{\frac{M-1}{2}} \sum_{v=-\frac{M-1}{2}}^{\frac{M-1}{2}} R(x+u,y+v)
$$

donde se aplica **clamp** en los bordes (si \(x+u\) sale del rango de la imagen, se limita al mínimo o máximo permitido).

---

## 6. Filtro de Suavizado (Mediana)

**Método:** `filtroMediana(BufferedImage imagen, int tamañoMascara)`

**Idea principal:**
En lugar de promediar, se ordenan los valores del vecindario y se elige la mediana.

### Procedimiento

1. Se recogen todos los valores (R, G, B por separado) de la ventana \(\text{tamañoMascara} \times \text{tamañoMascara}\).
2. Se ordenan.
3. Se elige el valor de la posición central (mediana).

Si la máscara es de tamaño \(M\), tiene \(M^2\) valores. La **mediana** es el elemento en el índice \(\lfloor\frac{M^2}{2}\rfloor\) (en base 0) tras el ordenamiento.

---

## 7. Filtros de Detección de Bordes

Estos filtros utilizan **operadores de convolución** para realzar discontinuidades en la imagen.

### 7.1 Filtro Sobel

**Método:** `filtroSobel(BufferedImage imagen)`

**Máscaras (kernels)**:

$$
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
$$

Para cada canal (R, G, B), se calcula:

$$
G_x = \sum_{i=-1}^{1}\sum_{j=-1}^{1} I(x+i,y+j)\,\text{kernelX}[i+1][j+1]
$$

$$
G_y = \sum_{i=-1}^{1}\sum_{j=-1}^{1} I(x+i,y+j)\,\text{kernelY}[i+1][j+1]
$$

La magnitud del gradiente es:

$$
G = \sqrt{G_x^2 + G_y^2}
$$

(Con `clamp` final a \([0,255]\)).

---

### 7.2 Filtro Prewitt

**Método:** `filtroPrewitt(BufferedImage imagen)`

**Máscaras (kernels)**:

$$
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
$$

El proceso de cálculo es análogo al Sobel, con:

$$
G = \sqrt{G_x^2 + G_y^2}
$$

---

### 7.3 Filtro Laplaciano

**Método:** `filtroLaplaciano(BufferedImage imagen)`

**Máscara (kernel)**:

$$
\text{kernel} = 
\begin{bmatrix}
0 & -1 & 0 \\
-1 & 4 & -1 \\
0 & -1 & 0
\end{bmatrix}
$$

La convolución es:

$$
L = \sum_{i=-1}^{1}\sum_{j=-1}^{1} I(x+i,y+j)\,\text{kernel}[i+1][j+1]
$$

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

1. **Erosión** \(\ominus\):

   $$
   (A \ominus B)(x) = \min_{b \in B} A(x + b)
   $$

2. **Dilatación** \(\oplus\):

   $$
   (A \oplus B)(x) = \max_{b \in B} A(x - b)
   $$

En la implementación digital, se realiza “clamp” en los bordes de la imagen y se toma en cuenta que \(b\in B\) significa aquellos píxeles donde \(B\) vale 1.

---

## 9. Filtros "Simulados" de Frecuencia

### 9.1 Filtro Paso Bajo

**Método:** `filtroPasoBajo(BufferedImage imagen)`

Se usa un **filtro de media** de 5x5 para atenuar las frecuencias altas.

### 9.2 Filtro Paso Alto

**Método:** `filtroPasoAlto(BufferedImage imagen)`

Se calcula la diferencia entre la imagen original y una imagen suavizada, y luego se suma un offset (128) para centrar los valores en el rango [0..255].

### Fórmula

Sea \(I\) la imagen original y \(B\) la imagen suavizada (filtro de media). Entonces, el píxel resultante \(H\) es:

$$
H = I - B + 128
$$

(Con `clamp` final a [0..255]).
