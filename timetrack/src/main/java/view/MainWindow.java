package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.SwingConstants;
import java.util.*;
import java.text.*;
import java.util.List;

public class MainWindow {

    public static void main(String[] arguments) {

        String cookie = "";
        String response;
        long entranceTimestamp = 0;

        // Login and open the membership page
        RequestHandler requestHandler = new RequestHandler();
        Map<String, List<String>> map = requestHandler.executePost(
                "http://nra2008:80/nroffice/Pages/login.aspx",
                "__VIEWSTATE=%2FwEPDwUJOTM4MDI3NzQ1D2QWAmYPZBYCAgMPZBYCAgMPZBYCZg9kFgICAQ88KwAJAgAPFgYeDU5ldmVyRXhwYW5kZWRkHgxTZWxlY3RlZE5vZGVkHglMYXN0SW5kZXgCBGQIFCsABWQUKwACFgIeCEV4cGFuZGVkZ2QUKwACFgIfA2dkFCsAAhYCHwNnZBQrAAIWAh8DZ2RkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYCBRljdGwwMCRMb2dpblZpZXckVHJlZVZpZXcxBRxjdGwwMCRDb250ZW50Qm9keSRjaGtSZW1lYmVyDUma3ZqKUmv9AtlbdTeEKbbr65E%3D&__VIEWSTATEGENERATOR=7EEC1B53&__EVENTVALIDATION=%2FwEWBgLKmJi3DgLl48WmDQL37YefDwKXr7zZBgKWo9AlAtjm5rIJ33lWd1AnvfwEVb%2FUaqVsk2Dn6lQ%3D&ctl00%24ContentBody%24LoginUserBox=LUA&ctl00%24ContentBody%24LoginPasswordBox=&ctl00%24ContentBody%24LoginButton=Autentifica-ma");

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry.getKey() != null) {
                if (entry.getKey().toString().equals("Set-Cookie")) {
                    cookie = entry.getValue().toString().replace("path=/; HttpOnly, ", "").replace("; path=/; HttpOnly", "")
                                .replace("[", "").replace("]", "");
                    System.out.println(cookie);
                }
            }
        }
        response = requestHandler.executeGet("http://nra2008:80/nroffice/Pages/Membership/query.aspx", cookie);

        // Extract the entrance time
        int timeIndex = response.lastIndexOf("Intrare Receptie Cluj-Napoca") + 81;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String entranceTime = sdf.format(new Date()) + " " + response.substring(timeIndex, timeIndex + 5) + ":00";

        // Convert entranceTime to timestamp
        try {
            entranceTimestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entranceTime).getTime();
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }

        ClockLabel timeLable = new ClockLabel(entranceTimestamp);

        JFrame f = new JFrame("Digital Clock");
        f.setUndecorated(true);
        f.setAlwaysOnTop(true);
        f.setSize(300,150);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(1, 1));
        f.getContentPane().setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
        f.setBackground(new Color(1.0f,1.0f,1.0f,0.0f));

        f.add(timeLable);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) ((int) 2 * rect.getMaxX() - f.getWidth());
        int y = (int) rect.getMaxY() - f.getHeight();
        f.setLocation(x, y);
        f.setVisible(true);
        f.setVisible(true);
    }
}

class ClockLabel extends JLabel implements ActionListener {

    SimpleDateFormat sdf;
    String result;
    long entranceTimestamp;

    public ClockLabel(long entranceTimestamp) {
        setForeground(new Color(198, 198, 198));
        this.entranceTimestamp = entranceTimestamp;

        sdf = new SimpleDateFormat("HH:mm:ss");

        setFont(new Font("monospaced", Font.PLAIN, 50));
        setHorizontalAlignment(SwingConstants.CENTER);

        Timer t = new Timer(1000, this);
        t.start();
    }

    public void actionPerformed(ActionEvent ae) {
        long currentTimestamp = System.currentTimeMillis();
        long timeSpentMillis = currentTimestamp - entranceTimestamp - 7200000;

        Date d = new Date(timeSpentMillis);
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String timeSpentString = df.format(d);
        result = sdf.format(d);

        try {
            d = new SimpleDateFormat("HH:mm:ss").parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setText(sdf.format(d));
    }
}