package asm;

import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.LineSeparator;
import com.intellij.util.io.PathKt;
import org.jetbrains.org.objectweb.asm.ClassReader;
import org.jetbrains.org.objectweb.asm.ClassWriter;
import org.jetbrains.org.objectweb.asm.Opcodes;
import tabs.settings.AppSettingsState;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main implements Opcodes {

    public static void main(String[] args) throws Exception {

        setTabVerticalBorders(AppSettingsState.getInstance().verticalBorder);

        int maxChars = 10;
        try {
            maxChars = Integer.parseInt(AppSettingsState.getInstance().maxNumOfChars);
            if (maxChars <= 0) {
                maxChars = 1000;
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        System.setProperty("max_editor_tab_chars", "" + maxChars);
        System.setProperty("only_file_name_in_tooltip", "" + AppSettingsState.getInstance().displayOnlyFileName);
        System.setProperty("append_3_dots", "" + AppSettingsState.getInstance().append3Dots);
        System.setProperty("fill_with_spaces", "" + AppSettingsState.getInstance().fillWithSpaces);

        ClassReader classReader;
        try (final InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("com/intellij/ui/tabs/TabInfo.class")) {
            if (inputStream == null) {
                System.err.println("Couldn't find class com/intellij/ui/tabs/TabInfo.class in the classpath.");
                return;
            }
            final byte[] bytes = inputStream.readAllBytes();
            classReader = new ClassReader(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        final MethodReplacer methodReplacer = new MethodReplacer(classWriter, "setText");
        classReader.accept(methodReplacer, 0);

        Loader.loadClass(classWriter.toByteArray(), "com.intellij.ui.tabs.TabInfo");
    }

    public static void setTabVerticalBorders(boolean val) {
        final String customPropertiesFile = getCustomPropertiesFile();
        if (customPropertiesFile == null) {

            System.err.println("Couldn't find idea.properties file path.");
            new Throwable().printStackTrace();
            return;
        }


        final String separator = LineSeparator.getSystemLineSeparator().getSeparatorString();

        final Path path = Paths.get(customPropertiesFile);
        if (!path.toFile().exists()) {
            System.out.println("Creating: " + path);
            final String fullProductName = ApplicationNamesInfo.getInstance().getFullProductName();
            PathKt.write(path,
                    StringUtil.convertLineSeparators("# custom " + fullProductName + " properties\n\n",
                            separator));
        }

        final String newVal = "ide.new.editor.tabs.vertical.borders=" + val + separator;

        try (BufferedReader br = new BufferedReader(new FileReader(customPropertiesFile));
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            boolean lineSet = false;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("ide.new.editor.tabs.vertical.borders")) {
                    out.write(newVal.getBytes());
                    lineSet = true;
                } else {
                    out.write((line + separator).getBytes());
                }
            }

            if (!lineSet) {
                out.write(newVal.getBytes());
            }

            br.close();

            try (final FileOutputStream fileOutputStream = new FileOutputStream(customPropertiesFile)) {
                fileOutputStream.write(out.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Setting ide.new.editor.tabs.vertical.borders set = " + val);


    }

    private static String getCustomPropertiesFile() {
        String configPath = PathManager.getCustomOptionsDirectory();
        return configPath != null ? configPath + '/' + PathManager.PROPERTIES_FILE_NAME : null;
    }
}