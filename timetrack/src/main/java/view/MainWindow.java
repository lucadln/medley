package view;

import model.RequestHandler;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.SwingConstants;
import java.util.*;
import java.text.*;

public class MainWindow {

    public static void main(String[] arguments) {

        String cookie = "";
        String response;

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

        ClockLabel timeLable = new ClockLabel();

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame f = new JFrame("Digital Clock");
        f.setSize(300,150);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(1, 1));

        f.add(timeLable);

        f.getContentPane().setBackground(Color.black);

        f.setVisible(true);
    }
}

class ClockLabel extends JLabel implements ActionListener {

    SimpleDateFormat sdf;

    public ClockLabel() {
        setForeground(Color.green);

        sdf = new SimpleDateFormat("HH:mm:ss");
        setFont(new Font("sans-serif", Font.PLAIN, 40));
        setHorizontalAlignment(SwingConstants.CENTER);

        Timer t = new Timer(1000, this);
        t.start();
    }

    public void actionPerformed(ActionEvent ae) {
        Date d = new Date();
        setText(sdf.format(d));
    }
}