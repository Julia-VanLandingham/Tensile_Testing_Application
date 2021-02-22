package view;

import controller.InputController;

import javax.swing.*;
import java.awt.*;

public class UserInputWindow extends JFrame {

    private static final int VERTICAL_BUFFER = 10;
    private static final int HORIZONTAL_BUFFER = 10;

    private JPanel rectangularInputPanel;
    private JPanel circularInputPanel;
    private JTextField elongationInputField;
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
        outerPanel.add(createInitialElongationInputPanel());
        outerPanel.add(Box.createVerticalGlue());
        outerPanel.add(createOptionsPanel());

        add(outerPanel);
        pack();
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

        widthInputField = new JTextField();
        depthInputField = new JTextField();

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

        diameterInputField = new JTextField();

        circularInputPanel.add(diameterLabel);
        circularInputPanel.add(diameterInputField);
        circularInputPanel.add(Box.createHorizontalGlue());
    }

    /*
     * Initial elongation input (will eventually be filled with a default value pulled from settings)
     */
    private JPanel createInitialElongationInputPanel(){
        JPanel initialElongationInputPanel = new JPanel();
        initialElongationInputPanel.setLayout(new BoxLayout(initialElongationInputPanel, BoxLayout.X_AXIS));
        initialElongationInputPanel.setBorder(BorderFactory.createEmptyBorder(VERTICAL_BUFFER, HORIZONTAL_BUFFER,VERTICAL_BUFFER,HORIZONTAL_BUFFER));

        JLabel elongationLabel = new JLabel("Initial Elongation (in):");
        elongationInputField = new JTextField("0.5");

        initialElongationInputPanel.add(elongationLabel);
        initialElongationInputPanel.add(Box.createHorizontalStrut(HORIZONTAL_BUFFER));
        initialElongationInputPanel.add(elongationInputField);
        initialElongationInputPanel.add(Box.createHorizontalGlue());

        return initialElongationInputPanel;
    }

    private JPanel createOptionsPanel(){
        JPanel optionsPanel = new JPanel(new GridLayout(1,2,HORIZONTAL_BUFFER,0));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(VERTICAL_BUFFER,HORIZONTAL_BUFFER,VERTICAL_BUFFER,HORIZONTAL_BUFFER));

        ok = new JButton("Ok");
        cancel = new JButton("Cancel");

        optionsPanel.add(ok);
        optionsPanel.add(cancel);

        return optionsPanel;
    }

    private void clear(){
    }

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

    public JButton getOkButton(){
        return ok;
    }

    public JButton getCancelButton(){
        return cancel;
    }

    public double getWidthInput(){
        return Double.parseDouble(widthInputField.getText());
    }

    public double getDepthInput(){
        return Double.parseDouble(depthInputField.getText());
    }

    public double getDiameterInput(){
        return Double.parseDouble(diameterInputField.getText());
    }

    public double getElongationInput(){
        return Double.parseDouble(elongationInputField.getText());
    }
}

