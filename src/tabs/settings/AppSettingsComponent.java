// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package tabs.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.picker.ColorListener;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;
import tabs.settings.listeners.ColorListenerImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AppSettingsComponent {

    private final JPanel myMainPanel;
    private final JBTextField maxEditorTabChars = new JBTextField();
    private final JBCheckBox fillWithSpaces = new JBCheckBox("Make all tabs appear with uniform lengths even if their length is less than the desired maximum. Works best with monospaced fonts.(*)");
    private final JBCheckBox displayOnlyFileName = new JBCheckBox("Display only file name in the editor tab tooltip (*)");
    private final JBCheckBox boldEditorTab = new JBCheckBox("Make the selected editor tab BOLD ");
    private final JBCheckBox verticalBorder = new JBCheckBox("Add vertical tab border (*)");

    private final JBTextField tabsOffsetToTheRight = new JBTextField();

    private final JComboBox<String> alternateTabsColorMenu = new JComboBox<>();

    // Tabs Font
    private final JBCheckBox overrideTabsColor = new JBCheckBox("Override tabs color. Has precedence over experimental features");

    private final JButton selectedTabColorBtn = new JButton("Select");

    private final JButton tabsColorBtn = new JButton("Select");

    // Tabs Text Color
    private final JBCheckBox overrideTextColorOfTabs = new JBCheckBox("Override tabs Text color.  Has precedence over experimental features");

    private final JButton selectedTextColorBtn = new JButton("Select");

    private final JButton textColorBtn = new JButton("Select");

    private final JBCheckBox textKeepOldColor = new JBCheckBox("Don't override inactive tabs colors");
    private final JBCheckBox textKeepOldTextColor = new JBCheckBox("Don't override inactive tabs Text colors");

    private final JBCheckBox overrideEditorTabsFont = new JBCheckBox("Override editor tabs font");

    private final JComboBox<String> fontsMenu = new JComboBox<>();

    private final JComboBox<String> fontSizes = new JComboBox<>();

    private final JTextField preview = new JTextField();

    private final ColorListener selectedTabListener = new ColorListenerImpl(selectedTabColorBtn);
    private final ColorListener tabsListener = new ColorListenerImpl(tabsColorBtn);

    private final ColorListener selectedTextListener = new ColorListenerImpl(selectedTextColorBtn);
    private final ColorListener textListener = new ColorListenerImpl(textColorBtn);

    public AppSettingsComponent() {
        populateFontsMenu(fontsMenu);
        populateFontSizes(fontSizes);

        populateTabColors();
        populateTextColors();

        //addListeners();
        populateAlternateMenu();

        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Display max number of characters in the editor tabs: (*) "), maxEditorTabChars, 1, false)
                .addComponent(fillWithSpaces, 1)
                .addComponent(displayOnlyFileName, 1)
                .addComponent(boldEditorTab, 1)
                .addComponent(verticalBorder, 1)
                .addLabeledComponent(new JBLabel("Experimental: Alternate tabs background color for:  (*) . Disabling it requires restart."), alternateTabsColorMenu)
                .addVerticalGap(45)
                .addComponent(new JSeparator(), 1)
                // tabs color
                .addComponent(overrideTabsColor, 1)
                .addLabeledComponent(new JBLabel("Inactive tabs color "), tabsColorBtn, 1, false)
                .addComponent(textKeepOldColor)
                .addVerticalGap(25)
                .addLabeledComponent(new JBLabel("Selected tab color "), selectedTabColorBtn, 1, false)
                .addVerticalGap(45)
                .addComponent(new JSeparator(), 1)
                // tabs text color
                .addComponent(overrideTextColorOfTabs, 1)
                .addLabeledComponent(new JBLabel("Inactive tabs Text color "), textColorBtn, 1, false)
                .addComponent(textKeepOldTextColor)
                .addVerticalGap(25)
                .addLabeledComponent(new JBLabel("Selected tab Text color "), selectedTextColorBtn, 1, false)
                .addVerticalGap(45)
                .addComponent(new JSeparator(), 1)
                .addComponent(overrideEditorTabsFont, 1)
                .addVerticalGap(15)
                .addLabeledComponent(new JBLabel("Leave space between tabs (in pixels): "), tabsOffsetToTheRight, 1, false)
                .addVerticalGap(15)
                .addComponent(new JBLabel("Font type:"))
                .addComponent(fontsMenu, 1)
                .addComponent(new JBLabel("Size:"))
                .addComponent(fontSizes, 1)
                .addComponentToRightColumn(new JBLabel("Preview:"))
                .addComponentToRightColumn(preview)


                .addComponentFillVertically(new JPanel(), 0)
                .addVerticalGap(15)
                .addComponent(new JBLabel("Changes marked with (*) require restart."))
                .getPanel();
    }

    private void populateTabColors() {
        tabsColorBtn.setBackground(getTabsColor());
        selectedTabColorBtn.setBackground(getSelectedTabColor());
    }

    private void populateTextColors() {
        textColorBtn.setBackground(getTextColor());
        selectedTextColorBtn.setBackground(getSelectedTextColor());
    }

    private void populateAlternateMenu() {
        alternateTabsColorMenu.addItem("off");
        alternateTabsColorMenu.addItem("Dark themes");
        alternateTabsColorMenu.addItem("Light themes");
    }

    public void addListeners() {
        fontsMenu.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    preview.setFont(new Font((String) e.getItem(), Font.PLAIN, Integer.parseInt((String) fontSizes.getSelectedItem())));

                    preview.setText(getSampleText());
                }
            }
        });


        fontSizes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    preview.setFont(new Font((String) fontsMenu.getSelectedItem(), Font.PLAIN, Integer.parseInt((String) fontSizes.getSelectedItem())));

                    preview.setText(getSampleText());
                }
            }
        });

        final Color tabsColorFinal = getTabsColor();

        tabsColorBtn.addActionListener(new ActionListener() {
                                          @Override
                                        public void actionPerformed(ActionEvent e) {

                                            ColorChooserServiceImpl.getInstance().showColorPickerPopup(null, tabsColorFinal,
                                                    tabsListener, null, false);

                                          }
                                    }
        );

        final Color selectedTabColorBtnColorFinal = getSelectedTabColor();

        selectedTabColorBtn.addActionListener(new ActionListener() {
                                          @Override
                                               public void actionPerformed(ActionEvent e) {

                                                   ColorChooserServiceImpl.getInstance().showColorPickerPopup(null, selectedTabColorBtnColorFinal,
                                                           selectedTabListener, null, false);

                                                 }
                                             }
        );

        // text color
        final Color textColorFinal = getTextColor();

        textColorBtn.addActionListener(new ActionListener() {
                                           @Override
                                           public void actionPerformed(ActionEvent e) {

                                               ColorChooserServiceImpl.getInstance().showColorPickerPopup(null, textColorFinal,
                                                       textListener, null, false);

                                           }
                                       }
        );

        final Color selectedTextColorBtnColorFinal = getSelectedTextColor();

        selectedTextColorBtn.addActionListener(new ActionListener() {
                                                  @Override
                                                  public void actionPerformed(ActionEvent e) {

                                                      ColorChooserServiceImpl.getInstance().showColorPickerPopup(null, selectedTextColorBtnColorFinal,
                                                              selectedTextListener, null, false);

                                                  }
                                              }
        );

    }

    private String getSampleText() {
//        try {
//            final int length = Integer.parseInt(maxEditorTabChars.getText());
//            if ("Test_tabPreview.extension".length() <= length) {
//                return "Test_tabPreview.extension".substring(0, length);
//            } else {
//                return "Test_tabPreview.extension";
//            }
//        } catch (IllegalArgumentException e) {
//            System.out.println(e.getMessage());
//            return "Error parsing max length: " + e.getMessage();
//        }
        return "Test_tabPreview.extension";
    }

    public boolean getOverrideEditorTabsFont() {
        return overrideEditorTabsFont.isSelected();
    }

    public boolean getOverrideTabsColor() {
        return overrideTabsColor.isSelected();
    }

    public void setOverrideTabsColor(boolean val) {
        overrideTabsColor.setSelected(val);
    }

    public boolean getOverrideTextColorOfTabs() {
        return overrideTextColorOfTabs.isSelected();
    }

    public void setOverrideTextColorOfTabs(boolean val) {
        overrideTextColorOfTabs.setSelected(val);
    }

    public String getFontSize() {
        return (String) fontSizes.getSelectedItem();
    }

    public void setFontSize(String val) {
        fontSizes.setSelectedItem(val);
    }

    public String getAlternateTabsColor() {
        return (String) alternateTabsColorMenu.getSelectedItem();
    }

    public void setAlternateTabsColor(String val) {
        alternateTabsColorMenu.setSelectedItem(val);
    }

    public String getFont() {
        return (String) fontsMenu.getSelectedItem();
    }

    public void setFont(String val) {
        fontsMenu.setSelectedItem(val);
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return maxEditorTabChars;
    }

    @NotNull
    public String getMaxNumberOfChars() {
        return maxEditorTabChars.getText();
    }

    public void setMaxNumberOfChars(@NotNull String newText) {
        maxEditorTabChars.setText(newText);
    }

    @NotNull
    public String getTabsOffsetToTheRight() {
        return tabsOffsetToTheRight.getText();
    }

    public void setTabsOffsetToTheRight(@NotNull String newText) {
        tabsOffsetToTheRight.setText(newText);
    }

    public boolean getDisplayOnlyFileName() {
        return displayOnlyFileName.isSelected();
    }

    public void setDisplayOnlyFileName(boolean newStatus) {
        displayOnlyFileName.setSelected(newStatus);
    }

    public boolean getFillWithSpaces() {
        return fillWithSpaces.isSelected();
    }

    public void setFillWithSpaces(boolean newStatus) {
        fillWithSpaces.setSelected(newStatus);
    }

    public boolean getBoldEditorTab() {
        return boldEditorTab.isSelected();
    }

    public void setBoldEditorTab(boolean newStatus) {
        boldEditorTab.setSelected(newStatus);
    }

    public boolean getVerticalBorder() {
        return verticalBorder.isSelected();
    }

    public void setVerticalBorder(boolean newStatus) {
        verticalBorder.setSelected(newStatus);
    }

    public void setOverrideEditorTabsFont(boolean newStatus) {
        overrideEditorTabsFont.setSelected(newStatus);
    }

    private void populateFontsMenu(JComboBox<String> list) {
        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

        for (Font font : fonts) {
            list.addItem(font.getName());
        }
    }

    private void populateFontSizes(JComboBox<String> list) {
        for (int i = 5; i < 50; i++) {
            list.addItem("" + i);
        }
    }

    public Color getSelectedTabColor() {
        return selectedTabColorBtn.getBackground();
    }

    public void setSelectedTabColor(Color selectedTabColor) {
        selectedTabColorBtn.setBackground(selectedTabColor);
    }

    public Color getTabsColor() {
        return tabsColorBtn.getBackground();
    }

    public void setTabsColor(Color tabsColor) {
        tabsColorBtn.setBackground(tabsColor);
    }

    public Color getTextColor() {
        return textColorBtn.getBackground();
    }

    public void setTextColor(Color textColor) {
        textColorBtn.setBackground(textColor);
    }

    public Color getSelectedTextColor() {
        return selectedTextColorBtn.getBackground();
    }

    public void setSelectedTextColor(Color textColor) {
        selectedTextColorBtn.setBackground(textColor);
    }

    public boolean getTextKeepOldColor() {
        return textKeepOldColor.isSelected();
    }

    public void setTextKeepOldColor(boolean newStatus) {
        textKeepOldColor.setSelected(newStatus);
    }

    public boolean getTextKeepOldTextColor() {
        return textKeepOldTextColor.isSelected();
    }

    public void setTextKeepOldTextColor(boolean newStatus) {
        textKeepOldTextColor.setSelected(newStatus);
    }
}
