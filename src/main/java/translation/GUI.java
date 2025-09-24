package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import translation.*;

// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CanadaTranslator translator = new CanadaTranslator();
            LanguageCodeConverter languageConverter = new LanguageCodeConverter();
            CountryCodeConverter countryConverter = new CountryCodeConverter();

            JPanel languagePanel = new JPanel();
            languagePanel.setLayout(new GridLayout(2, 2));
            languagePanel.add(new JLabel("Language:"), 0);

            final String[] selectedCountry = {null};
            final String[] selectedLanguage = {null};


            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String countryCode : translator.getLanguageCodes()) {
                languageComboBox.addItem(languageConverter.fromLanguage(countryCode));
            }
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
                        String country = languageComboBox.getSelectedItem().toString();
                        selectedCountry[0] = country;
                    }
                }


            });

            languagePanel.add(languageComboBox);


            //

            JPanel countryPanel = new JPanel();
            countryPanel.setLayout(new GridLayout(0, 2));
            countryPanel.add(new JLabel("Language:"), 0);
            JTextField countryField = new JTextField(10);

            JTextField languageField = new JTextField(10);

            //
            JPanel scrollLangPanel = new JPanel();
            scrollLangPanel.setLayout(new GridLayout(0, 2));

            String[] items = new String[translator.getLanguageCodes().size()];

            JComboBox<String> scrollLangComboBox = new JComboBox<>();
            int i = 0;
            for(String countryCode : translator.getCountryCodes()) {
                items[i++] = countryConverter.fromCountryCode(countryCode);
            }

            // create the JList with the array of strings and set it to allow multiple
            // items to be selected at once.
            JList<String> list = new JList<>(items);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            // place the JList in a scroll pane so that it is scrollable in the UI
            JScrollPane scrollPane = new JScrollPane(list);
            scrollLangPanel.add(scrollPane);

            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");


            list.addListSelectionListener(new ListSelectionListener() {

                /**
                 * Called whenever the value of the selection changes.
                 *
                 * @param e the event that characterizes the change.
                 */
                @Override
                public void valueChanged(ListSelectionEvent e) {

                    int[] indices = list.getSelectedIndices();
                    String[] items = new String[indices.length];
                    for (int i = 0; i < indices.length; i++) {
                        items[i] = list.getModel().getElementAt(indices[i]);
                    }
                    selectedLanguage[0] = items[list.getSelectedIndex()];
//                    JOptionPane.showMessageDialog(null, "User selected:" +
//                            System.lineSeparator() + Arrays.toString(items));
                    Translator translator = new JSONTranslator();
                    String countryCode = countryConverter.fromCountry(selectedCountry[0]);
                    String languageCode = languageConverter.fromLanguage(selectedLanguage[0]);

                    String result = translator.translate(countryCode, languageCode);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);

                }
            });













            //

            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Submit");
            buttonPanel.add(submit);

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            buttonPanel.add(resultLabel);


            // adding listener for when the user clicks the submit button
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String language = languageField.getText();
                    String country = countryField.getText();

                    // for now, just using our simple translator, but
                    // we'll need to use the real JSON version later.
                    Translator translator = new CanadaTranslator();

                    String result = translator.translate(country, language);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);

                }

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);
            mainPanel.add(scrollLangPanel);

//            mainPanel.add(countryPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}
