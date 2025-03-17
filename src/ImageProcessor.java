/**
 * Proyecto: Procesamiento de Imágenes 2D
 * 
 * Descripción:
 * Este programa es una herramienta de procesamiento de imágenes que permite realizar diversas operaciones
 * sobre imágenes en formato 2D. Incluye funcionalidades para la manipulación de colores, aplicación de filtros,
 * operaciones morfológicas y análisis de imágenes. La interfaz gráfica facilita la interacción con el usuario,
 * permitiendo cargar, procesar y guardar imágenes de manera intuitiva.
 * 
 * Estructura del programa:
 * 1. Clase `OperacionesImagen`: Contiene todas las operaciones de procesamiento de imágenes, como filtros,
 *    transformaciones y operaciones morfológicas.
 * 2. Clase `ColorConverter`: Implementa conversiones entre diferentes modelos de color (RGB, HSV, HSL, etc.).
 * 3. Clase `ImageProcessor`: Es la interfaz gráfica principal que permite cargar, procesar y guardar imágenes.
 *    También incluye un sistema de deshacer/rehacer para revertir o reaplicar operaciones.
 * 4. Clase `Main`: Punto de entrada del programa, inicia la interfaz gráfica.
 * 
 * Funcionalidades principales:
 * - Archivo:
 *   - Abrir, guardar y cerrar imágenes.
 * - Edición:
 *   - Deshacer y rehacer operaciones.
 * - Imagen:
 *   - Convertir entre modelos de color (RGB, HSI, HSL, HSV, CMYK).
 *   - Escala de grises, imagen binaria, negativo y generación de histograma.
 * - Filtros:
 *   - Suavizado y reducción de ruido (media, mediana).
 *   - Realce de bordes (Sobel, Prewitt, Laplaciano).
 *   - Filtros morfológicos (erosión, dilatación, apertura, cierre, esqueletonización).
 *   - Operaciones con frecuencia (filtro de paso bajo, paso alto, paso de banda).
 * 
 * Uso:
 * 1. Abrir una imagen desde el menú "Archivo".
 * 2. Seleccionar una operación del menú "Imagen" o "Filtros".
 * 3. Guardar la imagen procesada desde el menú "Archivo".
 * 4. Utilizar las opciones de "Deshacer" y "Rehacer" para revertir o reaplicar operaciones.
 * 
 * Autor: Jose Maria Romero Davila
 * Versión: 1.0
 * Fecha: 10 Marzo 2025
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Dimension;
import java.util.Stack;

public class ImageProcessor extends JFrame {

    private static final long serialVersionUID = 1L;
	private JLabel imageLabel;
    private BufferedImage currentImage;
    private File currentFile;
    private JScrollPane scrollPane;
    private Stack<BufferedImage> undoStack = new Stack<>();
    private Stack<BufferedImage> redoStack = new Stack<>();

    public ImageProcessor() {
        setTitle("Procesamiento de Imágenes 2D");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear la barra de menú
        JMenuBar menuBar = new JMenuBar();

        // Menú "Archivo"
        JMenu archivoMenu = new JMenu("Archivo");
        JMenuItem abrirItem = new JMenuItem("Abrir imagen");
        JMenuItem guardarItem = new JMenuItem("Guardar imagen");
        JMenuItem guardarComoItem = new JMenuItem("Guardar como...");
        JMenuItem cerrarItem = new JMenuItem("Cerrar");

        // Menú "Edición"
        JMenu edicionMenu = new JMenu("Edición");
        JMenuItem deshacerItem = new JMenuItem("Deshacer");
        JMenuItem rehacerItem = new JMenuItem("Rehacer");

        // Menú "Imagen"
        JMenu imagenMenu = new JMenu("Imagen");

        // Submenú "Modelo de color"
        JMenu modeloColorMenu = new JMenu("Modelo de color");
        JMenuItem rgbItem = new JMenuItem("RGB");
        JMenuItem hsiItem = new JMenuItem("HSI");
        JMenuItem hslItem = new JMenuItem("HSL");
        JMenuItem hsvItem = new JMenuItem("HSV");
        JMenuItem cmykItem = new JMenuItem("CMYK");

        // Opciones del menú "Imagen"
        JMenuItem escalaGrisesItem = new JMenuItem("Escala de grises");
        JMenuItem imagenBinariaItem = new JMenuItem("Imagen binaria (blanco / negro)");
        JMenuItem negativoItem = new JMenuItem("Negativo");
        JMenuItem histogramaItem = new JMenuItem("Histograma");

        // Menú "Filtros"
        JMenu filtrosMenu = new JMenu("Filtros");

        // Submenú "Suavizado y reducción de ruido"
        JMenu suavizadoMenu = new JMenu("Suavizado y reducción de ruido");
        JMenuItem mediaItem = new JMenuItem("Media");
        JMenuItem medianaItem = new JMenuItem("Mediana");

        // Submenú "Realce"
        JMenu realceMenu = new JMenu("Realce");

        // Submenú "Detección de bordes"
        JMenu deteccionBordesMenu = new JMenu("Detección de bordes");
        JMenuItem sobelItem = new JMenuItem("Sobel");
        JMenuItem prewittItem = new JMenuItem("Prewitt");
        JMenuItem laplacianoItem = new JMenuItem("Laplaciano");

        // Submenú "Filtros morfológicos (Imágenes binarias)"
        JMenu filtrosMorfologicosMenu = new JMenu("Filtros morfológicos (Imágenes binarias)");
        JMenuItem erosionItem = new JMenuItem("Erosión");
        JMenuItem dilatacionItem = new JMenuItem("Dilatación");
        JMenuItem aperturaItem = new JMenuItem("Apertura");
        JMenuItem cierreItem = new JMenuItem("Cierre");
        JMenuItem esqueletonizacionItem = new JMenuItem("Esqueletonización");

        // Submenú "Operaciones con frecuencia"
        JMenu operacionesFrecuenciaMenu = new JMenu("Operaciones con frecuencia");
        JMenuItem pasoBajoItem = new JMenuItem("Filtro de paso bajo");
        JMenuItem pasoAltoItem = new JMenuItem("Filtro de paso alto");
        JMenuItem pasoBandaItem = new JMenuItem("Filtro de paso de banda");

        // Añadir elementos al menú "Archivo"
        archivoMenu.add(abrirItem);
        archivoMenu.add(guardarItem);
        archivoMenu.add(guardarComoItem);
        archivoMenu.addSeparator();
        archivoMenu.add(cerrarItem);

        // Añadir elementos al menú "Edición"
        edicionMenu.add(deshacerItem);
        edicionMenu.add(rehacerItem);

        // Añadir elementos al menú "Modelo de color"
        modeloColorMenu.add(rgbItem);
        modeloColorMenu.add(hsiItem);
        modeloColorMenu.add(hslItem);
        modeloColorMenu.add(hsvItem);
        modeloColorMenu.add(cmykItem);

        // Añadir elementos al menú "Imagen"
        imagenMenu.add(modeloColorMenu);
        imagenMenu.addSeparator();
        imagenMenu.add(escalaGrisesItem);
        imagenMenu.add(imagenBinariaItem);
        imagenMenu.add(negativoItem);
        imagenMenu.add(histogramaItem);

        // Añadir elementos al menú "Suavizado y reducción de ruido"
        suavizadoMenu.add(mediaItem);
        suavizadoMenu.add(medianaItem);

        // Añadir elementos al menú "Detección de bordes"
        deteccionBordesMenu.add(sobelItem);
        deteccionBordesMenu.add(prewittItem);
        deteccionBordesMenu.add(laplacianoItem);

        // Añadir elementos al menú "Realce"
        realceMenu.add(deteccionBordesMenu);

        // Añadir elementos al menú "Filtros morfológicos"
        filtrosMorfologicosMenu.add(erosionItem);
        filtrosMorfologicosMenu.add(dilatacionItem);
        filtrosMorfologicosMenu.add(aperturaItem);
        filtrosMorfologicosMenu.add(cierreItem);
        filtrosMorfologicosMenu.add(esqueletonizacionItem);

        // Añadir elementos al menú "Operaciones con frecuencia"
        operacionesFrecuenciaMenu.add(pasoBajoItem);
        operacionesFrecuenciaMenu.add(pasoAltoItem);
        operacionesFrecuenciaMenu.add(pasoBandaItem);

        // Añadir elementos al menú "Filtros"
        filtrosMenu.add(suavizadoMenu);
        filtrosMenu.add(realceMenu);
        filtrosMenu.add(filtrosMorfologicosMenu);
        filtrosMenu.add(operacionesFrecuenciaMenu);

        // Añadir los menús a la barra de menú
        menuBar.add(archivoMenu);
        menuBar.add(edicionMenu);
        menuBar.add(imagenMenu);
        menuBar.add(filtrosMenu);

        // Añadir la barra de menú a la ventana
        setJMenuBar(menuBar);

        // Crear un JLabel para mostrar la imagen
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // Crear un JScrollPane para contener el JLabel
        scrollPane = new JScrollPane(imageLabel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Añadir el JScrollPane a la ventana
        add(scrollPane);

        // Configurar acciones para los elementos del menú "Archivo"
        abrirItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirImagen();
            }
        });

        guardarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarImagen();
            }
        });

        guardarComoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarComoImagen();
            }
        });

        cerrarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarImagen();
            }
        });

        // Configurar acciones para los elementos del menú "Edición"
        deshacerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deshacer();
            }
        });

        rehacerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rehacer();
            }
        });

        // Configurar acciones para los elementos del menú "Imagen"
        escalaGrisesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarEscalaDeGrises();
            }
        });

        imagenBinariaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarImagenBinaria();
            }
        });

        negativoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarNegativo();
            }
        });

        histogramaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarHistograma();
            }
        });

        // Configurar acciones para los elementos del menú "Filtros"
        mediaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltroMedia();
            }
        });

        medianaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltroMediana();
            }
        });

        sobelItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltroSobel();
            }
        });

        prewittItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltroPrewitt();
            }
        });

        laplacianoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltroLaplaciano();
            }
        });

        erosionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarErosion();
            }
        });

        dilatacionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarDilatacion();
            }
        });

        pasoBajoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltroPasoBajo();
            }
        });

        pasoAltoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltroPasoAlto();
            }
        });

        pasoBandaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltroPasoBanda();
            }
        });
    }

    private void abrirImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                currentFile = fileChooser.getSelectedFile();
                currentImage = ImageIO.read(currentFile);
                escalarImagen();
                scrollPane.revalidate();
                scrollPane.repaint();
                undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
                redoStack.clear(); // Limpiar la pila de rehacer
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarImagen() {
        if (currentImage != null) {
            try {
                ImageIO.write(currentImage, "png", currentFile);
                JOptionPane.showMessageDialog(this, "Imagen guardada correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void guardarComoImagen() {
        if (currentImage != null) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fileChooser.getSelectedFile();
                    ImageIO.write(currentImage, "png", file);
                    JOptionPane.showMessageDialog(this, "Imagen guardada como: " + file.getName());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al guardar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cerrarImagen() {
        imageLabel.setIcon(null);
        imageLabel.setText("No hay imagen abierta");
        currentImage = null;
        currentFile = null;
        undoStack.clear();
        redoStack.clear();
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    private void deshacer() {
        if (!undoStack.isEmpty()) {
            redoStack.push(copyImage(currentImage)); // Guardar en la pila de rehacer
            currentImage = undoStack.pop(); // Restaurar la imagen anterior
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay acciones para deshacer.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void rehacer() {
        if (!redoStack.isEmpty()) {
            undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
            currentImage = redoStack.pop(); // Restaurar la imagen siguiente
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay acciones para rehacer.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarEscalaDeGrises() {
        if (currentImage != null) {
            undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
            currentImage = OperacionesImagen.escalaDeGrises(currentImage);
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarImagenBinaria() {
        if (currentImage != null) {
            String umbralStr = JOptionPane.showInputDialog(this, "Introduce el umbral (0-255):", "128");
            try {
                int umbral = Integer.parseInt(umbralStr);
                undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
                currentImage = OperacionesImagen.imagenBinaria(currentImage, umbral);
                escalarImagen();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Umbral inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarNegativo() {
        if (currentImage != null) {
            undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
            currentImage = OperacionesImagen.negativo(currentImage);
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarHistograma() {
        if (currentImage != null) {
            undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
            currentImage = OperacionesImagen.ecualizarHistograma(currentImage);
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarFiltroMedia() {
        if (currentImage != null) {
            String tamañoStr = JOptionPane.showInputDialog(this, "Introduce el tamaño de la máscara (3, 5, 7, ...):", "3");
            try {
                int tamaño = Integer.parseInt(tamañoStr);
                undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
                currentImage = OperacionesImagen.filtroMedia(currentImage, tamaño);
                escalarImagen();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Tamaño inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarFiltroMediana() {
        if (currentImage != null) {
            String tamañoStr = JOptionPane.showInputDialog(this, "Introduce el tamaño de la máscara (3, 5, 7, ...):", "3");
            try {
                int tamaño = Integer.parseInt(tamañoStr);
                undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
                currentImage = OperacionesImagen.filtroMediana(currentImage, tamaño);
                escalarImagen();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Tamaño inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarFiltroSobel() {
        if (currentImage != null) {
            undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
            currentImage = OperacionesImagen.filtroSobel(currentImage);
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarFiltroPrewitt() {
        if (currentImage != null) {
            undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
            currentImage = OperacionesImagen.filtroPrewitt(currentImage);
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarFiltroLaplaciano() {
        if (currentImage != null) {
            undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
            currentImage = OperacionesImagen.filtroLaplaciano(currentImage);
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarErosion() {
        if (currentImage != null) {
            undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
            currentImage = OperacionesImagen.erosion(currentImage);
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarDilatacion() {
        if (currentImage != null) {
            undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
            currentImage = OperacionesImagen.dilatacion(currentImage);
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarFiltroPasoBajo() {
        if (currentImage != null) {
            undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
            currentImage = OperacionesImagen.filtroPasoBajo(currentImage);
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarFiltroPasoAlto() {
        if (currentImage != null) {
            undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
            currentImage = OperacionesImagen.filtroPasoAlto(currentImage);
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarFiltroPasoBanda() {
        if (currentImage != null) {
            undoStack.push(copyImage(currentImage)); // Guardar en la pila de deshacer
            currentImage = OperacionesImagen.filtroPasoBanda(currentImage);
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen abierta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private BufferedImage copyImage(BufferedImage source) {
        BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        copy.getGraphics().drawImage(source, 0, 0, null);
        return copy;
    }

    private void escalarImagen() {
        if (currentImage != null) {
            Dimension scrollPaneSize = scrollPane.getViewport().getSize();
            double imageAspectRatio = (double) currentImage.getWidth() / currentImage.getHeight();
            double scrollPaneAspectRatio = (double) scrollPaneSize.width / scrollPaneSize.height;

            int newWidth, newHeight;
            if (imageAspectRatio > scrollPaneAspectRatio) {
                newWidth = scrollPaneSize.width;
                newHeight = (int) (newWidth / imageAspectRatio);
            } else {
                newHeight = scrollPaneSize.height;
                newWidth = (int) (newHeight * imageAspectRatio);
            }

            Image scaledImage = currentImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            imageLabel.setText("");
        }
    }
}