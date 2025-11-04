package chess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IntroGUI extends JPanel {
    private List<String> openingsList = new ArrayList<>();

    public IntroGUI(Runnable freePlayClick) {
        setSize(600, 600);
        setLayout(new GridLayout(3, 2));

        openingsList.add("Not an opening");
        openingsList.add("also not an opening");

        JLabel chooseOpeningLabel = new JLabel("Choose Opening: ");
        JComboBox<String> openingsDropdown = new JComboBox<>(openingsList.toArray(new String[0]));
        JButton newOpeningButton = new JButton("Add Opening");
        JButton startButton = new JButton("Start");
        JButton freePlayButton = new JButton("Free Play");

        freePlayButton.addActionListener(e -> freePlayClick.run());


        add(chooseOpeningLabel);
        add(newOpeningButton);

        add(openingsDropdown);
        add(new JLabel(""));

        add(startButton);
        add(freePlayButton);



    }
}
