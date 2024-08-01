import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

class framey extends JFrame implements ActionListener {
    int settime = 0;
    int sec = 0;
    int min = 0;
    int hor = 0;

    int totalSec = 0;
    int totalMin = 0;
    int totalHor = 0;

    boolean started = false;

    String sec_name = String.format("%02d", sec);
    String min_name = String.format("%02d", min);
    String hor_name = String.format("%02d", hor);

    Timer time = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            settime += 1000;
            hor = (settime / 3600000);
            min = (settime / 60000) % 60;
            sec = (settime / 1000) % 60;
            sec_name = String.format("%02d", sec);
            min_name = String.format("%02d", min);
            hor_name = String.format("%02d", hor);
            label.setText(hor_name + ": " + min_name + ": " + sec_name);
        }
    });

    JPanel panel1, panel2;
    JButton button1, button2, button3;
    JLabel label;

    framey() {
        panel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        button3 = new JButton("Total Time");
        button3.setFocusable(false);
        button3.setBackground(Color.decode("#ffa600"));
        button3.addActionListener(this);
        panel1.setBackground(Color.BLACK);
        panel1.add(button3);

        ImageIcon icon = new ImageIcon("butto.png");
        Image image = icon.getImage();
        this.setIconImage(image);

        panel2 = new JPanel(null);
        panel2.setBackground(Color.decode("#00244a"));
        label = new JLabel();
        label.setForeground(Color.WHITE);
        panel1.setBackground(Color.decode("#212121"));
        label.setFont(new Font("Arial", Font.BOLD, 50));
        label.setText(hor_name + ": " + min_name + ": " + sec_name);
        label.setBounds(135, 40, 300, 100);

        button1 = new JButton("START");
        button1.setFocusable(false);
        button1.setBackground(Color.decode("#ffa600"));
        button1.setFont(new Font("Franklin Gothic Demi Cond", Font.BOLD, 20));
        button1.setBounds(150, 170, 100, 50);
        button1.addActionListener(this);

        button2 = new JButton("STOP");
        button2.setFocusable(false);
        button2.setBackground(Color.decode("#ffa600"));
        button2.setFont(new Font("Franklin Gothic Demi Cond", Font.BOLD, 20));
        button2.setBounds(250, 170, 100, 50);
        button2.addActionListener(this);

        panel2.add(label);
        panel2.add(button1);
        panel2.add(button2);

        setLayout(new BorderLayout());
        this.add(panel1, BorderLayout.NORTH);
        this.add(panel2, BorderLayout.CENTER);
        setLocation(400, 200);

        // Load total time from file
        loadTotalTime();
        updateLabel();
    }

    private void loadTotalTime() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Timefile.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    totalHor = Integer.parseInt(parts[0]);
                    totalMin = Integer.parseInt(parts[1]);
                    totalSec = Integer.parseInt(parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLabel() {
        label.setText(String.format("%02d: %02d: %02d", totalHor, totalMin, totalSec));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            if (!started) {
                time.start();
                started = true;
            }
        }
        if (e.getSource() == button2) {
            if (started) {
                time.stop();
                started = false;

                // Calculate total time
                int elapsedSec = (sec + (min * 60) + (hor * 3600));
                totalSec += elapsedSec;
                totalMin += totalSec / 60;
                totalSec %= 60;
                totalHor += totalMin / 60;
                totalMin %= 60;

                updateLabel();

                // Save total time to file
                try (FileWriter writer = new FileWriter("Timefile.txt")) {
                    writer.write(String.format("%02d:%02d:%02d%n", totalHor, totalMin, totalSec));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // Reset the timer
                settime = 0;
                sec = 0;
                min = 0;
                hor = 0;
                sec_name = String.format("%02d", sec);
                min_name = String.format("%02d", min);
                hor_name = String.format("%02d", hor);
                label.setText(hor_name + ": " + min_name + ": " + sec_name);
            }
        }
        if (e.getSource() == button3) {
            launch mywin = new launch();
            dispose();
            mywin.setVisible(true);
        }
    }

    class launch extends JFrame implements ActionListener {
        JButton buttonnew, Zerobtn;
        JLabel totalTimeLabel;

        launch() {
            setSize(500, 400);
            JLabel text = new JLabel("Your Total Work Time");
            text.setBounds(90, 40, 400, 100);
            text.setForeground(Color.white);
            totalTimeLabel = new JLabel();
            totalTimeLabel.setForeground(Color.WHITE);
            text.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 45));

            ImageIcon icon = new ImageIcon("butto.png");
            Image image = icon.getImage();
            this.setIconImage(image);

            buttonnew = new JButton("Back");
            buttonnew.setFocusable(false);
            buttonnew.setBackground(Color.decode("#ffa600"));
            buttonnew.setFont(new Font("Franklin Gothic Demi Cond", Font.BOLD, 20));
            buttonnew.setBounds(110, 240, 100, 30);
            buttonnew.addActionListener(this);

            Zerobtn = new JButton("Reset");
            Zerobtn.setFocusable(false);
            Zerobtn.setBackground(Color.decode("#ffa600"));
            Zerobtn.setFont(new Font("Franklin Gothic Demi Cond", Font.BOLD, 20));
            Zerobtn.setBounds(290, 240, 100, 30);
            Zerobtn.addActionListener(this);

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            loadTotalTime();

            add(totalTimeLabel);
            totalTimeLabel.setBounds(130, 170, 500, 50);
            totalTimeLabel.setFont(new Font("Arial", Font.BOLD, 60));

            add(text);
            add(buttonnew);
            add(Zerobtn);
            getContentPane().setBackground(Color.decode("#002a57"));
            setLayout(null);
            setTitle("Worktime");
            setResizable(false);
            setLocation(400, 200);
        }

        private void loadTotalTime() {
            try (BufferedReader reader = new BufferedReader(new FileReader("Timefile.txt"))) {
                String line = reader.readLine();
                if (line != null) {
                    totalTimeLabel.setText(line.trim());
                } else {
                    totalTimeLabel.setText("00:00:00");
                }
            } catch (IOException e) {
                totalTimeLabel.setText("00:00:00");
                e.printStackTrace();
            }
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buttonnew) {
                framey window = new framey();
                window.setSize(500, 400);
                window.setTitle("Worktime");
                window.setResizable(false);
                window.setDefaultCloseOperation(framey.EXIT_ON_CLOSE);
                dispose();
                window.setVisible(true);
            }
            if (e.getSource() == Zerobtn) {
                totalTimeLabel.setText("00:00:00");

                try (FileWriter upwritter = new FileWriter("Timefile.txt")) {
                    upwritter.write("00:00:00");
                } catch (IOException j) {
                    j.printStackTrace();
                }
            }
        }
    }
}

public class timerapp {
    public static void main(String[] args) {
        framey screenw = new framey();
        screenw.setVisible(true);
        screenw.setDefaultCloseOperation(framey.EXIT_ON_CLOSE);
        screenw.setSize(500, 400);
        screenw.setResizable(false);
        screenw.setTitle("Worktime");
    }
}
