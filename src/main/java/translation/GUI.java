package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.awt.*;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Translator translator = new JSONTranslator();
            CountryCodeConverter countryConverter = new CountryCodeConverter();
            LanguageCodeConverter languageConverter = new LanguageCodeConverter();

            JPanel countryPanel = new JPanel();
            countryPanel.setLayout(new BorderLayout());

            DefaultListModel<String> countryListModel = new DefaultListModel<>();
            for(String countryCode : translator.getCountryCodes()) {
                String countryName = countryConverter.fromCountryCode(countryCode);
                if (countryName != null) {
                    countryListModel.addElement(countryName);
                }
            }

            JList<String> countryList = new JList<>(countryListModel);
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(countryList);
            scrollPane.setPreferredSize(new Dimension(300, 150));
            countryPanel.add(scrollPane, BorderLayout.CENTER);


            JPanel languagePanel = new JPanel();
            JLabel languageLabel = new JLabel("Language");
            languagePanel.add(languageLabel);
            JComboBox<String> languageCombo = new JComboBox<>();
            for(String languageCode : translator.getLanguageCodes()) {
                String languageName = languageConverter.fromLanguageCode(languageCode);
                if (languageName != null) {
                    languageCombo.addItem(languageName);
                }
            }
            languagePanel.add(languageCombo);

            JPanel buttonPanel = new JPanel();
            JLabel resultLabelText = new JLabel("Translation:");
            JLabel resultLabel = new JLabel("");
            buttonPanel.add(resultLabelText);
            buttonPanel.add(resultLabel);

            Runnable updateTranslation = () -> {
                String selectedCountryName = countryList.getSelectedValue();
                String selectedLanguageName = (String) languageCombo.getSelectedItem();

                if (selectedCountryName != null && selectedLanguageName != null) {
                    String countryCode = countryConverter.fromCountry(selectedCountryName);
                    String languageCode = languageConverter.fromLanguage(selectedLanguageName);

                    if (countryCode != null && languageCode != null) {
                        String result = translator.translate(countryCode, languageCode);
                        if (result != null) {
                            resultLabel.setText(result);
                        } else {
                            resultLabel.setText("No translation found!");
                        }
                    }
                } else {
                    resultLabel.setText("");
                }
            };


            languageCombo.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        updateTranslation.run();
                    }
                }
            });

            countryList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        updateTranslation.run();
                    }
                }
            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);
            mainPanel.add(countryPanel);


            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}
