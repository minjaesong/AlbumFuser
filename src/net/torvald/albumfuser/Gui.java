package net.torvald.albumfuser;

import kotlin.text.Charsets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by minjaesong on 2019-03-19.
 */
public class Gui extends JFrame {

    private JPanel chooser = new JPanel();
    private JPanel threadCounter = new JPanel();
    private JPanel goer = new JPanel();
    private JPanel readme = new JPanel();

    private JTextField threadCount = new JTextField(String.valueOf(Runtime.getRuntime().availableProcessors()));

    private File workPath = null;

    private String outFormat = "wav";

    public Gui() {
        try {
            Properties p = new Properties();
            p.load(new FileInputStream("./CONFIG.properties"));
            outFormat = p.getProperty("outformat");
        }
        catch (IOException e) {  }


        chooser.setLayout(new FlowLayout());
        threadCounter.setLayout(new FlowLayout());
        goer.setLayout(new FlowLayout());

        chooser.add(new JLabel("Directory"));
        JTextField path = new JTextField();
        path.setPreferredSize(new Dimension(166, 24));
        chooser.add(path);
        JButton chooseFileButton = new JButton("Select...");
        chooseFileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showOpenDialog(null);

                if (fileChooser.getSelectedFile() != null) {
                    workPath = fileChooser.getSelectedFile();
                    try {
                        path.setText(workPath.getCanonicalPath());
                    }
                    catch (IOException iofucc) {
                        iofucc.printStackTrace();
                    }
                }
            }
        });
        chooser.add(chooseFileButton);
        JButton goButton = new JButton("GO!");
        goButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("GO!");
                new Fuser(outFormat, Charsets.UTF_8).runAll(workPath);
            }
        });
        threadCount.setPreferredSize(new Dimension(48, 24));
        threadCount.setEditable(false);
        threadCounter.add(new JLabel("Thread count"));
        threadCounter.add(threadCount);
        goer.add(goButton);
        readme.add(new JTextArea(readmeText));


        JPanel b1 = new JPanel();
        b1.setLayout(new BorderLayout());
        b1.add(chooser, BorderLayout.NORTH);
        b1.add(threadCounter, BorderLayout.CENTER);
        b1.add(goer, BorderLayout.SOUTH);
        b1.setVisible(true);

        this.setLayout(new BorderLayout());
        this.add(b1, BorderLayout.NORTH);
        this.add(readme, BorderLayout.CENTER);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(360, 450));
    }

    private String readmeText = "Select a folder that contains all the album folders.\n" +
            "Allowed directory structure:\n\n" +
            "    (root)/ArtistName/AlbumName/music.*\n" +
            "    (root)/AlbumName/music.*\n\n" +
            "Then select the thread count, and push Go.\n" +
            "This program will NOT show the progress;\n" +
            "Open up the Explorer/Finder on the destination\n" +
            "directory and see for yourself for file being written,\n" +
            "Task Manager for CPU Usage; if the usage drops to\n" +
            "near zero, you know the job is done.";

    public static void main(String[] args) {
        new Gui();
    }
}
