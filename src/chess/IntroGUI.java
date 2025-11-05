package chess;

import javax.swing.*;
import java.awt.*;
//import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IntroGUI extends JPanel {
    private List<String> openingsList;

    public IntroGUI(Runnable freePlayClick, Consumer<String> addWhiteOpening) {
        setSize(600, 600);
        setLayout(new GridLayout(4, 2));
        openingsList = Database.getAllOpenings();

        JLabel chooseOpeningLabel = new JLabel("Choose Opening: ");
        JComboBox<String> openingsDropdown = new JComboBox<>(openingsList.toArray(new String[0]));
        JButton addOpeningButton = new JButton("Add Opening");
        JTextField newOpeningTextField = new JTextField(10);
        JButton startButton = new JButton("Start");
        JButton addLineButton = new JButton("Add Line");
        JCheckBox isWhiteOpeningCheckbox = new JCheckBox("Is White Opening?");
        JButton freePlayButton = new JButton("Free Play");

        freePlayButton.addActionListener(e -> freePlayClick.run());
        addOpeningButton.addActionListener(e -> {
            addWhiteOpening.accept(newOpeningTextField.getText());
        });

//line 1
        add(chooseOpeningLabel);
        add(newOpeningTextField);

//line 2
        add(openingsDropdown);
        add(isWhiteOpeningCheckbox);


//line 3
        add(addLineButton);
        add(addOpeningButton);
        //Line 4
        add(startButton);
        add(freePlayButton);




    }
}
