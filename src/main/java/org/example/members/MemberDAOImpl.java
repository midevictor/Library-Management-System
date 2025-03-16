package org.example.members;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MemberDAOImpl implements  MemberDAO {
    private final Connection connection;

    public MemberDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addMember(Member member) {
        String SQL = "INSERT INTO member (member_id, name, email, phone)  VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, member.getMemberId());
            ps.setString(2, member.getName());
            ps.setString(3, member.getEmail());
            ps.setString(4, member.getPhone());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override

    public void updateMember(Member member) {
        String SQL = "UPDATE member SET name = ?, email = ?, phone = ? WHERE member_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setInt(4, member.getMemberId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMember(int member_id) {
        String SQL = "DELETE FROM member WHERE member_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, member_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Member> getAllMembers() {
        String sql = "SELECT * FROM member";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Member ID: " + rs.getInt("member_id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Phone: " + rs.getString("phone"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
//    public Member getMemberById(int member_id) {
//        String sql = "SELECT * FROM member WHERE member_id = ?";
//        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setInt(1, member_id);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    System.out.println("Member ID: " + rs.getInt("member_id"));
//                    System.out.println("Name: " + rs.getString("name"));
//                    System.out.println("Email: " + rs.getString("email"));
//                    System.out.println("Phone: " + rs.getString("phone"));
//                } else {
//                    System.out.println("No member record found ith id: " + member_id);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    public Member getMemberById(int member_id) {
        String SQL = "SELECT * FROM member WHERE member_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, member_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Member member = new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                System.out.println("Member fetched: " + member);
                return member;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
