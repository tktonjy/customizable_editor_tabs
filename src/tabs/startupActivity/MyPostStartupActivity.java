package tabs.startupActivity;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link StartupActivity} to demonstrate how a plugin can execute some code after a project has been opened.
 */
public class MyPostStartupActivity implements StartupActivity {
    private static final Logger LOG = Logger.getInstance(MyPostStartupActivity.class);

    public void runActivity(@NotNull Project project) {
        LOG.info("Project startup activity");
    }
}
