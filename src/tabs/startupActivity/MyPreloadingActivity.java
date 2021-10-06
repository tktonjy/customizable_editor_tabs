package tabs.startupActivity;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.application.PreloadingActivity;
import com.intellij.openapi.diagnostic.Logger;

/**
 * A {@link PreloadingActivity} to demonstrate how a plugin could run some (possibly expensive)
 * activities on startup.
 */
public class MyPreloadingActivity implements AppLifecycleListener {
    private static final Logger LOG = Logger.getInstance(MyPreloadingActivity.class);

    @Override
    public void welcomeScreenDisplayed() {
        LOG.info("Preloading plugin-data");

//        try {
//            Main.main(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
