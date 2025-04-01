import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Dimension;
import java.util.Stack;
import java.util.function.Function;

public class Menu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLabel imageLabel;
    private BufferedImage currentImage;
    private File currentFile;
    private JScrollPane scrollPane;
    private Stack<BufferedImage> undoStack = new Stack<>();
    private Stack<BufferedImage> redoStack = new Stack<>();

    public Menu() {
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

        // Submenú "Realce" y detección de bordes
        JMenu realceMenu = new JMenu("Realce");
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

        // Añadir elementos al submenú "Modelo de color"
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

        // Añadir elementos a "Suavizado y reducción de ruido"
        suavizadoMenu.add(mediaItem);
        suavizadoMenu.add(medianaItem);

        // Añadir elementos a "Detección de bordes"
        deteccionBordesMenu.add(sobelItem);
        deteccionBordesMenu.add(prewittItem);
        deteccionBordesMenu.add(laplacianoItem);

        // Añadir "Detección de bordes" al submenú "Realce"
        realceMenu.add(deteccionBordesMenu);

        // Añadir elementos al submenú "Filtros morfológicos"
        filtrosMorfologicosMenu.add(erosionItem);
        filtrosMorfologicosMenu.add(dilatacionItem);
        filtrosMorfologicosMenu.add(aperturaItem);
        filtrosMorfologicosMenu.add(cierreItem);
        filtrosMorfologicosMenu.add(esqueletonizacionItem);

        // Añadir elementos a "Operaciones con frecuencia"
        operacionesFrecuenciaMenu.add(pasoBajoItem);
        operacionesFrecuenciaMenu.add(pasoAltoItem);
        operacionesFrecuenciaMenu.add(pasoBandaItem);

        // Añadir submenús a "Filtros"
        filtrosMenu.add(suavizadoMenu);
        filtrosMenu.add(realceMenu);
        filtrosMenu.add(filtrosMorfologicosMenu);
        filtrosMenu.add(operacionesFrecuenciaMenu);

        // Añadir menús a la barra de menú
        menuBar.add(archivoMenu);
        menuBar.add(edicionMenu);
        menuBar.add(imagenMenu);
        menuBar.add(filtrosMenu);

        // Asignar barra de menú
        setJMenuBar(menuBar);

        // Crear JLabel para mostrar la imagen
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // Crear un JScrollPane para contener la imagen
        scrollPane = new JScrollPane(imageLabel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);

        // Listeners de menú "Archivo"
        abrirItem.addActionListener(e -> abrirImagen());
        guardarItem.addActionListener(e -> guardarImagen());
        guardarComoItem.addActionListener(e -> guardarComoImagen());
        cerrarItem.addActionListener(e -> cerrarImagen());

        // Listeners de menú "Edición"
        deshacerItem.addActionListener(e -> deshacer());
        rehacerItem.addActionListener(e -> rehacer());

        // Listeners de menú "Imagen"
        escalaGrisesItem.addActionListener(e -> aplicarEscalaDeGrises());
        imagenBinariaItem.addActionListener(e -> aplicarImagenBinaria());
        negativoItem.addActionListener(e -> aplicarNegativo());
        histogramaItem.addActionListener(e -> mostrarHistograma());

        // Listeners de menú "Filtros" (suavizado)
        mediaItem.addActionListener(e -> aplicarFiltroMedia());
        medianaItem.addActionListener(e -> aplicarFiltroMediana());

        // Detección de bordes
        sobelItem.addActionListener(e -> aplicarFiltroSobel());
        prewittItem.addActionListener(e -> aplicarFiltroPrewitt());
        laplacianoItem.addActionListener(e -> aplicarFiltroLaplaciano());

        // Filtros morfológicos
        erosionItem.addActionListener(e -> aplicarErosion());
        dilatacionItem.addActionListener(e -> aplicarDilatacion());
        // Apertura, cierre y esqueletonización aún sin implementar
        // (no se incluyeron en el código original), pero aquí podrías
        // añadirlos de forma similar.

        // Operaciones con frecuencia
        pasoBajoItem.addActionListener(e -> aplicarFiltroPasoBajo());
        pasoAltoItem.addActionListener(e -> aplicarFiltroPasoAlto());
        pasoBandaItem.addActionListener(e -> aplicarFiltroPasoBanda());

        aperturaItem.addActionListener(e -> aplicarApertura());
        cierreItem.addActionListener(e -> aplicarCierre());
        esqueletonizacionItem.addActionListener(e -> aplicarEsqueletonizacion());
    }

    /**
     * Abre una imagen desde el disco.
     */
    private void abrirImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                currentFile = fileChooser.getSelectedFile();
                currentImage = ImageIO.read(currentFile);
                if (currentImage != null) {
                    escalarImagen();
                    // Agregar imagen al stack de "undo" y limpiar "redo"
                    undoStack.push(copyImage(currentImage));
                    redoStack.clear();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al abrir la imagen: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Guarda la imagen en el fichero original (si existe).
     */
    private void guardarImagen() {
        if (!checkImageLoaded())
            return; // Sale si no hay imagen
        try {
            if (currentFile == null) {
                // Si no hay archivo asociado, pedir "Guardar como"
                guardarComoImagen();
            } else {
                ImageIO.write(currentImage, "png", currentFile);
                JOptionPane.showMessageDialog(this, "Imagen guardada correctamente.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar la imagen: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre un diálogo para guardar la imagen con otro nombre/ruta.
     */
    private void guardarComoImagen() {
        if (!checkImageLoaded())
            return;
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                ImageIO.write(currentImage, "png", file);
                // Actualizar currentFile si deseas seguir guardando en esa ruta
                currentFile = file;
                JOptionPane.showMessageDialog(this, "Imagen guardada como: " + file.getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar la imagen: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Cierra la imagen actual, limpiando stacks y la etiqueta.
     */
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

    /**
     * Deshace la última operación, si es posible.
     */
    private void deshacer() {
        if (!undoStack.isEmpty()) {
            redoStack.push(copyImage(currentImage));
            currentImage = undoStack.pop();
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No hay acciones para deshacer.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Rehace la última operación, si es posible.
     */
    private void rehacer() {
        if (!redoStack.isEmpty()) {
            undoStack.push(copyImage(currentImage));
            currentImage = redoStack.pop();
            escalarImagen();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No hay acciones para rehacer.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Aplica un filtro/operación sobre la imagen (sin parámetros adicionales).
     * Maneja automáticamente la pila de deshacer y reescala al final.
     */
    private void aplicarOperacion(Function<BufferedImage, BufferedImage> operacion) {
        if (!checkImageLoaded())
            return;
        undoStack.push(copyImage(currentImage));
        currentImage = operacion.apply(currentImage);
        escalarImagen();
        // Limpiar la pila de rehacer, pues se creó una nueva rama
        redoStack.clear();
    }

    /**
     * Aplica un filtro/operación que requiere un parámetro entero (por ejemplo
     * umbral, tamaño de máscara, etc.).
     */
    private void aplicarOperacionConEntero(String mensaje, String titulo, int valorPorDefecto,
            FiltroConEntero filtro) {
        if (!checkImageLoaded())
            return;
        Integer valor = solicitarEntero(mensaje, titulo, valorPorDefecto);
        if (valor == null)
            return; // Usuario canceló o valor inválido
        undoStack.push(copyImage(currentImage));
        currentImage = filtro.aplicar(currentImage, valor);
        escalarImagen();
        redoStack.clear();
    }

    // ---------------------------------------------
    // Métodos de acción concretos para cada filtro
    // ---------------------------------------------

    private void aplicarEscalaDeGrises() {
        aplicarOperacion(Operaciones::escalaDeGrises);
    }

    private void aplicarImagenBinaria() {
        aplicarOperacionConEntero(
                "Introduce el umbral (0-255):",
                "Umbral de binarización",
                128,
                (img, umbral) -> Operaciones.imagenBinaria(img, umbral));
    }

    private void aplicarNegativo() {
        aplicarOperacion(Operaciones::negativo);
    }

    private void mostrarHistograma() {
        if (!checkImageLoaded())
            return;
        int[] histograma = Operaciones.calcularHistogramaGrises(currentImage);

        JFrame histogramaFrame = new JFrame("Histograma de Intensidad (Grises)");
        histogramaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        histogramaFrame.add(new HistogramaPanel(histograma));
        histogramaFrame.pack();
        histogramaFrame.setLocationRelativeTo(this);
        histogramaFrame.setVisible(true);
    }

    private void aplicarFiltroMedia() {
        aplicarOperacionConEntero(
                "Introduce el tamaño de la máscara (3, 5, 7, ...):",
                "Filtro de media",
                3,
                (img, tam) -> Operaciones.filtroMedia(img, tam));
    }

    private void aplicarFiltroMediana() {
        aplicarOperacionConEntero(
                "Introduce el tamaño de la máscara (3, 5, 7, ...):",
                "Filtro de mediana",
                3,
                (img, tam) -> Operaciones.filtroMediana(img, tam));
    }

    private void aplicarFiltroSobel() {
        aplicarOperacion(Operaciones::filtroSobel);
    }

    private void aplicarFiltroPrewitt() {
        aplicarOperacion(Operaciones::filtroPrewitt);
    }

    private void aplicarFiltroLaplaciano() {
        aplicarOperacion(Operaciones::filtroLaplaciano);
    }

    private void aplicarErosion() {
        aplicarOperacion(Operaciones::erosion3x3);
    }

    private void aplicarDilatacion() {
        aplicarOperacion(Operaciones::dilatacion3x3);
    }

    private void aplicarFiltroPasoBajo() {
        aplicarOperacion(Operaciones::filtroPasoBajo);
    }

    private void aplicarFiltroPasoAlto() {
        aplicarOperacion(Operaciones::filtroPasoAlto);
    }

    private void aplicarFiltroPasoBanda() {
        aplicarOperacion(Operaciones::filtroPasoBanda);
    }

    private void aplicarApertura() {
        aplicarOperacion(Operaciones::apertura3x3);
    }

    private void aplicarCierre() {
        aplicarOperacion(Operaciones::cierre3x3);
    }

    private void aplicarEsqueletonizacion() {
        aplicarOperacion(Operaciones::esqueletonizacion3x3);
    }

    // ---------------------------------------------
    // Métodos de utilidad
    // ---------------------------------------------

    /**
     * Verifica si hay una imagen cargada. Si no la hay, muestra un mensaje y
     * retorna false.
     */
    private boolean checkImageLoaded() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay ninguna imagen abierta.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Solicita un entero al usuario mediante un JOptionPane. Controla cancelación y
     * errores de parseo.
     *
     * @param mensaje         Texto a mostrar en el diálogo.
     * @param titulo          Título de la ventana.
     * @param valorPorDefecto Valor inicial (por ejemplo 3).
     * @return Integer válido o null si el usuario canceló o puso un valor no
     *         válido.
     */
    private Integer solicitarEntero(String mensaje, String titulo, int valorPorDefecto) {
        String valorStr = JOptionPane.showInputDialog(this, mensaje, titulo, JOptionPane.QUESTION_MESSAGE);
        if (valorStr == null) {
            // El usuario canceló
            return null;
        }
        try {
            return Integer.parseInt(valorStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Valor inválido: " + valorStr,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Copia profunda de la imagen para evitar referencias compartidas.
     */
    private BufferedImage copyImage(BufferedImage source) {
        BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        copy.getGraphics().drawImage(source, 0, 0, null);
        return copy;
    }

    /**
     * Reescala la imagen para ajustarla al tamaño disponible del scrollPane,
     * manteniendo proporción.
     */
    private void escalarImagen() {
        if (currentImage == null) {
            return;
        }
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
        // Si te interesa redibujar en tiempo real al redimensionar la ventana,
        // puedes añadir un ComponentListener para llamar a escalarImagen().
    }

    /**
     * Interfaz funcional para filtros con parámetro entero (umbral, tamaño de
     * máscara, etc.).
     */
    @FunctionalInterface
    private interface FiltroConEntero {
        BufferedImage aplicar(BufferedImage img, int valor);
    }

    // Método main opcional para test rápido
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu();
            menu.setVisible(true);
        });
    }

}
