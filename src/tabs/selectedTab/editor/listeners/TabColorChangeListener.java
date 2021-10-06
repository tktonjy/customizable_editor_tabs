package tabs.selectedTab.editor.listeners;

import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.ui.FileColorManager;
import com.intellij.ui.tabs.TabInfo;
import tabs.selectedTab.editor.TabChangedListener;
import tabs.selectedTab.editor.TabsPainter;
import tabs.settings.AppSettingsState;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.Objects;

public class TabColorChangeListener extends ChangeListenerBase  {

    public TabColorChangeListener(FileColorManager colorManager, EditorWindow editorWindow, TabChangedListener tabChangedListener) {
        super(colorManager, editorWindow, tabChangedListener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final TabInfo selectedTab = getSelectedTab();
        if (evt.getSource() != null && selectedTab != null) {
            final TabInfo changedTab = (TabInfo) evt.getSource();
            final int changedTabHash = Objects.hashCode(changedTab.getObject().toString());
            final int selectedTabHash = Objects.hashCode(selectedTab.getObject().toString());

            if (changedTabHash == selectedTabHash &&
                    !new Color(AppSettingsState.getInstance().selectedTabColor).equals(evt.getNewValue())) {
                //changedTab.setTabColor(new Color(AppSettingsState.getInstance().selectedTabColor));

                this.tabChangedListener.getTabsPainter().colorTabsHardcoded(colorManager, this.editorWindow,this.tabChangedListener.getSelectedTabIdx());
                //this.tabChangedListener.colorTabsHardcoded(this.editorWindow, this.tabChangedListener.getSelectedTabIdx());
            }

        }
    }

}
