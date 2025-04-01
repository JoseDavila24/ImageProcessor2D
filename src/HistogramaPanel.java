import javax.swing.*;
import java.awt.*;

public class HistogramaPanel extends JPanel {

    private int[] histograma;

    public HistogramaPanel(int[] histograma) {
        this.histograma = histograma;
        setPreferredSize(new Dimension(600, 350));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (histograma == null)
            return;

        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        int margin = 40;
        int graphWidth = width - 2 * margin;
        int graphHeight = height - 2 * margin;

        int max = 0;
        for (int v : histograma) {
            if (v > max)
                max = v;
        }

        // Dibujar ejes
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawLine(margin, height - margin, width - margin, height - margin); // eje X
        g2.drawLine(margin, height - margin, margin, margin); // eje Y

        // Etiquetas eje Y (frecuencia)
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i <= 5; i++) {
            int y = height - margin - (i * graphHeight / 5);
            int val = (int) (i * max / 5.0);
            g2.drawString(String.valueOf(val), 5, y + 5);
            g2.drawLine(margin - 5, y, margin, y);
        }

        // Etiquetas eje X (niveles de gris)
        int barCount = histograma.length;
        int barWidth = graphWidth / barCount;

        for (int i = 0; i < barCount; i++) {
            int barHeight = (int) ((histograma[i] / (float) max) * graphHeight);
            int x = margin + i * barWidth;
            int y = height - margin - barHeight;

            g2.setColor(Color.GREEN);
            g2.fillRect(x, y, barWidth, barHeight);

            // Mostrar etiquetas cada 64 niveles
            if (i % 64 == 0) {
                g2.setColor(Color.BLACK);
                g2.drawString(String.valueOf(i), x, height - margin + 12);
            }
        }

        // Títulos
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("Histograma de Intensidad (Grises)", margin, margin - 10);
        g2.drawString("Niveles de gris", width / 2 - 30, height - 5);
        g2.rotate(-Math.PI / 2);
        g2.drawString("Frecuencia", -height / 2 - 30, 15);
        g2.rotate(Math.PI / 2);
    }
}
