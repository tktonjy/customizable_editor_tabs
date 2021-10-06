package tabs.selectedTab.editor.listeners;

import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.ui.FileColorManager;
import com.intellij.ui.tabs.TabInfo;
import tabs.selectedTab.editor.TabChangedListener;
import tabs.selectedTab.editor.TabsPainter;

import java.beans.PropertyChangeEvent;

public class TextColorChangeListener extends ChangeListenerBase {

    public TextColorChangeListener(FileColorManager colorManager, EditorWindow editorWindow, TabChangedListener tabChangedListener) {
        super(colorManager,editorWindow, tabChangedListener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final TabInfo selectedTab = getSelectedTab();
        if (evt.getSource() != null && selectedTab != null) {
            this.tabChangedListener.getTabsPainter().colorTabsHardcoded(colorManager, this.editorWindow,this.tabChangedListener.getSelectedTabIdx());
            //this.tabChangedListener.colorTabsHardcoded(this.editorWindow, this.tabChangedListener.getSelectedTabIdx());
        }
    }

}
