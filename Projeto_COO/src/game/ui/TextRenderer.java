package game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.lang.reflect.Field;

import lib.GameLib;

public class TextRenderer {
    private static final Field GRAPHICS_FIELD = resolveGraphicsField();

    private TextRenderer() {
    }

    public static void drawTextCentered(String text, double cx, double centerY, Color color, int fontSize) {
        Graphics g = currentGraphics();
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
        g.setColor(color);

        FontMetrics metrics = g.getFontMetrics();
        int width = metrics.stringWidth(text);
        int baseline = (int) Math.round(centerY + (metrics.getAscent() - metrics.getDescent()) / 2.0);

        g.drawString(text, (int) Math.round(cx - width / 2.0), baseline);
    }

    private static Field resolveGraphicsField() {
        try {
            Field field = GameLib.class.getDeclaredField("g");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static Graphics currentGraphics() {
        try {
            return (Graphics) GRAPHICS_FIELD.get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Não foi possível acessar o contexto gráfico do GameLib.", e);
        }
    }
}
