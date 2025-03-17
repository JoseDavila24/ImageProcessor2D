import java.awt.image.BufferedImage;
import java.util.Arrays;

public class OperacionesImagen {

    // Escala de grises
    public static BufferedImage escalaDeGrises(BufferedImage imagen) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage gris = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int rgb = imagen.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int promedio = (r + g + b) / 3;
                int grisRGB = (promedio << 16) | (promedio << 8) | promedio;
                gris.setRGB(x, y, grisRGB);
            }
        }
        return gris;
    }

    // Imagen binaria
    public static BufferedImage imagenBinaria(BufferedImage imagen, int umbral) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage binaria = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int rgb = imagen.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
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
        BufferedImage negativo = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int rgb = imagen.getRGB(x, y);
                int r = 255 - ((rgb >> 16) & 0xFF);
                int g = 255 - ((rgb >> 8) & 0xFF);
                int b = 255 - (rgb & 0xFF);
                int negativoRGB = (r << 16) | (g << 8) | b;
                negativo.setRGB(x, y, negativoRGB);
            }
        }
        return negativo;
    }
    
 // Ecualización del histograma
    public static BufferedImage ecualizarHistograma(BufferedImage imagen) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        // Calcular el histograma
        int[] histograma = new int[256];
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int rgb = imagen.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int promedio = (r + g + b) / 3;
                histograma[promedio]++;
            }
        }

        // Calcular la función de distribución acumulativa (CDF)
        int[] cdf = new int[256];
        cdf[0] = histograma[0];
        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + histograma[i];
        }

        // Normalizar el CDF
        int cdfMin = cdf[0];
        for (int i = 1; i < 256; i++) {
            if (cdf[i] < cdfMin && cdf[i] != 0) {
                cdfMin = cdf[i];
            }
        }

        int[] lut = new int[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = (int) (((cdf[i] - cdfMin) * 255.0) / (ancho * alto - cdfMin));
        }

        // Aplicar la transformación a la imagen
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int rgb = imagen.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int promedio = (r + g + b) / 3;
                int nuevoValor = lut[promedio];
                int nuevoRGB = (nuevoValor << 16) | (nuevoValor << 8) | nuevoValor;
                resultado.setRGB(x, y, nuevoRGB);
            }
        }

        return resultado;
    }

    // Filtro de media
    public static BufferedImage filtroMedia(BufferedImage imagen, int tamañoMascara) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        int offset = tamañoMascara / 2;
        for (int y = offset; y < alto - offset; y++) {
            for (int x = offset; x < ancho - offset; x++) {
                int r = 0, g = 0, b = 0;
                for (int ky = -offset; ky <= offset; ky++) {
                    for (int kx = -offset; kx <= offset; kx++) {
                        int rgb = imagen.getRGB(x + kx, y + ky);
                        r += (rgb >> 16) & 0xFF;
                        g += (rgb >> 8) & 0xFF;
                        b += rgb & 0xFF;
                    }
                }
                int count = tamañoMascara * tamañoMascara;
                r /= count;
                g /= count;
                b /= count;
                int nuevoRGB = (r << 16) | (g << 8) | b;
                resultado.setRGB(x, y, nuevoRGB);
            }
        }
        return resultado;
    }

    // Filtro de mediana
    public static BufferedImage filtroMediana(BufferedImage imagen, int tamañoMascara) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        int offset = tamañoMascara / 2;
        for (int y = offset; y < alto - offset; y++) {
            for (int x = offset; x < ancho - offset; x++) {
                int[] r = new int[tamañoMascara * tamañoMascara];
                int[] g = new int[tamañoMascara * tamañoMascara];
                int[] b = new int[tamañoMascara * tamañoMascara];
                int index = 0;
                for (int ky = -offset; ky <= offset; ky++) {
                    for (int kx = -offset; kx <= offset; kx++) {
                        int rgb = imagen.getRGB(x + kx, y + ky);
                        r[index] = (rgb >> 16) & 0xFF;
                        g[index] = (rgb >> 8) & 0xFF;
                        b[index] = rgb & 0xFF;
                        index++;
                    }
                }
                Arrays.sort(r);
                Arrays.sort(g);
                Arrays.sort(b);
                int mediana = tamañoMascara * tamañoMascara / 2;
                int nuevoRGB = (r[mediana] << 16) | (g[mediana] << 8) | b[mediana];
                resultado.setRGB(x, y, nuevoRGB);
            }
        }
        return resultado;
    }

    // Filtro de Sobel
    public static BufferedImage filtroSobel(BufferedImage imagen) {
        int[][] kernelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] kernelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
        return aplicarConvolucion(imagen, kernelX, kernelY);
    }

    // Filtro de Prewitt
    public static BufferedImage filtroPrewitt(BufferedImage imagen) {
        int[][] kernelX = {{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}};
        int[][] kernelY = {{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}};
        return aplicarConvolucion(imagen, kernelX, kernelY);
    }

    // Filtro de Laplaciano
    public static BufferedImage filtroLaplaciano(BufferedImage imagen) {
        int[][] kernel = {{0, -1, 0}, {-1, 4, -1}, {0, -1, 0}};
        return aplicarConvolucion(imagen, kernel, null);
    }

    // Aplicar convolución
    private static BufferedImage aplicarConvolucion(BufferedImage imagen, int[][] kernelX, int[][] kernelY) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < alto - 1; y++) {
            for (int x = 1; x < ancho - 1; x++) {
                int sumXr = 0, sumXg = 0, sumXb = 0;
                int sumYr = 0, sumYg = 0, sumYb = 0;
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int rgb = imagen.getRGB(x + kx, y + ky);
                        int r = (rgb >> 16) & 0xFF;
                        int g = (rgb >> 8) & 0xFF;
                        int b = rgb & 0xFF;
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
                int magnitudR = (int) Math.sqrt(sumXr * sumXr + sumYr * sumYr);
                int magnitudG = (int) Math.sqrt(sumXg * sumXg + sumYg * sumYg);
                int magnitudB = (int) Math.sqrt(sumXb * sumXb + sumYb * sumYb);
                magnitudR = Math.min(255, Math.max(0, magnitudR));
                magnitudG = Math.min(255, Math.max(0, magnitudG));
                magnitudB = Math.min(255, Math.max(0, magnitudB));
                int nuevoRGB = (magnitudR << 16) | (magnitudG << 8) | magnitudB;
                resultado.setRGB(x, y, nuevoRGB);
            }
        }
        return resultado;
    }

    // Erosión
    public static BufferedImage erosion(BufferedImage imagen) {
        return aplicarMorfologia(imagen, true);
    }

    // Dilatación
    public static BufferedImage dilatacion(BufferedImage imagen) {
        return aplicarMorfologia(imagen, false);
    }

    // Aplicar operación morfológica
    private static BufferedImage aplicarMorfologia(BufferedImage imagen, boolean esErosion) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < alto - 1; y++) {
            for (int x = 1; x < ancho - 1; x++) {
                int minMax = esErosion ? 255 : 0;
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int rgb = imagen.getRGB(x + kx, y + ky);
                        int gris = (rgb >> 16) & 0xFF;
                        if (esErosion) {
                            minMax = Math.min(minMax, gris);
                        } else {
                            minMax = Math.max(minMax, gris);
                        }
                    }
                }
                int nuevoRGB = (minMax << 16) | (minMax << 8) | minMax;
                resultado.setRGB(x, y, nuevoRGB);
            }
        }
        return resultado;
    }

    // Filtro de paso bajo (simulado)
    public static BufferedImage filtroPasoBajo(BufferedImage imagen) {
        return filtroMedia(imagen, 5);
    }

    // Filtro de paso alto (simulado)
    public static BufferedImage filtroPasoAlto(BufferedImage imagen) {
        BufferedImage resultado = new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                int rgb = imagen.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int nuevoRGB = (r << 16) | (g << 8) | b;
                resultado.setRGB(x, y, nuevoRGB);
            }
        }
        return resultado;
    }

    // Filtro de paso de banda (simulado)
    public static BufferedImage filtroPasoBanda(BufferedImage imagen) {
        return imagen;
    }
}