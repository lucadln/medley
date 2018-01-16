package view;

import javax.swing.*;

public class MainWindow {

    public static void main(String[] args) {

        // Create the frame.
        JFrame frame = new JFrame("FrameDemo");

        // Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Size the frame.
        frame.pack();

        // Show frame.
        frame.setVisible(true);
    }
}