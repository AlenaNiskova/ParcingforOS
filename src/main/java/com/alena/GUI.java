package com.alena;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.Map;

public class GUI extends JFrame {

    //Don't wanna say anything, 'cause it was really hard to create this GUI.
    //But! It's beautiful and understandable interface.
    //Elements build in one column and has different sizes, which is works
    //because of GridBagLayout.
    //Front-end development is harder than I think (._.).
    //P.S. Links in TextArea, 'cause I need their background to be transparent.
    //TextArea.setOpaque(false) is really helpful with it.
    //Borders are for beauty and distinction.
    public GUI(Map<String, Record> JSONMap) {
        JFrame frame = new JFrame("ВК Новости");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int count = 0;

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints allbag = new GridBagConstraints();
        allbag.gridx = 0;

        for (Record rec: JSONMap.values()) {
            JPanel Post = new JPanel();
            Post.setLayout(new GridBagLayout());
            GridBagConstraints bag = new GridBagConstraints();
            int num = 0;
            bag.gridx = 0;

            String Text = rec.getText();
            if ((Text!="")||(Text!="\n")) {
                JTextArea Area = new JTextArea();
                Area.setText(Text);
                Area.setWrapStyleWord(true);
                Area.setLineWrap(true);
                Area.setRows(Area.getLineCount());
                Area.setColumns(55);
                Area.setEditable(false);
                Area.setOpaque(false);
                bag.gridy = num;
                bag.anchor = GridBagConstraints.WEST;
                bag.insets.bottom = 5;
                Post.add(Area, bag);
                num++;
            }

            String[] links = rec.getLinks().toArray(new String[rec.getLinks().size()]);
            String Links = "";
            for (String link: links) {
                Links = Links + link + "\n";
            }
            if (Links!="") {
                JTextArea Link = new JTextArea();
                Link.setText(Links);
                Link.setWrapStyleWord(false);
                Link.setLineWrap(true);
                Link.setRows(Link.getLineCount());
                Link.setColumns(55);
                Link.setEditable(false);
                Link.setOpaque(false);
                bag.gridy = num;
                bag.anchor = GridBagConstraints.WEST;
                bag.insets.bottom = 5;
                Post.add(Link, bag);
                num++;
            }

            String[] pics = rec.getPics().toArray(new String[rec.getPics().size()]);
            for (String pic: pics) {
                JLabel label = new JLabel();
                ImageIcon icon = new ImageIcon(pic);
                label.setIcon(icon);
                bag.gridy = num;
                bag.anchor = GridBagConstraints.WEST;
                bag.insets.bottom = 5;
                Post.add(label, bag);
                num++;
            }

            Post.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                    BorderFactory.createEmptyBorder(25,25,25,25)));
            Post.revalidate();
            allbag.gridy = count;
            allbag.anchor = GridBagConstraints.WEST;
            allbag.insets.left = 10;
            allbag.insets.bottom = 50;
            panel.add(Post, allbag);
            count++;
        }
        panel.revalidate();

        JScrollPane Scroll = new JScrollPane(panel);
        Scroll.getVerticalScrollBar().setUnitIncrement(20);
        frame.getContentPane().add(Scroll);

        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
