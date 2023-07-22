import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimeZone extends JFrame implements ActionListener {
    private JComboBox<String> fromComboBox;
    private JComboBox<String> toComboBox;
    private JLabel fromLabel;
    private JLabel toLabel;
    private JLabel convertedTimeLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TimeZone app = new TimeZone();
            app.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        setTitle("TimeZone Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        createComponents();
        addComponentsToPane();
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true); 
    }

    private void createComponents() {
        fromLabel = createLabel("From Time:");
        toLabel = createLabel("To Time:");
        convertedTimeLabel = new JLabel();
        convertedTimeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        convertedTimeLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        fromComboBox = createComboBox();
        toComboBox = createComboBox();

        createConvertButton();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private JComboBox<String> createComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        List<String> timeZoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds());
        for (String timeZoneId : timeZoneIds) {
            comboBox.addItem(timeZoneId);
        }
        return comboBox;
    }

    private void createConvertButton() {
                   JButton convertButton = new JButton("Convert");
            convertButton.addActionListener(this);
            
            Font buttonFont = convertButton.getFont();
            FontMetrics fontMetrics = convertButton.getFontMetrics(buttonFont);
            int textWidth = fontMetrics.stringWidth(convertButton.getText());
            int textHeight = fontMetrics.getHeight();
            int preferredWidth = textWidth + 40; 
            int preferredHeight = textHeight + 10;
            convertButton.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(convertButton);
        
            add(buttonPanel, BorderLayout.SOUTH);
        }

    private void addComponentsToPane() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("TimeZone Converter");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);

        JPanel comboBoxPanel = new JPanel(new GridLayout(2, 2, 20, 10));
        comboBoxPanel.setBorder(BorderFactory.createTitledBorder("Time Zones"));
        comboBoxPanel.setBackground(Color.WHITE);
        comboBoxPanel.add(fromLabel);
        comboBoxPanel.add(fromComboBox);
        comboBoxPanel.add(toLabel);
        comboBoxPanel.add(toComboBox);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(comboBoxPanel, BorderLayout.CENTER);
        mainPanel.add(convertedTimeLabel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Convert")) {
            String fromTimeZone = (String) fromComboBox.getSelectedItem();
            String toTimeZone = (String) toComboBox.getSelectedItem();

            LocalDateTime now = LocalDateTime.now();
            ZonedDateTime fromDateTime = ZonedDateTime.of(now, ZoneId.of(fromTimeZone));
            ZonedDateTime toDateTime = fromDateTime.withZoneSameInstant(ZoneId.of(toTimeZone));

            String fromCountry = getCountryName(fromTimeZone);
            String toCountry = getCountryName(toTimeZone);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String convertedFromTime = fromDateTime.format(formatter);
            String convertedToTime = toDateTime.format(formatter);

            convertedTimeLabel.setText("From " + fromCountry + ": " + convertedFromTime +
                    "  |  To " + toCountry + ": " + convertedToTime);
        }
    }

    private String getCountryName(String timeZoneId) {
        String[] parts = timeZoneId.split("/");
        if (parts.length == 2) {
            String country = parts[0].replace("_", " ");
            return toTitleCase(country);
        }
        return "";
    }

    private String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}
