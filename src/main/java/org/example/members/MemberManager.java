package org.example.members;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class MemberManager {
    private BufferedWriter output;
    private final List<Member> memberRecords = new ArrayList<>();
    private final Map<Integer, Member> memberMap = new HashMap<>();

    public void openFile() {
        try {
            output = new BufferedWriter(new FileWriter("members.txt", true));
        } catch (SecurityException securityException) {
            System.err.println("You do not have write access to this file");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error creating file");
            System.exit(1);
        }
    }

    public void addMember() {
        Scanner input = new Scanner(System.in);
        boolean continueInput = true;

        while (continueInput) {
            try {
                Member member = new Member();
                System.out.printf("%s\n%s", "Enter member id, name, email and phone", "?");
                member.setMemberId(input.nextInt());
//                input.nextLine();
                member.setName(input.next());
                member.setEmail(input.next());
                member.setPhone(input.next());

                String recordString = String.format("%d %s %s %s",
                        member.getMemberId(), member.getName(), member.getEmail(), member.getPhone());
                output.write(recordString);
                output.newLine();
                memberRecords.add(member);
                memberMap.put(member.getMemberId(), member);
                System.out.println("Record added successfully");

                System.out.print("Do you want to continue? (yes/no): ");
                String response = input.next();
                if (response.equalsIgnoreCase("no")) {
                    continueInput = false;
                }

            } catch (FormatterClosedException formatterClosedException) {
                System.err.println("Error writing to file.");
                return;
            } catch (NoSuchElementException elementException) {
                System.err.println("Invalid input. Please try again");
                input.nextLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeFile() {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the file path to read: ");
        String filePath = input.nextLine();

        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
            String user = "neondb_owner";
            String password = "npg_icua3pobEN6q";

            try {
                Class.forName("org.postgresql.Driver");
                try (Connection connection = DriverManager.getConnection(url, user, password);
                     BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    memberRecords.clear();
                    memberMap.clear();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        String[] data = line.split(" ");
                        if (data.length == 4) {
                            int member_id = Integer.parseInt(data[0]);
                            String name = data[1];
                            String email = data[2];
                            String phone = data[3];

                            Member member = new Member(member_id, name, email, phone);
                            memberRecords.add(member);
                            memberMap.put(member_id, member);

                            MemberDAOImpl memberDAO = new MemberDAOImpl(connection);
                            memberDAO.addMember(member);
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Error connecting to database: " + e.getMessage());
                } catch (IOException e) {
                    System.err.println("Error reading file");
                }
            } catch (ClassNotFoundException e) {
                System.err.println("Error loading PostgreSQL JDBC driver: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:postgresql://ep-ancient-pond-abk7yv84-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require";
        String user = "neondb_owner";
        String password = "npg_icua3pobEN6q";
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        MemberManager app = new MemberManager();

        while (isRunning) {
            System.out.println("Menu");
            System.out.println("1. Add member from the member.txt file");
            System.out.println("2. Get Member by ID");
            System.out.println("3. Update Member");
            System.out.println("4. Delete Member");
            System.out.println("5. Get all Members");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    app.openFile();
                    app.addMember();
                    app.closeFile();
                    app.readFile();
                    break;
                case 2:
                    System.out.print("Enter the ID of the member to get: ");
                    int id = scanner.nextInt();
                    try (Connection connection = DriverManager.getConnection(url, user, password)) {
                        MemberDAOImpl memberDAO = new MemberDAOImpl(connection);
                        Member member = memberDAO.getMemberById(id);
                        if (member != null) {
                            System.out.println("Member ID: " + member.getMemberId());
                            System.out.println("Name: " + member.getName());
                            System.out.println("Email: " + member.getEmail());
                            System.out.println("Phone: " + member.getPhone());
                        } else {
                            System.out.println("No more member found record f with id: " + id);
                        }
                    } catch (SQLException e) {
                        System.err.println("Error connecting to database: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Enter the ID of the member to update: ");
                    int memberId = scanner.nextInt();
                    scanner.nextLine();
                    try (Connection connection = DriverManager.getConnection(url, user, password)) {
                        MemberDAOImpl memberDAO = new MemberDAOImpl(connection);
                        Member member = memberDAO.getMemberById(memberId);
                        if (member != null) {
                            System.out.print("Enter new name: ");
                            String name = scanner.nextLine();
                            System.out.print("Enter new email: ");
                            String email = scanner.nextLine();
                            System.out.print("Enter new phone: ");
                            String phone = scanner.nextLine();

                            member.setName(name);
                            member.setEmail(email);
                            member.setPhone(phone);

                            memberDAO.updateMember(member);
                            System.out.println("Member updated successfully");
                        } else {
                            System.out.println("No member record found with id: " + memberId);
                        }
                    } catch (SQLException e) {
                        System.err.println("Error connecting to database: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.print("Enter the ID of the member to delete: ");
                    int member_id = scanner.nextInt();
                    try (Connection connection = DriverManager.getConnection(url, user, password)) {
                        MemberDAOImpl memberDAO = new MemberDAOImpl(connection);
                        memberDAO.deleteMember(member_id);
                        System.out.println("Member deleted successfully");
                    } catch (SQLException e) {
                        System.err.println("Error connecting to database: " + e.getMessage());
                    }
                    break;
                case 5:
                    try (Connection connection = DriverManager.getConnection(url, user, password)) {
                        MemberDAOImpl memberDAO = new MemberDAOImpl(connection);
                        memberDAO.getAllMembers();
                    } catch (SQLException e) {
                        System.err.println("Error connecting to database: " + e.getMessage());
                    }
                    break;
                case 6:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again");
            }
        }
        scanner.close();
    }
}