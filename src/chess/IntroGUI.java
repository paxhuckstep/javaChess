package chess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IntroGUI extends JPanel {
    private List<String> openingsList = new ArrayList<>();

    public IntroGUI(Runnable freePlayClick, Consumer<String> addWhiteOpening) {
        setSize(600, 600);
        setLayout(new GridLayout(3, 2));

        openingsList.add("Not an opening");
        openingsList.add("also not an opening");

        JLabel chooseOpeningLabel = new JLabel("Choose Opening: ");
        JComboBox<String> openingsDropdown = new JComboBox<>(openingsList.toArray(new String[0]));
        JButton addWhiteOpeningButton = new JButton("Add Opening");
        JTextField newOpeningTextField = new JTextField(10);
        JButton startButton = new JButton("Start");
        JButton freePlayButton = new JButton("Free Play");

        freePlayButton.addActionListener(e -> freePlayClick.run());
        addWhiteOpeningButton.addActionListener(e -> {
            addWhiteOpening.accept(newOpeningTextField.getText());
        });

//line 1
        add(chooseOpeningLabel);
        add(newOpeningTextField);

//line 2
        add(openingsDropdown);
        add(addWhiteOpeningButton);

//line 3
        add(startButton);
        add(freePlayButton);



    }
}
