// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package tabs.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "org.intellij.sdk.settings.AppSettingsState",
        storages = {@Storage("SdkSettingsPlugin.xml")}
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState>, Cloneable {

    public String maxNumOfChars = "10";
    public String tabsOffsetToTheRight = "5";
    public boolean fill = false;
    public boolean displayOnlyFileName = false;
    public boolean boldEditorTab = false;
    public boolean verticalBorder = false;

    public boolean overrideEditorTabsFont = false;

    public String fontName = "Arial";
    public String fontSize = "10";
    public String alternateTabsColor = "off";

    public boolean overrideTabsColor = false;

    public boolean overrideTextColorOfTabs = false;

    public int selectedTabColor = Color.BLACK.getRGB();
    public int tabsColor = Color.white.getRGB();
    public boolean textKeepOldColor = false;

    public int selectedTextColor = Color.WHITE.getRGB();
    public int textColor = Color.BLACK.getRGB();
    public boolean textKeepOldTextColor;
    public boolean append3Dots;
    public boolean fillWithSpaces;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AppSettingsState that = (AppSettingsState) o;
        return fill == that.fill && displayOnlyFileName == that.displayOnlyFileName && boldEditorTab == that.boldEditorTab && verticalBorder == that.verticalBorder && overrideEditorTabsFont == that.overrideEditorTabsFont && overrideTabsColor == that.overrideTabsColor && overrideTextColorOfTabs == that.overrideTextColorOfTabs && selectedTabColor == that.selectedTabColor && tabsColor == that.tabsColor && textKeepOldColor == that.textKeepOldColor && selectedTextColor == that.selectedTextColor && textColor == that.textColor && textKeepOldTextColor == that.textKeepOldTextColor && append3Dots == that.append3Dots && fillWithSpaces == that.fillWithSpaces && Objects.equals(maxNumOfChars, that.maxNumOfChars) && Objects.equals(tabsOffsetToTheRight, that.tabsOffsetToTheRight) && Objects.equals(fontName, that.fontName) && Objects.equals(fontSize, that.fontSize) && Objects.equals(alternateTabsColor, that.alternateTabsColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxNumOfChars, tabsOffsetToTheRight, fill, displayOnlyFileName, boldEditorTab, verticalBorder, overrideEditorTabsFont, fontName, fontSize, alternateTabsColor, overrideTabsColor, overrideTextColorOfTabs, selectedTabColor, tabsColor, textKeepOldColor, selectedTextColor, textColor, textKeepOldTextColor, append3Dots, fillWithSpaces);
    }

    public static AppSettingsState getInstance() {
        return ServiceManager.getService(AppSettingsState.class);
    }

    @Nullable
    @Override
    public AppSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AppSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
       return super.clone();
    }

}
