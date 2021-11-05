// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package tabs.settings;

import asm.Main;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Provides controller functionality for application settings.
 */
public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Customizable Editor Tabs";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettingsState settings = AppSettingsState.getInstance();
        boolean modified = !mySettingsComponent.getMaxNumberOfChars().equals(settings.maxNumOfChars);
        modified |= !mySettingsComponent.getTabsOffsetToTheRight().equals(settings.tabsOffsetToTheRight);
        modified |= mySettingsComponent.getDisplayOnlyFileName() != settings.displayOnlyFileName;
        modified |= mySettingsComponent.getAppend3Dots() != settings.append3Dots;
        modified |= mySettingsComponent.getFillWithSpaces() != settings.fillWithSpaces;
        modified |= mySettingsComponent.getBoldEditorTab() != settings.boldEditorTab;
        modified |= mySettingsComponent.getVerticalBorder() != settings.verticalBorder;

        modified |= mySettingsComponent.getOverrideEditorTabsFont() != settings.overrideEditorTabsFont;
        //modified |= mySettingsComponent.getOverrideTabsColor() != settings.overrideTabsColor;
        modified |= !mySettingsComponent.getFont().equals(settings.fontName);
        modified |= !mySettingsComponent.getFontSize().equals(settings.fontSize);

        modified |= mySettingsComponent.getOverrideTabsColor() != (settings.overrideTabsColor);
        modified |= mySettingsComponent.getTabsColor().getRGB() !=settings.tabsColor;
        modified |= mySettingsComponent.getSelectedTabColor().getRGB() != settings.selectedTabColor;

        // text color
        modified |= mySettingsComponent.getOverrideTextColorOfTabs() != (settings.overrideTextColorOfTabs);
        modified |= mySettingsComponent.getTextColor().getRGB() !=settings.textColor;
        modified |= mySettingsComponent.getSelectedTextColor().getRGB() != settings.selectedTextColor;

        modified |= mySettingsComponent.getTextKeepOldColor() != settings.textKeepOldColor;
        modified |= mySettingsComponent.getTextKeepOldTextColor() != settings.textKeepOldTextColor;

        modified |= !mySettingsComponent.getAlternateTabsColor().equals(settings.alternateTabsColor);

        return modified;
    }

    @Override
    public void apply() {
        AppSettingsState settings = AppSettingsState.getInstance();
        settings.maxNumOfChars = mySettingsComponent.getMaxNumberOfChars();
        settings.tabsOffsetToTheRight = mySettingsComponent.getTabsOffsetToTheRight();
        settings.displayOnlyFileName = mySettingsComponent.getDisplayOnlyFileName();
        settings.append3Dots = mySettingsComponent.getAppend3Dots();
        settings.fillWithSpaces = mySettingsComponent.getFillWithSpaces();
        settings.boldEditorTab = mySettingsComponent.getBoldEditorTab();
        settings.verticalBorder = mySettingsComponent.getVerticalBorder();

        settings.overrideEditorTabsFont = mySettingsComponent.getOverrideEditorTabsFont();
        settings.fontName = mySettingsComponent.getFont();
        settings.fontSize = mySettingsComponent.getFontSize();
        settings.alternateTabsColor = mySettingsComponent.getAlternateTabsColor();

        settings.overrideTabsColor = mySettingsComponent.getOverrideTabsColor();
        settings.tabsColor = mySettingsComponent.getTabsColor().getRGB();
        settings.selectedTabColor = mySettingsComponent.getSelectedTabColor().getRGB();

        // text color
        settings.overrideTextColorOfTabs = mySettingsComponent.getOverrideTextColorOfTabs();
        settings.textColor = mySettingsComponent.getTextColor().getRGB();
        settings.selectedTextColor = mySettingsComponent.getSelectedTextColor().getRGB();
        settings.textKeepOldColor = mySettingsComponent.getTextKeepOldColor();
        settings.textKeepOldTextColor = mySettingsComponent.getTextKeepOldTextColor();

        Main.setTabVerticalBorders(settings.verticalBorder);
    }

    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();
        mySettingsComponent.setMaxNumberOfChars(settings.maxNumOfChars);
        mySettingsComponent.setTabsOffsetToTheRight(settings.tabsOffsetToTheRight);
        mySettingsComponent.setDisplayOnlyFileName(settings.displayOnlyFileName);
        mySettingsComponent.setAppend3Dots(settings.append3Dots);
        mySettingsComponent.setFillWithSpaces(settings.fillWithSpaces);
        mySettingsComponent.setBoldEditorTab(settings.boldEditorTab);
        mySettingsComponent.setVerticalBorder(settings.verticalBorder);

        mySettingsComponent.setOverrideEditorTabsFont(settings.overrideEditorTabsFont);
        mySettingsComponent.setFont(settings.fontName);
        mySettingsComponent.setFontSize(settings.fontSize);
        mySettingsComponent.setAlternateTabsColor(settings.alternateTabsColor);

        mySettingsComponent.setOverrideTabsColor(settings.overrideTabsColor);
        mySettingsComponent.setTabsColor(new Color(settings.tabsColor));
        mySettingsComponent.setSelectedTabColor(new Color(settings.selectedTabColor));

        mySettingsComponent.setOverrideTextColorOfTabs(settings.overrideTextColorOfTabs);
        mySettingsComponent.setTextColor(new Color(settings.textColor));
        mySettingsComponent.setSelectedTextColor(new Color(settings.selectedTextColor));
        mySettingsComponent.setSelectedTextColor(new Color(settings.selectedTextColor));

        mySettingsComponent.setTextKeepOldColor(settings.textKeepOldColor);
        mySettingsComponent.setTextKeepOldTextColor(settings.textKeepOldTextColor);

        mySettingsComponent.addListeners();
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

}
