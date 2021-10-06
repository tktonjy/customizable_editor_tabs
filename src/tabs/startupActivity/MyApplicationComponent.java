package tabs.startupActivity;

import asm.Main;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Sample application component to demonstrate how code can be executed on startup. Open "resources/META-INF/plugin.xml"
 * to see how this class is configued for the plugin.
 *
 * @author jansorg
 */
public class MyApplicationComponent implements ApplicationComponent {
    private static final Logger LOG = Logger.getInstance(MyApplicationComponent.class);

    /**
     * This method is called on startup of the application, i.e. as soon as the plugin is initialized.
     */
    public void initComponent() {
        LOG.info("Initializing plugin data structures");
        try {
            Main.main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called as soon as the application is closed.
     */
//    public void disposeComponent() {
//        LOG.info("Disposing plugin data structures");
//    }

    @NotNull
    public String getComponentName() {
        return "myApplicationComponent";
    }
}
