package tabs.selectedTab;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import tabs.selectedTab.editor.TabChangedListener;
import org.jetbrains.annotations.NotNull;

public class TabComponent implements ApplicationComponent {

    private static final Logger logger = Logger.getInstance(TabComponent.class);

    private MessageBusConnection connection;

    public TabComponent() {
    }

    @Override
    public void initComponent() {
        logger.debug("Initializing component");
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        connection = bus.connect();
        TabChangedListener tabChangedListener = new TabChangedListener();
        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, tabChangedListener);
    }

    @Override
    public void disposeComponent() {
        logger.debug("Disposing component");
        connection.disconnect();
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "tabs.selectedTab.TabComponent";
    }
}