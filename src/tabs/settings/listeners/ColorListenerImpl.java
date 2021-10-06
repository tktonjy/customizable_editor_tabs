package tabs.settings.listeners;

import com.intellij.ui.picker.ColorListener;

import javax.swing.*;
import java.awt.*;

public class ColorListenerImpl implements ColorListener {

    private final JButton btn;

    private Color color;

    public ColorListenerImpl(JButton btn) {
        this.btn = btn;
    }

    @Override
    public void colorChanged(Color color, Object o) {
        this.color = color;
        this.btn.setBackground(color);
    }

    public Color getColor() {
        return color;
    }

}
