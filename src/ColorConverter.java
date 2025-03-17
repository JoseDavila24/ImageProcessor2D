public class ColorConverter {

    // Convertir de RGB a HSV
    public static float[] rgbToHsv(int r, int g, int b) {
        float[] hsv = new float[3];
        float min = Math.min(Math.min(r, g), b);
        float max = Math.max(Math.max(r, g), b);
        float delta = max - min;

        // Hue
        if (delta == 0) {
            hsv[0] = 0;
        } else if (max == r) {
            hsv[0] = (60 * ((g - b) / delta) % 360);
        } else if (max == g) {
            hsv[0] = 60 * ((b - r) / delta + 2);
        } else {
            hsv[0] = 60 * ((r - g) / delta + 4);
        }

        // Saturation
        hsv[1] = (max == 0) ? 0 : (delta / max);

        // Value
        hsv[2] = max / 255.0f;

        return hsv;
    }

    // Convertir de HSV a RGB
    public static int[] hsvToRgb(float h, float s, float v) {
        int[] rgb = new int[3];
        int hInt = (int) (h / 60);
        float f = (h / 60) - hInt;
        float p = v * (1 - s);
        float q = v * (1 - s * f);
        float t = v * (1 - s * (1 - f));

        switch (hInt) {
            case 0:
                rgb[0] = (int) (v * 255);
                rgb[1] = (int) (t * 255);
                rgb[2] = (int) (p * 255);
                break;
            case 1:
                rgb[0] = (int) (q * 255);
                rgb[1] = (int) (v * 255);
                rgb[2] = (int) (p * 255);
                break;
            case 2:
                rgb[0] = (int) (p * 255);
                rgb[1] = (int) (v * 255);
                rgb[2] = (int) (t * 255);
                break;
            case 3:
                rgb[0] = (int) (p * 255);
                rgb[1] = (int) (q * 255);
                rgb[2] = (int) (v * 255);
                break;
            case 4:
                rgb[0] = (int) (t * 255);
                rgb[1] = (int) (p * 255);
                rgb[2] = (int) (v * 255);
                break;
            case 5:
                rgb[0] = (int) (v * 255);
                rgb[1] = (int) (p * 255);
                rgb[2] = (int) (q * 255);
                break;
        }
        return rgb;
    }
}