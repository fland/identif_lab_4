package ua.pp.fland.labs.identif.lab4.gui;

import org.apache.log4j.Logger;
import ua.pp.fland.labs.identif.lab4.gui.tools.BoxLayoutUtils;
import ua.pp.fland.labs.identif.lab4.gui.tools.ComponentUtils;
import ua.pp.fland.labs.identif.lab4.gui.tools.GUITools;
import ua.pp.fland.labs.identif.lab4.gui.tools.StandardBordersSizes;
import ua.pp.fland.labs.identif.lab4.model.TemperatureCalculator;
import ua.pp.fland.labs.identif.lab4.model.storage.CsvTimeTemperatureStorer;
import ua.pp.fland.labs.identif.lab4.model.storage.TimeTemperatureStorer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Maxim Bondarenko
 * @version 1.0 9/29/11
 */

public class MainWindow {
    private static final Logger log = Logger.getLogger(MainWindow.class);

    private final static ResourceBundle bundle = ResourceBundle.getBundle("lab4");

    private final static Dimension MAIN_FRAME_SIZE = new Dimension(Integer.parseInt(bundle.getString("window.width")),
            Integer.parseInt(bundle.getString("window.height")));

    private final static String PROCESS_BTN_TEXT = "Process";
    private final static String PROCESS_WEIGHTED_BTN_TEXT = "Process Weighted";
    private final static String EXIT_BTN_TEXT = "Exit";

    private final static String CAST_IRON_START_TEMPERATURE_LABEL = "Cast iron start temperature, C: ";
    private final static String CAST_IRON_MASS_DEVIATION_LABEL = "Cast iron mass deviation(tons): ";
    private final static String TIME_TRANSPORT_DEVIATION = "Transport time deviation:";
    private final static String TRANSPORT_TIME_TO_SLUG_REMOVAL_DEVIATION_LABEL = "To slug removal department(min): ";
    private final static String TRANSPORT_TIME_TO_MIXER_DEVIATION_LABEL = "To mixer department(min): ";
    private final static String DESULFURATION_COUNT = "Desulfurations count: ";
    private final static String LADLES_COUNT_DEVIATION = "Ladle count deviation: ";

    private final JFrame mainFrame;

    private final JTextField castIronStartTemperatureInput;
    private final JTextField castIronMassDeviationInput;
    private final JTextField slugRemovalDepTransTimeDeviationInput;
    private final JTextField mixerDepTransTimeDeviationInput;
    private final JTextField desulfurationCountInput;
    private final JTextField ladleCountDeviationInput;

    public MainWindow() {
        mainFrame = new JFrame("Lab 4");
        mainFrame.setSize(MAIN_FRAME_SIZE);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        castIronMassDeviationInput = new JTextField("10");
        GUITools.fixTextFieldSize(castIronMassDeviationInput);
        castIronMassDeviationInput.setCaretPosition(0);

        slugRemovalDepTransTimeDeviationInput = new JTextField("60");
        GUITools.fixTextFieldSize(slugRemovalDepTransTimeDeviationInput);
        slugRemovalDepTransTimeDeviationInput.setCaretPosition(0);

        mixerDepTransTimeDeviationInput = new JTextField("60");
        GUITools.fixTextFieldSize(mixerDepTransTimeDeviationInput);
        mixerDepTransTimeDeviationInput.setCaretPosition(0);

        desulfurationCountInput = new JTextField("1");
        GUITools.fixTextFieldSize(desulfurationCountInput);
        desulfurationCountInput.setCaretPosition(0);

        ladleCountDeviationInput = new JTextField("0");
        GUITools.fixTextFieldSize(ladleCountDeviationInput);
        ladleCountDeviationInput.setCaretPosition(0);

        castIronStartTemperatureInput = new JTextField("1450");
        GUITools.fixTextFieldSize(castIronStartTemperatureInput);
        castIronStartTemperatureInput.setCaretPosition(0);

        final JPanel mainPanel = BoxLayoutUtils.createVerticalPanel();
        mainPanel.setBorder(new EmptyBorder(StandardBordersSizes.MAIN_BORDER.getValue()));
        ComponentUtils.setSize(mainPanel, MAIN_FRAME_SIZE.width, MAIN_FRAME_SIZE.height);

        mainPanel.add(createInputPanels());
        mainPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));
        mainPanel.add(createButtonsPanel(mainFrame));

        mainFrame.add(mainPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private JPanel createButtonsPanel(final JFrame mainFrame) {
        JPanel buttonsPanel = BoxLayoutUtils.createHorizontalPanel();

        JButton processButton = new JButton(PROCESS_BTN_TEXT);
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                log.debug("Process btn pressed");
                try {
                    TemperatureCalculator temperatureCalculator = new TemperatureCalculator(
                            Integer.parseInt(castIronMassDeviationInput.getText()),
                            Integer.parseInt(slugRemovalDepTransTimeDeviationInput.getText()),
                            Integer.parseInt(mixerDepTransTimeDeviationInput.getText()),
                            Integer.parseInt(desulfurationCountInput.getText()),
                            Integer.parseInt(ladleCountDeviationInput.getText()));
                    Map<Integer, Float> timeTemperature =
                            new HashMap<Integer, Float>(temperatureCalculator.calculateTemperature(
                                    Integer.parseInt(castIronStartTemperatureInput.getText())));
                    JFileChooser fileChooser = new JFileChooser() {
                        @Override
                        public void approveSelection() {
                            File selectedFile = getSelectedFile();
                            if (selectedFile.exists() && getDialogType() == SAVE_DIALOG) {
                                int result = JOptionPane.showConfirmDialog(this, "File " + selectedFile.getName() +
                                        " exist. Overwrite it?", "Overwrite file dialog", JOptionPane.YES_NO_CANCEL_OPTION);
                                switch (result) {
                                    case JOptionPane.YES_OPTION:
                                        super.approveSelection();
                                        return;
                                    case JOptionPane.NO_OPTION:
                                        return;
                                    case JOptionPane.CANCEL_OPTION:
                                        super.cancelSelection();
                                        return;
                                }
                            }
                            super.approveSelection();
                        }
                    };
                    fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files (*.csv)", "csv"));
                    int result = fileChooser.showSaveDialog(mainFrame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        String path = fileChooser.getSelectedFile().getAbsolutePath();
                        if (!path.toLowerCase().endsWith(".csv")) {
                            path = path + ".csv";
                        }
                        log.debug("Storing data to: " + path);
                        TimeTemperatureStorer timeTemperatureStorer = new CsvTimeTemperatureStorer(path);
                        timeTemperatureStorer.store(timeTemperature, 1);
                        log.debug("Data stored.");
                        JOptionPane.showMessageDialog(mainFrame, "Temperature calculated and stored to " + path, "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(mainFrame, "Cannot parse input from string to numbers. " +
                            "\nCheck your input", "I/O Error", JOptionPane.ERROR_MESSAGE);
                    log.error("Cannot parse input string to int: " + e, e);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(mainFrame, "Unexpected I/O error:\n" + e, "I/O Error",
                            JOptionPane.ERROR_MESSAGE);
                    log.error("Exception: " + e, e);
                }
            }
        });

        JButton exitButton = new JButton(EXIT_BTN_TEXT);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("Exit btn pressed");
                shutdown();
            }
        });

        GUITools.createRecommendedMargin(processButton, exitButton);
        GUITools.makeSameSize(processButton, exitButton);

        buttonsPanel.add(processButton);
        buttonsPanel.add(Box.createRigidArea(StandardDimension.HOR_RIGID_AREA.getValue()));
        buttonsPanel.add(Box.createRigidArea(StandardDimension.HOR_RIGID_AREA.getValue()));
        buttonsPanel.add(exitButton);

        return buttonsPanel;
    }

    private JPanel createInputPanels() {
        JPanel inputsPanel = BoxLayoutUtils.createVerticalPanel();

        JPanel tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel castIronStartTempLabel = new JLabel(CAST_IRON_START_TEMPERATURE_LABEL);
        tempHorPanel.add(castIronStartTempLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(castIronStartTemperatureInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel castIronMassDeviationLabel = new JLabel(CAST_IRON_MASS_DEVIATION_LABEL);
        tempHorPanel.add(castIronMassDeviationLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(castIronMassDeviationInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel transportTimeDeviationLabel = new JLabel(TIME_TRANSPORT_DEVIATION);
        tempHorPanel.add(transportTimeDeviationLabel);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_HALF_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel slugRemovalDepDevLabel = new JLabel(TRANSPORT_TIME_TO_SLUG_REMOVAL_DEVIATION_LABEL);
        tempHorPanel.add(slugRemovalDepDevLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(slugRemovalDepTransTimeDeviationInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_HALF_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel mixerDepDevLabel = new JLabel(TRANSPORT_TIME_TO_MIXER_DEVIATION_LABEL);
        tempHorPanel.add(mixerDepDevLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(mixerDepTransTimeDeviationInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel desulfurationCountLabel = new JLabel(DESULFURATION_COUNT);
        tempHorPanel.add(desulfurationCountLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(desulfurationCountInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel ladlesCountDeviationLabel = new JLabel(LADLES_COUNT_DEVIATION);
        tempHorPanel.add(ladlesCountDeviationLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(ladleCountDeviationInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        GUITools.makeSameSize(castIronMassDeviationLabel, transportTimeDeviationLabel, slugRemovalDepDevLabel,
                mixerDepDevLabel, desulfurationCountLabel, ladlesCountDeviationLabel, castIronStartTempLabel);

        return inputsPanel;
    }

    private void shutdown() {
        mainFrame.setVisible(false);
        mainFrame.dispose();
    }
}
