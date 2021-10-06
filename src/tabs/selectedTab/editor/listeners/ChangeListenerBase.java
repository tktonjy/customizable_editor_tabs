package tabs.selectedTab.editor.listeners;

import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.ui.FileColorManager;
import com.intellij.ui.tabs.TabInfo;
import tabs.selectedTab.editor.TabChangedListener;

import java.beans.PropertyChangeListener;
import java.util.List;

public abstract class ChangeListenerBase implements PropertyChangeListener {

    protected final FileColorManager colorManager;
    protected final EditorWindow editorWindow;
    protected final TabChangedListener tabChangedListener;

    public ChangeListenerBase(FileColorManager colorManager, EditorWindow editorWindow, TabChangedListener tabChangedListener) {
        this.colorManager = colorManager;
        this.editorWindow = editorWindow;
        this.tabChangedListener = tabChangedListener;
    }

    protected TabInfo getSelectedTab() {
        final int selectedTabIdx = tabChangedListener.getSelectedTabIdx();
        final List<TabInfo> tabs = editorWindow.getTabbedPane().getTabs().getTabs();

        if (selectedTabIdx >= 0 && selectedTabIdx < tabs.size()) {
            return tabs.get(selectedTabIdx);
        } else {
            return null;
        }
    }
}
