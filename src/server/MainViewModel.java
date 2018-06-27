package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainViewModel {

    ChatServer server;
    ArrayList<String> users;
    UserListObservable userList;
    UserListObserver userListObserver;
    private int port;
    BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

    public MainViewModel() {
        initalizeData();
        menu1();
    }

    private void initalizeData() {
        users = new ArrayList<>();
        userList = new UserListObservable(users);
        userListObserver = new UserListObserver(this);
        userList.addObserver(userListObserver);
    }

    public void closeApplication() {
        System.exit(0);
    }

    public void stopServer() {
        server.shutdown();
    }

    public void startServer(int port) {
        if (server == null) {
            initalizeData();
            server = new ChatServer(port, userList);
            server.start();
            System.out.println("INFO: Server started");
        }
    }


    public synchronized void refreshUsers(ArrayList<String> updated) {
        users = updated;
        users.stream().forEach(i -> System.out.println(i));
        printUsers(updated);
    }


    private String getHostname() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostName();
        } catch (IOException e) {

        }
        return "";
    }

    private void capturePort() {
        try {


            while (port == 0) {
                System.out.println("DATA: Please enter a port between 50 000 and 51 000");
                String input = buffer.readLine();
                if (input.matches("^\\d{5}$")) {
                    int temp = Integer.parseInt(input);
                    if (temp >= 50000 && temp <= 51000) {
                        port = temp;
                        startServer(port);
                    } else {
                        System.out.println("DATA: Incorrect. Please try again");
                    }
                }
            }
        } catch (IOException e) {

        }
    }

    private void captureAnswer() {
        System.out.println("DATA: Press key to start the server");
        System.console().readLine();
        startServer(port);
        menu2();
    }

    private void menu1() {
        boolean runAgain = true;
        try {
            while (runAgain) {
                System.out.println("INFO: Main menu");
                System.out.println("1: Start server");
                System.out.println("2: Close application");
                String command = buffer.readLine();

                if (command.equals("1")) {
                    capturePort();
                    runAgain = false;
                    menu2();
                } else if (command.equals("2")) {
                    System.exit(0);
                }
            }
        } catch (IOException e) {

        }
    }

    private void menu2() {
        boolean runAgain = true;
        try {
                while (runAgain) {
                    System.out.println("INFO: Server menu");
                    System.out.println("1: Stopserver");
                    String command = buffer.readLine();
                    if (command.equals("1")) {
                        stopServer();
                        System.exit(0);
                        runAgain = false;
                        menu1();
                    }
                }
            }catch(IOException e){

            }
        }

    public void printUsers(List<String> users) {
        System.out.println("DATA: Refreshed user list");
        users.forEach(i -> System.out.println(i));
    }

    public void serverStopped() {
        System.out.println("INFO: Server stopped");
        port = 0;
        System.exit(0);
    }
}
