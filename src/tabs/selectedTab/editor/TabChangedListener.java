package tabs.selectedTab.editor;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.fileEditor.impl.EditorWithProviderComposite;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.FileColorManager;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.UiDecorator;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import tabs.selectedTab.editor.listeners.TabColorChangeListener;
import tabs.selectedTab.editor.listeners.TextColorChangeListener;
import tabs.settings.AppSettingsState;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class TabChangedListener implements FileEditorManagerListener {

    private static volatile boolean styleSet = false;

    private static final Logger LOGGER = Logger.getInstance(TabChangedListener.class);

    private final TabsPainter tabsPainter;

    private volatile AppSettingsState oldSettings;

    private volatile int selectedTabIdx = -1;

    public TabChangedListener() {

//        TabsLayoutInfo tabsLayoutInfo = TabsLayoutSettingsManager.getInstance().getSelectedTabsLayoutInfo();
//        if (tabsLayoutInfo instanceof ScrollableSingleRowLayout.ScrollableSingleRowTabsLayoutInfo ) {
//            final @Nullable Integer[] availableTabsPositions = ((ScrollableSingleRowLayout.ScrollableSingleRowTabsLayoutInfo) tabsLayoutInfo).getAvailableTabsPositions();
//        }

        try {
            oldSettings = (AppSettingsState) AppSettingsState.getInstance().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        tabsPainter = new TabsPainter();
    }

    @Override
    public void fileOpened(@NotNull FileEditorManager fileEditorManager, @NotNull VirtualFile virtualFile) {

        final Project project = fileEditorManager.getProject();
        final FileEditorManagerEx manager = FileEditorManagerEx.getInstanceEx(project);
        final FileColorManager fileColorManager = FileColorManager.getInstance(project);

        for (EditorWindow editorWindow : manager.getWindows()) {

            final EditorWithProviderComposite fileComposite = editorWindow.findFileComposite(virtualFile);
            final int editorIndex = getEditorIndex(editorWindow, fileComposite);

            setSelectedTabIdx(editorIndex);
            tabsPainter.colorTabs(fileColorManager, fileColorManager.getFileColor(virtualFile), editorWindow, editorIndex);
            addTabChangedListeners(fileColorManager, editorWindow, editorIndex);
        }
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager fileEditorManager, @NotNull VirtualFile virtualFile) {
        tabsPainter.remove(Objects.hashCode(virtualFile.toString()));

        final Project project = fileEditorManager.getProject();
        final FileEditorManagerEx manager = FileEditorManagerEx.getInstanceEx(project);
        final FileColorManager fileColorManager = FileColorManager.getInstance(project);

        for (EditorWindow editorWindow : manager.getWindows()) {

            setSelectedTabIdx(-1);
            tabsPainter.colorTabs(fileColorManager, fileColorManager.getFileColor(virtualFile), editorWindow, -1);
        }

    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent fileEditorManagerEvent) {


        final Project project = fileEditorManagerEvent.getManager().getProject();
        final FileEditorManagerEx manager = FileEditorManagerEx.getInstanceEx(project);
        final FileColorManager fileColorManager = FileColorManager.getInstance(project);

        final VirtualFile oldFile = fileEditorManagerEvent.getOldFile();
        final VirtualFile newFile = fileEditorManagerEvent.getNewFile();

        for (EditorWindow editorWindow : manager.getWindows()) {

            refreshSettings(editorWindow, fileColorManager);

            if (oldFile != null && editorWindow.findFileComposite(oldFile) != null) {
                setTabStyle(fileColorManager, oldFile, editorWindow, false);
            }

            if (newFile != null && editorWindow.findFileComposite(newFile) != null) {
                setTabStyle(fileColorManager, newFile, editorWindow, true);
            }
        }
    }

    private void addTabChangedListeners(FileColorManager colorManager, EditorWindow editorWindow, int editorIndex) {
        TabInfo tab = editorWindow.getTabbedPane().getTabs().getTabs().get(editorIndex);
        if (tab != null) {

            //if (AppSettingsState.getInstance().overrideTabsColor) {
                final TabColorChangeListener listener = new TabColorChangeListener(colorManager, editorWindow, this);
                tab.getChangeSupport().addPropertyChangeListener(TabInfo.TAB_COLOR, listener);
            //}

            //if (AppSettingsState.getInstance().overrideTextColorOfTabs) {
                TextColorChangeListener listener2 = new TextColorChangeListener(colorManager, editorWindow, this);
                tab.getChangeSupport().addPropertyChangeListener(TabInfo.TEXT, listener2);

        }
    }

    private void refreshSettings(EditorWindow editorWindow, FileColorManager colorManager) {
        if (!AppSettingsState.getInstance().equals(oldSettings)) {
            // settings changed
            styleSet = false;
            tabsPainter.refreshSettings(editorWindow, colorManager);
            try {
                oldSettings = (AppSettingsState) AppSettingsState.getInstance().clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }


    private void setTabStyle(FileColorManager fileColorManager, @NotNull VirtualFile file, @NotNull EditorWindow editorWindow, boolean onFocus) {

        try {

            setTabFont(editorWindow);

            final EditorWithProviderComposite fileComposite = editorWindow.findFileComposite(file);

            final int index = getEditorIndex(editorWindow, fileComposite);
            if (index >= 0) {
                setSelectedTabIdx(index);
                tabsPainter.colorTabs(fileColorManager, fileColorManager.getFileColor(file), editorWindow, index);
                setBold(editorWindow, onFocus, index);
            }
        } finally {
            styleSet = true;
        }
    }

    private void setBold(@NotNull EditorWindow editorWindow, boolean onFocus, int index) {
//        if (!AppSettingsState.getInstance().boldEditorTab) {
//            return;
//        }
        final TabInfo selectedTab = editorWindow.getTabbedPane().getTabs().getTabAt(index);

        final SimpleTextAttributes originalAttrs = getAttrs(selectedTab);
        if (originalAttrs == null) {
            return;
        }

        final int originalAttrsStyle = originalAttrs.getStyle();

        int newStyle;
        if (onFocus && AppSettingsState.getInstance().boldEditorTab) {
            newStyle = originalAttrsStyle | (SimpleTextAttributes.STYLE_BOLD);
        } else {
            newStyle = originalAttrsStyle & (~SimpleTextAttributes.STYLE_BOLD);
        }
        selectedTab.setDefaultStyle(newStyle);
    }

    private void setSelectedTabIdx(int index) {
        if (index >= 0) {
            selectedTabIdx = index;
        }
    }

    private void setTabFont(@NotNull EditorWindow editorWindow) {

        if (!styleSet &&
                AppSettingsState.getInstance().overrideEditorTabsFont) {

            final String fontName = AppSettingsState.getInstance().fontName;
            final String fontSize = AppSettingsState.getInstance().fontSize;
            final String tabsOffsetToTheRight = AppSettingsState.getInstance().tabsOffsetToTheRight;
            int offset = 0;

            try {
                if (tabsOffsetToTheRight != null && !tabsOffsetToTheRight.trim().equals("")) {
                    offset = Integer.parseInt(tabsOffsetToTheRight);
                    if (offset < 0) {
                        offset = 0;
                    }
                }
                final int size = Integer.parseInt(fontSize);

                if (size > 0 && fontName != null &&
                        Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()).
                                anyMatch(font -> fontName.equals(font.getName()))) {

                    final int finalOffset = offset;

                    editorWindow.getTabbedPane().getTabs().getPresentation().setUiDecorator(new UiDecorator() {
                        @Override
                        public @NotNull
                        UiDecoration getDecoration() {

                            return new UiDecoration(new Font(AppSettingsState.getInstance().fontName, Font.PLAIN, size),
                                    JBUI.insets(0, finalOffset / 2, 0, finalOffset / 2));
                        }
                    });
                }

            } catch (NumberFormatException ex) {
                LOGGER.error(ex);
            }

        }
    }

    private SimpleTextAttributes getAttrs(TabInfo selectedTab) {
        try {
            final Method getDefaultAttributes = selectedTab.getClass().getDeclaredMethod("getDefaultAttributes");
            getDefaultAttributes.setAccessible(true);
            return (SimpleTextAttributes) getDefaultAttributes.invoke(selectedTab);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getEditorIndex(@NotNull EditorWindow editorWindow, EditorWithProviderComposite fileComposite) {
        int index = -1;
        for (EditorWithProviderComposite editorWithProviderComposite : editorWindow.getEditors()) {
            index++;
            if (editorWithProviderComposite.equals(fileComposite)) {
                break;
            }
        }

        return index;
    }


    public int getSelectedTabIdx() {
        return selectedTabIdx;
    }

    public static void invalidateSettings() {
        styleSet = false;
    }

    public TabsPainter getTabsPainter() {
        return tabsPainter;
    }
}
