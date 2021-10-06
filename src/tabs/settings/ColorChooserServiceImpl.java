package tabs.settings;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.ColorChooserService;
import com.intellij.ui.ColorPicker;
import com.intellij.ui.ColorPickerListener;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.picker.ColorListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class ColorChooserServiceImpl extends ColorChooserService {

    @Nullable
    @Override
    public Color showDialog(Component parent,
                            @NlsContexts.DialogTitle String caption,
                            Color preselectedColor,
                            boolean enableOpacity,
                            List<? extends ColorPickerListener> listeners,
                            boolean opacityInPercent) {
        return ColorPicker.showDialog(parent, caption, preselectedColor, enableOpacity, listeners, opacityInPercent);
    }

    @Nullable
    @Override
    public Color showDialog(Project project,
                            Component parent,
                            @NlsContexts.DialogTitle String caption,
                            Color preselectedColor,
                            boolean enableOpacity,
                            List<? extends ColorPickerListener> listeners,
                            boolean opacityInPercent) {
        return showDialog(parent, caption, preselectedColor, enableOpacity, listeners, opacityInPercent);
    }

    @Override
    public void showColorPickerPopup(@Nullable Project project, @Nullable Color currentColor, @NotNull ColorListener listener, @Nullable RelativePoint location, boolean showAlpha) {
        ColorPicker.showColorPickerPopup(project, currentColor, listener, location, showAlpha);
    }
}
