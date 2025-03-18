Aquí tienes un compendio de las **operaciones y fórmulas matemáticas** que aparecen en el código. Puedes copiar este texto en Word (o cualquier editor de texto) para conservar la información. He agrupado por secciones para reflejar la estructura principal del código:

---

## 1. Escala de grises (promedio)

\[
\text{Gris} = \frac{R + G + B}{3}
\]

Se calcula un valor promedio (entero) y se asigna la misma intensidad a cada componente para formar un píxel en escala de grises.

---

## 2. Imagen binaria

1. Se calcula el **promedio** de los tres canales:

\[
\text{promedio} = \frac{R + G + B}{3}
\]

2. Se compara el promedio con un **umbral** (threshold).  
   - Si \(\text{promedio} > \text{umbral}\), el píxel se vuelve **blanco** (\(255,255,255\)).  
   - Si \(\text{promedio} \le \text{umbral}\), el píxel se vuelve **negro** (\(0,0,0\)).

---

## 3. Negativo

Para cada componente (R, G, B) se aplica:

\[
R' = 255 - R, \quad G' = 255 - G, \quad B' = 255 - B
\]

---

## 4. Ecualización de histograma en grises

1. **Conversión a gris**:  
   \[
   \text{gris}(x, y) = \frac{R + G + B}{3}
   \]

2. **Histograma** \(\text{hist}[i]\): contador de cuántos píxeles tienen nivel de gris \(i\) (entre 0 y 255).

3. **CDF (función de distribución acumulada)**:

\[
\text{cdf}[0] = \text{hist}[0]
\]
\[
\text{cdf}[i] = \text{cdf}[i-1] + \text{hist}[i] \quad \text{para } i = 1, 2, \dots, 255
\]

4. **Búsqueda de \( \text{cdfMin} \)**: el primer valor de \(\text{cdf}[i]\) distinto de cero.

5. **LUT (Look-Up Table)**:

\[
\text{lut}[i] = \text{round} \Bigl( \frac{(\text{cdf}[i] - \text{cdfMin}) \times 255}{(\text{totalPixeles} - \text{cdfMin})} \Bigr)
\]

6. **Aplicación**:  
   \[
   \text{nuevoValor} = \text{lut}[\text{gris}(x,y)]
   \]

   Y se construye la nueva imagen aplicando este \(\text{nuevoValor}\) (clamp a [0..255] si hiciera falta).

---

## 5. Filtros de suavizado

### 5.1 Filtro de Media (promedio)

Para una **máscara de tamaño \(n \times n\)** se recorre cada píxel y se suma el valor de R, G, B de todos los vecinos en la ventana de la máscara. Si la máscara es de tamaño \(n\):

\[
\text{r} = \frac{\sum \text{R}_{\text{vecinos}}}{n \times n}, \quad 
\text{g} = \frac{\sum \text{G}_{\text{vecinos}}}{n \times n}, \quad 
\text{b} = \frac{\sum \text{B}_{\text{vecinos}}}{n \times n}
\]

### 5.2 Filtro de Mediana

1. Se toma la **ventana \(n \times n\)** alrededor de cada píxel.  
2. Se extraen los valores de R, G, B de todos los vecinos en **arrays separados**.  
3. Se ordenan (por ejemplo, con `Arrays.sort`) y se toma el valor central:

\[
\text{r} = \text{mediana}(\{R_{\text{vecinos}}\}), \quad 
\text{g} = \text{mediana}(\{G_{\text{vecinos}}\}), \quad 
\text{b} = \text{mediana}(\{B_{\text{vecinos}}\})
\]

---

## 6. Filtros de detección de bordes

Se aplican **operaciones de convolución** con distintos **kernels** (matrices 3x3).  
La fórmula genérica para un kernel \(K\) de 3x3 sobre el píxel \((x, y)\) es:

\[
\text{acumuladoR} = \sum_{i=-1}^{1} \sum_{j=-1}^{1} (R_{(x+j),(y+i)} \times K[i+1][j+1])
\]
\[
\text{acumuladoG} = \sum_{i=-1}^{1} \sum_{j=-1}^{1} (G_{(x+j),(y+i)} \times K[i+1][j+1])
\]
\[
\text{acumuladoB} = \sum_{i=-1}^{1} \sum_{j=-1}^{1} (B_{(x+j),(y+i)} \times K[i+1][j+1])
\]

Luego se hace **clamp** a [0..255].

### 6.1 Filtro Sobel

Dos kernels (para X y para Y):

\[
K_x = 
\begin{bmatrix}
-1 & 0 & 1 \\
-2 & 0 & 2 \\
-1 & 0 & 1
\end{bmatrix}, 
\quad
K_y =
\begin{bmatrix}
-1 & -2 & -1 \\
\;\,0 & \;\,0 & \;\,0 \\
\;\,1 & \;\,2 & \;\,1
\end{bmatrix}
\]

Tras calcular (sumXr, sumXg, sumXb) y (sumYr, sumYg, sumYb), se calcula la **magnitud**:

\[
\text{R'} = \sqrt{(\text{sumXr})^2 + (\text{sumYr})^2}
\]
\[
\text{G'} = \sqrt{(\text{sumXg})^2 + (\text{sumYg})^2}
\]
\[
\text{B'} = \sqrt{(\text{sumXb})^2 + (\text{sumYb})^2}
\]

### 6.2 Filtro Prewitt

Similar a Sobel, pero con otros kernels:

\[
K_x = 
\begin{bmatrix}
-1 & 0 & 1 \\
-1 & 0 & 1 \\
-1 & 0 & 1
\end{bmatrix},
\quad
K_y =
\begin{bmatrix}
-1 & -1 & -1 \\
\;\,0 & \;\,0 & \;\,0 \\
\;\,1 & \;\,1 & \;\,1
\end{bmatrix}
\]

### 6.3 Filtro Laplaciano

Solo un kernel (no hay X/Y separados).  
Ejemplo usado:

\[
K =
\begin{bmatrix}
\;\,0 & -1 & \;\,0 \\
-1 & \;\,4 & -1 \\
\;\,0 & -1 & \;\,0
\end{bmatrix}
\]

Se aplica la misma fórmula de convolución, sin magnitud de Sobel/Prewitt. El valor final es simplemente \(\text{sumXr}\) (y análogos para G y B).

---

## 7. Morfología (Erosión y Dilatación)

Se asume imagen en binario o grises. Para cada píxel \((x, y)\) y un **elemento estructurante** de tamaño \(m \times n\):

- **Erosión (\(\min\))**:  
  \[
  \text{valorRef} = \min \{\text{gris}(x+\Delta x,y+\Delta y) \mid \text{structElem}(\Delta x,\Delta y) = 1 \}
  \]

- **Dilatación (\(\max\))**:  
  \[
  \text{valorRef} = \max \{\text{gris}(x+\Delta x,y+\Delta y) \mid \text{structElem}(\Delta x,\Delta y) = 1 \}
  \]

Donde \(\Delta x, \Delta y\) recorren la zona cubierta por el elemento estructurante.

---

## 8. Filtros de “frecuencia” (simulados)

### 8.1 Filtro Paso Bajo
Se aplica un **difuminado** (por ejemplo, un filtro de media 5×5). Ver la **fórmula del Filtro de Media** en la Sección 5.1.

### 8.2 Filtro Paso Alto
Se resta una versión difuminada de la imagen a la imagen original y se ajusta con un offset (por ejemplo, +128) para no obtener valores negativos:

\[
\text{HP} = ( \text{Original} - \text{Blur} ) + 128
\]

Para cada componente:

\[
R' = R_{\text{original}} - R_{\text{blur}} + 128
\]
\[
G' = G_{\text{original}} - G_{\text{blur}} + 128
\]
\[
B' = B_{\text{original}} - B_{\text{blur}} + 128
\]

(Clampeando a [0..255] al final.)

### 8.3 Filtro Paso Banda
En el código, se deja como un **retorno directo** de la misma imagen (no se implementa un efecto real de paso banda).

---

## 9. Clampeo (Clamp)

En varios puntos se realiza un **clamp** para asegurar que los valores queden en \([0, 255]\).  
Si \(\text{val} < 0\), se pone \(\text{val} = 0\).  
Si \(\text{val} > 255\), se pone \(\text{val} = 255\).

---

### Nota adicional
- El método `clampIndex` se usa para asegurar que el índice de fila o columna no se salga de la imagen, fijándolo en el rango [0..(alto–1)] o [0..(ancho–1)].

¡Listo! Estos son los **detalles matemáticos** principales que se aplican en cada una de las operaciones del código Java. Puedes copiar y pegar este documento en Word para tu referencia.
