package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import translation.*;

public class GUI {

    private static void updateResult(JSONTranslator translator, LanguageCodeConverter languageConverter, CountryCodeConverter countryConverter, String selectedLanguageName, String selectedCountryName, JLabel resultLabel) {
        if (selectedLanguageName == null || selectedCountryName == null) {
            return;
        }
        String langCode = languageConverter.fromLanguage(selectedLanguageName);
        String countryCode = countryConverter.fromCountry(selectedCountryName);
        String result = translator.translate(countryCode, langCode);
        if (result == null) result = "no translation found!";
        resultLabel.setText(result);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JSONTranslator translator = new JSONTranslator();
            LanguageCodeConverter languageConverter = new LanguageCodeConverter();
            CountryCodeConverter countryConverter = new CountryCodeConverter();

            JPanel languagePanel = new JPanel();
            languagePanel.setLayout(new GridLayout(2, 2));
            languagePanel.add(new JLabel("Language:"), 0);

            final String[] selectedCountry = {null};
            final String[] selectedLanguage = {null};


            JComboBox<String> languageComboBox = new JComboBox<>();
            System.out.println(translator.getLanguageCodes());
            for(String languageCode : translator.getLanguageCodes()) {
                languageComboBox.addItem(languageConverter.fromLanguageCode(languageCode));
            }
            languagePanel.add(languageComboBox);


            JPanel countryPanel = new JPanel();
            countryPanel.setLayout(new GridLayout(1, 0));
            countryPanel.add(new JLabel("Language:"), 0);

            JPanel scrollLangPanel = new JPanel();
            scrollLangPanel.setLayout(new GridLayout(0, 1));

            String[] items = new String[translator.getCountryCodes().size()];
            JComboBox<String> scrollLangComboBox = new JComboBox<>();
            int i = 0;
            for(String countryCode : translator.getCountryCodes()) {
                items[i] = countryConverter.fromCountryCode(countryCode);
                i++;
            }

            JList<String> countryList = new JList<>(items);
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(countryList);
            scrollLangPanel.add(scrollPane);

            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");

            JPanel buttonPanel = new JPanel();
            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            buttonPanel.add(resultLabel);
            languageComboBox.addItemListener(new ItemListener() {

                /**
                 * Invoked when an item has been selected or deselected by the user.
                 * The code written for this method performs the operations
                 * that need to occur when an item is selected (or deselected).
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void itemStateChanged(ItemEvent e) {

                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String language = languageComboBox.getSelectedItem().toString();
                        selectedLanguage[0] = language;
                        updateResult(translator, languageConverter, countryConverter, selectedLanguage[0], selectedCountry[0], resultLabel );
                    }
                }


            });

            countryList.addListSelectionListener((e) -> {
                String country = countryList.getSelectedValue().toString();
                if (country != null) {
                        selectedCountry[0] = country;
                        updateResult(translator, languageConverter, countryConverter, selectedLanguage[0], selectedCountry[0], resultLabel );
                    }
            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);
            mainPanel.add(scrollLangPanel);

            // Set default values
            if (languageComboBox.getItemCount() > 0) {
                languageComboBox.setSelectedIndex(0);
                selectedLanguage[0] = (String) languageComboBox.getSelectedItem();
            }
            if (countryList.getModel().getSize() > 0) {
                countryList.setSelectedIndex(0);
                selectedCountry[0] = countryList.getSelectedValue();
            }

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

        });
    }
}
