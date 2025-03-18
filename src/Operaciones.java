import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Operaciones {

    /**
     * Utilidad para extraer y componer componentes de color
     * evitando la repetición de código.
     */
    private static class ColorUtils {
        static int getR(int rgb) { return (rgb >> 16) & 0xFF; }
        static int getG(int rgb) { return (rgb >> 8) & 0xFF; }
        static int getB(int rgb) { return rgb & 0xFF; }

        static int toRGB(int r, int g, int b) {
            r = clamp(r);
            g = clamp(g);
            b = clamp(b);
            return (r << 16) | (g << 8) | b;
        }

        static int clamp(int val) {
            if (val < 0) return 0;
            if (val > 255) return 255;
            return val;
        }
    }

    // --------------------------------------------------------
    // Operaciones básicas
    // --------------------------------------------------------

    // Escala de grises (promedio)
    public static BufferedImage escalaDeGrises(BufferedImage imagen) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage gris = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        // Usamos un stream paralelo opcional para mostrar cómo hacerlo. 
        // Podrías usar un simple for si no te interesa.
        IntStream.range(0, alto).parallel().forEach(y -> {
            for (int x = 0; x < ancho; x++) {
                int rgb = imagen.getRGB(x, y);
                int r = ColorUtils.getR(rgb);
                int g = ColorUtils.getG(rgb);
                int b = ColorUtils.getB(rgb);
                int promedio = (r + g + b) / 3;
                gris.setRGB(x, y, ColorUtils.toRGB(promedio, promedio, promedio));
            }
        });

        return gris;
    }

    // Imagen binaria
    public static BufferedImage imagenBinaria(BufferedImage imagen, int umbral) {
        umbral = Math.max(0, Math.min(255, umbral)); // clamp del umbral

        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage binaria = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int rgb = imagen.getRGB(x, y);
                int r = ColorUtils.getR(rgb);
                int g = ColorUtils.getG(rgb);
                int b = ColorUtils.getB(rgb);
                int promedio = (r + g + b) / 3;
                int binarioRGB = (promedio > umbral) ? 0xFFFFFF : 0x000000;
                binaria.setRGB(x, y, binarioRGB);
            }
        }
        return binaria;
    }

    // Negativo
    public static BufferedImage negativo(BufferedImage imagen) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage neg = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int rgb = imagen.getRGB(x, y);
                int r = 255 - ColorUtils.getR(rgb);
                int g = 255 - ColorUtils.getG(rgb);
                int b = 255 - ColorUtils.getB(rgb);
                neg.setRGB(x, y, ColorUtils.toRGB(r, g, b));
            }
        }
        return neg;
    }

    // --------------------------------------------------------
    // Ecualización de histograma (en escala de grises)
    // --------------------------------------------------------
    public static BufferedImage ecualizarHistogramaGrises(BufferedImage imagen) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        // Calcular histograma
        int[] histograma = new int[256];
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int rgb = imagen.getRGB(x, y);
                int r = ColorUtils.getR(rgb);
                int g = ColorUtils.getG(rgb);
                int b = ColorUtils.getB(rgb);
                int gris = (r + g + b) / 3;
                histograma[gris]++;
            }
        }

        // CDF
        int[] cdf = new int[256];
        cdf[0] = histograma[0];
        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + histograma[i];
        }

        // Buscar cdfMin > 0
        int cdfMin = 0;
        for (int i = 0; i < 256; i++) {
            if (cdf[i] != 0) {
                cdfMin = cdf[i];
                break;
            }
        }

        // LUT (Look-Up Table)
        int totalPixeles = ancho * alto;
        int[] lut = new int[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = Math.round(((cdf[i] - cdfMin) * 255f) / (totalPixeles - cdfMin));
            lut[i] = ColorUtils.clamp(lut[i]);
        }

        // Aplicar LUT
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int rgb = imagen.getRGB(x, y);
                int gris = (ColorUtils.getR(rgb) + ColorUtils.getG(rgb) + ColorUtils.getB(rgb)) / 3;
                int nuevo = lut[gris];
                resultado.setRGB(x, y, ColorUtils.toRGB(nuevo, nuevo, nuevo));
            }
        }

        return resultado;
    }

    // --------------------------------------------------------
    // Filtros de suavizado
    // --------------------------------------------------------
    public static BufferedImage filtroMedia(BufferedImage imagen, int tamañoMascara) {
        if (tamañoMascara < 1) {
            throw new IllegalArgumentException("El tamaño de la máscara debe ser >= 1");
        }
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        int offset = tamañoMascara / 2;
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int sumaR = 0, sumaG = 0, sumaB = 0;
                int count = 0;
                // Recorremos el vecindario con "clamp" en bordes
                for (int ky = -offset; ky <= offset; ky++) {
                    int ny = clampIndex(y + ky, 0, alto - 1);
                    for (int kx = -offset; kx <= offset; kx++) {
                        int nx = clampIndex(x + kx, 0, ancho - 1);
                        int rgb = imagen.getRGB(nx, ny);
                        sumaR += ColorUtils.getR(rgb);
                        sumaG += ColorUtils.getG(rgb);
                        sumaB += ColorUtils.getB(rgb);
                        count++;
                    }
                }
                int r = sumaR / count;
                int g = sumaG / count;
                int b = sumaB / count;
                resultado.setRGB(x, y, ColorUtils.toRGB(r, g, b));
            }
        }
        return resultado;
    }

    public static BufferedImage filtroMediana(BufferedImage imagen, int tamañoMascara) {
        if (tamañoMascara < 1) {
            throw new IllegalArgumentException("El tamaño de la máscara debe ser >= 1");
        }
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        int offset = tamañoMascara / 2;
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int[] rs = new int[tamañoMascara * tamañoMascara];
                int[] gs = new int[tamañoMascara * tamañoMascara];
                int[] bs = new int[tamañoMascara * tamañoMascara];
                int index = 0;
                for (int ky = -offset; ky <= offset; ky++) {
                    int ny = clampIndex(y + ky, 0, alto - 1);
                    for (int kx = -offset; kx <= offset; kx++) {
                        int nx = clampIndex(x + kx, 0, ancho - 1);
                        int rgb = imagen.getRGB(nx, ny);
                        rs[index] = ColorUtils.getR(rgb);
                        gs[index] = ColorUtils.getG(rgb);
                        bs[index] = ColorUtils.getB(rgb);
                        index++;
                    }
                }
                Arrays.sort(rs);
                Arrays.sort(gs);
                Arrays.sort(bs);
                int medianaPos = rs.length / 2;
                int r = rs[medianaPos];
                int g = gs[medianaPos];
                int b = bs[medianaPos];
                resultado.setRGB(x, y, ColorUtils.toRGB(r, g, b));
            }
        }
        return resultado;
    }

    // --------------------------------------------------------
    // Filtros de detección de bordes
    // --------------------------------------------------------
    public static BufferedImage filtroSobel(BufferedImage imagen) {
        int[][] kernelX = {
            {-1,  0,  1},
            {-2,  0,  2},
            {-1,  0,  1}
        };
        int[][] kernelY = {
            {-1, -2, -1},
            { 0,  0,  0},
            { 1,  2,  1}
        };
        return aplicarConvolucion(imagen, kernelX, kernelY);
    }

    public static BufferedImage filtroPrewitt(BufferedImage imagen) {
        int[][] kernelX = {
            {-1, 0, 1},
            {-1, 0, 1},
            {-1, 0, 1}
        };
        int[][] kernelY = {
            {-1, -1, -1},
            { 0,  0,  0},
            { 1,  1,  1}
        };
        return aplicarConvolucion(imagen, kernelX, kernelY);
    }

    public static BufferedImage filtroLaplaciano(BufferedImage imagen) {
        int[][] kernel = {
            { 0, -1,  0},
            {-1,  4, -1},
            { 0, -1,  0}
        };
        return aplicarConvolucion(imagen, kernel, null);
    }

    /**
     * Aplica convolución con dos kernels (X e Y). 
     * Si kernelY es null, se realiza solo la convolución con kernelX.
     */
    private static BufferedImage aplicarConvolucion(BufferedImage imagen, int[][] kernelX, int[][] kernelY) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int sumXr = 0, sumXg = 0, sumXb = 0;
                int sumYr = 0, sumYg = 0, sumYb = 0;

                // Kernel 3x3 => offset = 1
                for (int ky = -1; ky <= 1; ky++) {
                    int ny = clampIndex(y + ky, 0, alto - 1);
                    for (int kx = -1; kx <= 1; kx++) {
                        int nx = clampIndex(x + kx, 0, ancho - 1);
                        int rgb = imagen.getRGB(nx, ny);
                        int r = ColorUtils.getR(rgb);
                        int g = ColorUtils.getG(rgb);
                        int b = ColorUtils.getB(rgb);

                        if (kernelX != null) {
                            sumXr += r * kernelX[ky + 1][kx + 1];
                            sumXg += g * kernelX[ky + 1][kx + 1];
                            sumXb += b * kernelX[ky + 1][kx + 1];
                        }
                        if (kernelY != null) {
                            sumYr += r * kernelY[ky + 1][kx + 1];
                            sumYg += g * kernelY[ky + 1][kx + 1];
                            sumYb += b * kernelY[ky + 1][kx + 1];
                        }
                    }
                }

                // Magnitud (Sobel/Prewitt) o sólo sumXr para Laplaciano
                int rr = (kernelY == null)
                        ? sumXr
                        : (int) Math.sqrt(sumXr * sumXr + sumYr * sumYr);
                int gg = (kernelY == null)
                        ? sumXg
                        : (int) Math.sqrt(sumXg * sumXg + sumYg * sumYg);
                int bb = (kernelY == null)
                        ? sumXb
                        : (int) Math.sqrt(sumXb * sumXb + sumYb * sumYb);

                // Clamps
                rr = ColorUtils.clamp(rr);
                gg = ColorUtils.clamp(gg);
                bb = ColorUtils.clamp(bb);
                resultado.setRGB(x, y, ColorUtils.toRGB(rr, gg, bb));
            }
        }
        return resultado;
    }

    // --------------------------------------------------------
    // Morfología
    // --------------------------------------------------------
    /**
     * Aplica una operación morfológica binaria dada por un structElem y un modo (erosion/dilatacion).
     * Para píxeles fuera del rango, se usa clamp (bordes).
     *
     * @param imagen      Imagen de entrada (en escala de grises o binaria).
     * @param structElem  Matriz de 0/1 que define la forma del elemento estructurante.
     * @param esErosion   true para erosión, false para dilatación.
     */
    public static BufferedImage morfologia(BufferedImage imagen, int[][] structElem, boolean esErosion) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        int elemAncho = structElem[0].length;
        int elemAlto = structElem.length;
        int offX = elemAncho / 2;
        int offY = elemAlto / 2;

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                // Para erosión: arrancamos con 255 y buscaremos min
                // Para dilatación: arrancamos con 0 y buscaremos max
                int valorRef = esErosion ? 255 : 0;

                for (int sy = 0; sy < elemAlto; sy++) {
                    for (int sx = 0; sx < elemAncho; sx++) {
                        if (structElem[sy][sx] == 1) {
                            int nx = clampIndex(x + (sx - offX), 0, ancho - 1);
                            int ny = clampIndex(y + (sy - offY), 0, alto - 1);
                            int rgb = imagen.getRGB(nx, ny);
                            int gris = ColorUtils.getR(rgb); // en binario, R=G=B

                            if (esErosion) {
                                valorRef = Math.min(valorRef, gris);
                            } else {
                                valorRef = Math.max(valorRef, gris);
                            }
                        }
                    }
                }
                resultado.setRGB(x, y, ColorUtils.toRGB(valorRef, valorRef, valorRef));
            }
        }
        return resultado;
    }

    // Ejemplos: Erosión y Dilatación 3x3 con struct elem completo (todo 1)
    public static BufferedImage erosion3x3(BufferedImage imagen) {
        int[][] struct3x3 = {
            {1,1,1},
            {1,1,1},
            {1,1,1}
        };
        return morfologia(imagen, struct3x3, true);
    }

    public static BufferedImage dilatacion3x3(BufferedImage imagen) {
        int[][] struct3x3 = {
            {1,1,1},
            {1,1,1},
            {1,1,1}
        };
        return morfologia(imagen, struct3x3, false);
    }

    // --------------------------------------------------------
    // Filtros "simulados" de frecuencia
    // --------------------------------------------------------
    public static BufferedImage filtroPasoBajo(BufferedImage imagen) {
        // Tomamos un filtro de media 5x5 como "paso bajo" simple
        return filtroMedia(imagen, 5);
    }

    public static BufferedImage filtroPasoAlto(BufferedImage imagen) {
        // Ejemplo simple: restar un difuminado a la imagen original.
        // Aquí lo hacemos muy básico, o se puede dejar simulado.
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage blurred = filtroMedia(imagen, 3); // Difuminado leve
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int origRGB = imagen.getRGB(x, y);
                int blurRGB = blurred.getRGB(x, y);
                // High-pass = original - blur + 128 (para evitar negativos)
                int r = ColorUtils.getR(origRGB) - ColorUtils.getR(blurRGB) + 128;
                int g = ColorUtils.getG(origRGB) - ColorUtils.getG(blurRGB) + 128;
                int b = ColorUtils.getB(origRGB) - ColorUtils.getB(blurRGB) + 128;
                resultado.setRGB(x, y, ColorUtils.toRGB(r, g, b));
            }
        }
        return resultado;
    }

    public static BufferedImage filtroPasoBanda(BufferedImage imagen) {
        // Se deja tal cual en tu código original. Si quisieras
        // algo real, podrías combinar un paso bajo y luego un paso alto.
        return imagen;
    }

    // --------------------------------------------------------
    // Utilidades privadas
    // --------------------------------------------------------
    /**
     * Ajusta el índice para que no se salga de [min..max].
     */
    private static int clampIndex(int idx, int min, int max) {
        if (idx < min) return min;
        if (idx > max) return max;
        return idx;
    }
}