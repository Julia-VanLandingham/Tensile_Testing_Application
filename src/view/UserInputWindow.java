package view;

import javax.swing.*;

/**
 * Creates a pop-up frame for user to input values used in calculations
 */
public class UserInputWindow extends JFrame {

    //constants used for formatting
    private static final int VERTICAL_BUFFER = 10;
    private static final int HORIZONTAL_BUFFER = 10;

    private JPanel rectangularInputPanel;
    private JPanel circularInputPanel;
    private JTextField gaugeLengthInputField;
    private JTextField diameterInputField;
    private JTextField widthInputField;
    private JTextField depthInputField;
    private JButton ok;
    private JButton cancel;
    private JRadioButton circular;
    private JRadioButton rectangular;

    public UserInputWindow(){
        setTitle("Measurements Input");

        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));

        outerPanel.add(createCrossSectionInputPanel());
        outerPanel.add(createGaugeLengthInputPanel());
        outerPanel.add(Box.createVerticalGlue());
        outerPanel.add(createOptionsPanel());

        setResizable(false);
        add(outerPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(false);
    }

    /*
     * Has the radio button for choosing the appropriate type for the
     * cross section of the specimen and then the associated panel for input
     */
    private JPanel createCrossSectionInputPanel(){
        JPanel crossSectionInputPanel = new JPanel();
        crossSectionInputPanel.setLayout(new BoxLayout(crossSectionInputPanel, BoxLayout.Y_AXIS));
        crossSectionInputPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Cross Section Type"), BorderFactory.createEmptyBorder(VERTICAL_BUFFER,HORIZONTAL_BUFFER,VERTICAL_BUFFER,HORIZONTAL_BUFFER)));
        setupRectangularInputPanel();
        setupCircularInputPanel();

        crossSectionInputPanel.add(createRadioButtonPanel());
        crossSectionInputPanel.add(Box.createVerticalStrut(VERTICAL_BUFFER));
        crossSectionInputPanel.add(Box.createVerticalGlue());
        crossSectionInputPanel.add(rectangularInputPanel);
        crossSectionInputPanel.add(circularInputPanel);
        circularInputPanel.setVisible(false);//because we have rectangular selected as default always

        return crossSectionInputPanel;
    }

    /*
     * Panel for formatting the radio button locations
     */
    private JPanel createRadioButtonPanel(){
        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.X_AXIS));

        ButtonGroup crossSectionButtons = new ButtonGroup();
        circular = new JRadioButton("Circular");
        rectangular = new JRadioButton("Rectangular");
        crossSectionButtons.add(rectangular);
        crossSectionButtons.add(circular);

        crossSectionButtons.setSelected(rectangular.getModel(), true);

        radioButtonPanel.add(rectangular);
        radioButtonPanel.add(Box.createHorizontalStrut(HORIZONTAL_BUFFER));
        radioButtonPanel.add(circular);
        radioButtonPanel.add(Box.createHorizontalGlue());

        return radioButtonPanel;
    }

    /*
     * Input for a rectangular cross section
     */
    private void setupRectangularInputPanel(){
        rectangularInputPanel = new JPanel();
        rectangularInputPanel.setLayout(new BoxLayout(rectangularInputPanel, BoxLayout.X_AXIS));

        JLabel widthLabel = new JLabel("Width (in): ");
        JLabel depthLabel = new JLabel("Depth (in): ");

        widthInputField = new JTextField(10);
        depthInputField = new JTextField(10);

        rectangularInputPanel.add(widthLabel);
        rectangularInputPanel.add(widthInputField);
        rectangularInputPanel.add(Box.createHorizontalStrut(HORIZONTAL_BUFFER));
        rectangularInputPanel.add(depthLabel);
        rectangularInputPanel.add(depthInputField);
        rectangularInputPanel.add(Box.createHorizontalGlue());
    }

    /*
     * Input for a circular cross section
     */
    private void setupCircularInputPanel(){
        circularInputPanel = new JPanel();
        circularInputPanel.setLayout(new BoxLayout(circularInputPanel, BoxLayout.X_AXIS));

        JLabel diameterLabel = new JLabel("Diameter (in): ");

        diameterInputField = new JTextField(10);

        circularInputPanel.add(diameterLabel);
        circularInputPanel.add(diameterInputField);
        circularInputPanel.add(Box.createHorizontalGlue());
    }

    /*
     * Initial gauge length input (will eventually be filled with a default value pulled from settings)
     */
    private JPanel createGaugeLengthInputPanel(){
        JPanel gaugeLengthInputPanel = new JPanel();
        gaugeLengthInputPanel.setLayout(new BoxLayout(gaugeLengthInputPanel, BoxLayout.X_AXIS));
        gaugeLengthInputPanel.setBorder(BorderFactory.createEmptyBorder(VERTICAL_BUFFER, HORIZONTAL_BUFFER,VERTICAL_BUFFER,HORIZONTAL_BUFFER));

        JLabel gaugeLengthLabel = new JLabel("Gauge Length:");
        gaugeLengthInputField = new JTextField("0.5", 10);

        gaugeLengthInputPanel.add(gaugeLengthLabel);
        gaugeLengthInputPanel.add(Box.createHorizontalStrut(HORIZONTAL_BUFFER));
        gaugeLengthInputPanel.add(gaugeLengthInputField);
        gaugeLengthInputPanel.add(Box.createHorizontalGlue());

        return gaugeLengthInputPanel;
    }

    /*
     * Ok and Cancel options
     */
    private JPanel createOptionsPanel(){
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(VERTICAL_BUFFER,HORIZONTAL_BUFFER,VERTICAL_BUFFER,HORIZONTAL_BUFFER));

        ok = new JButton("OK");
        cancel = new JButton("Cancel");

        optionsPanel.add(Box.createHorizontalGlue());
        optionsPanel.add(ok);
        optionsPanel.add(Box.createHorizontalStrut(HORIZONTAL_BUFFER));
        optionsPanel.add(cancel);

        return optionsPanel;
    }


    /*
     * Resets all the text fields back to their initial values (nothing or default values)
     */
    /** NOTE: This needs fixed, it does not do what it is supposed to do */
    /*
    private void clear(){
        gaugeLengthInputField.setText("0.5");
        widthInputField.setText("");
        depthInputField.setText("");
        diameterInputField.setText("");
    }
    */

    /**
     * Gets the width that was input from the user
     * @return the width value
     */
    public double getWidthInput(){
        return Double.parseDouble(widthInputField.getText().trim());
    }

    /**
     * Gets the depth that was input from the user
     * @return the depth value
     */
    public double getDepthInput(){
        return Double.parseDouble(depthInputField.getText().trim());
    }

    /**
     * Gets the diameter that was input from the user
     * @return the diameter value
     */
    public double getDiameterInput(){
        return Double.parseDouble(diameterInputField.getText().trim());
    }

    /**
     * Gets the gauge length that was input from the user (more likely this is still just the default value)
     * @return the gauge length value
     */
    public double getGaugeLengthInput(){
        return Double.parseDouble(gaugeLengthInputField.getText().trim());
    }

    //Getters
    public JRadioButton getCircularButton(){
        return circular;
    }

    public JRadioButton getRectangularButton(){
        return rectangular;
    }

    public JPanel getRectangularInputPanel(){
        return rectangularInputPanel;
    }

    public JPanel getCircularInputPanel(){
        return circularInputPanel;
    }

    public JTextField getGaugeLengthInputField(){ return gaugeLengthInputField;}

    public JButton getOkButton(){
        return ok;
    }

    public JButton getCancelButton(){
        return cancel;
    }
}

