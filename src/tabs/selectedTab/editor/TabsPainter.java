package tabs.selectedTab.editor;

import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.FileColorManager;
import com.intellij.ui.tabs.TabInfo;
import org.jetbrains.annotations.NotNull;
import tabs.settings.AppSettingsState;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class TabsPainter {

    private enum Alternate {
        OFF,
        DARK_THEME,
        LIGHT_THEME;
    }

    private final Map<Integer, Color> modifiedTabs = new HashMap<>();
    private final Map<Integer, Color> originalTabTextColors = new ConcurrentHashMap<>();

    private Alternate alternateScheme = Alternate.OFF;


    public TabsPainter() {
        refreshSettings(null, null);
    }

    public boolean colorTabsHardcoded(FileColorManager colorManager, EditorWindow editorWindow, int index) {

        if (!AppSettingsState.getInstance().overrideTabsColor && !AppSettingsState.getInstance().overrideTextColorOfTabs) {
            return false;
        }

        if (index == -1) {
            return true;
        }

        final List<TabInfo> tabs = editorWindow.getTabbedPane().getTabs().getTabs();
        for (int i = 0; i < tabs.size(); i++) {
            final TabInfo tabInfo = tabs.get(i);
            //System.out.println("Color tabs : " + tabInfo.getText());
            if (i == index) {
                changeTabColor(colorManager, tabInfo, true);
                changeTextColor(tabInfo, true);
            } else {
                changeTabColor(colorManager, tabInfo, false);
                changeTextColor(tabInfo, false);
            }
        }

        return true;
    }

    public void colorTabs(FileColorManager colorManager, Color originalTabColor, @NotNull EditorWindow editorWindow, int index) {
        //setSelectedTabIdx(index);

        if (colorTabsHardcoded(colorManager, editorWindow, index)) {
            return;
        }

        alternateTabs(originalTabColor, editorWindow, index);
    }

    private void alternateTabs(Color originalTabColor, @NotNull EditorWindow editorWindow, int index) {
        if (alternateScheme == Alternate.OFF) {
            return;
        }

        final List<TabInfo> tabs = editorWindow.getTabbedPane().getTabs().getTabs();

        for (int i = 0; i < tabs.size(); i++) {
            final TabInfo tabInfo = tabs.get(i);

            Color tabColor = tabInfo.getTabColor();

            if (tabColor == null) {
                tabColor = originalTabColor;
                if (tabColor == null) {
                    tabColor = tabInfo.getComponent().getBackground();
                }
            }

            final int hash = Objects.hashCode(tabInfo.getObject().toString());

            if (modifiedTabs.containsKey(hash)) {
                tabColor = modifiedTabs.get(hash);
            }

            if (tabColor != null) {

                if (alternateScheme == Alternate.OFF) {
                    tabInfo.setTabColor(tabColor);
                } else {
                    final Color newColor = i % 2 == 0 ? lighter(tabColor) : shade(tabColor);

                    if (index == i) {//selected tab
                        tabInfo.setTabColor(alternateScheme != Alternate.DARK_THEME ? tabColor.brighter() : tabColor.darker());
                    } else {
                        tabInfo.setTabColor(newColor);
                    }

                }

                modifiedTabs.putIfAbsent(hash, tabColor);
            }
        }
    }

    private void changeTabColor(FileColorManager colorManager, TabInfo tabInfo, boolean selected) {
        if (!AppSettingsState.getInstance().overrideTabsColor) {
            return;
        }

        if (AppSettingsState.getInstance().textKeepOldColor) {
            // keep old color of not selected tabs
            if (!selected &&
                    tabInfo.getObject() != null && tabInfo.getObject() instanceof VirtualFile) {
                final Color originalFileColor = colorManager.getFileColor((VirtualFile) tabInfo.getObject());
                tabInfo.setTabColor(originalFileColor);
                return;
            }
        }

        final Color color;
        if (selected) {
            color = new Color(AppSettingsState.getInstance().selectedTabColor);
        } else {
            color = new Color(AppSettingsState.getInstance().tabsColor);
        }

        if (!color.equals(tabInfo.getTabColor())) {
            tabInfo.setTabColor(color);
        }
    }

    private void changeTextColor(TabInfo tabInfo, boolean selected) {
        if (!AppSettingsState.getInstance().overrideTextColorOfTabs) {
            return;
        }

        // the virtual file - full path
        final int hash = Objects.hashCode(tabInfo.getObject().toString());
        final Color currentTextColor = tabInfo.getDefaultForeground();
        if (currentTextColor != null) {
            if (!currentTextColor.equals(new Color(AppSettingsState.getInstance().selectedTextColor)) &&
                            !currentTextColor.equals(new Color(AppSettingsState.getInstance().textColor))) {
                originalTabTextColors.put(hash, currentTextColor);
            }
        }

        final boolean keepOldTextColor = AppSettingsState.getInstance().textKeepOldTextColor;

        final Color newColor;
        if (selected) {
            newColor = new Color(AppSettingsState.getInstance().selectedTextColor);
        } else {// not selected
            final Color originalColor = originalTabTextColors.get(hash);

            if (originalColor != null && keepOldTextColor) {
                // keep it
                newColor = originalColor;
            } else {
                newColor = new Color(AppSettingsState.getInstance().textColor);
            }
        }

        if (!newColor.equals(currentTextColor)) {
            tabInfo.setDefaultForeground(newColor);
        }
    }

    private Color shade(Color color) {
        double shade_factor;
        if (alternateScheme == Alternate.DARK_THEME) {
            shade_factor = 0.6;
        } else {
            shade_factor = 0.001;
        }
        final int newR = (int) (color.getRed() * (1 - shade_factor));
        final int newG = (int) (color.getGreen() * (1 - shade_factor));
        final int newB = (int) (color.getBlue() * (1 - shade_factor));
        return new Color(newR, newG, newB, color.getAlpha());
        //return color;
    }

    private Color lighter(Color color) {
        double tint_factor;
        if (alternateScheme != Alternate.DARK_THEME) {
            tint_factor = 0.95;
        } else {
            tint_factor = 0.0;
        }
        final int newR = (int) (color.getRed() + (255 - color.getRed()) * tint_factor);
        final int newG = (int) (color.getGreen() + (255 - color.getGreen()) * tint_factor);
        final int newB = (int) (color.getBlue() + (255 - color.getBlue()) * tint_factor);
        return new Color(newR, newG, newB, color.getAlpha());
//        return color;
    }

    public void refreshSettings(EditorWindow editorWindow, FileColorManager colorManager) {
        restoreOriginalTabsColors(editorWindow, colorManager);
        restoreOriginalTabsTextColors(editorWindow);

        if (AppSettingsState.getInstance().alternateTabsColor != null) {
            switch (AppSettingsState.getInstance().alternateTabsColor.toLowerCase()) {
                case "dark themes":
                    alternateScheme = Alternate.DARK_THEME;
                    break;
                case "light themes":
                    alternateScheme = Alternate.LIGHT_THEME;
                    break;
                case "off":
                    alternateScheme = Alternate.OFF;
                    break;
            }
        }
    }

    public void remove(int hash) {
        modifiedTabs.remove(hash);
        originalTabTextColors.remove(hash);
    }

    private void restoreOriginalTabsColors(EditorWindow editorWindow, FileColorManager colorManager) {
        if (editorWindow == null || colorManager == null) {
            return;
        }

        if (AppSettingsState.getInstance().overrideTabsColor) {// TODO
            // don't restore
            return;
        }

        final List<TabInfo> tabs = editorWindow.getTabbedPane().getTabs().getTabs();
        for (int i = 0; i < tabs.size(); i++) {
            final TabInfo tabInfo = tabs.get(i);

            if (tabInfo.getObject() != null && tabInfo.getObject() instanceof VirtualFile) {
                final Color originalFileColor = colorManager.getFileColor((VirtualFile) tabInfo.getObject());
                tabInfo.setTabColor(originalFileColor);
            }


        }
    }

    private void restoreOriginalTabsTextColors(EditorWindow editorWindow) {
        if (editorWindow == null) {
            return;
        }

        if (AppSettingsState.getInstance().overrideTextColorOfTabs) {// todo
            // don't restore
            return;
        }

        final List<TabInfo> tabs = editorWindow.getTabbedPane().getTabs().getTabs();
        for (int i = 0; i < tabs.size(); i++) {
            final TabInfo tabInfo = tabs.get(i);
            final int hash = Objects.hashCode(tabInfo.getObject().toString());
            final Color originalColor = originalTabTextColors.get(hash);
                tabInfo.setDefaultForeground(originalColor);
        }
    }
}