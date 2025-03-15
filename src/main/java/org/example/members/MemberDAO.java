package org.example.members;

import java.util.List;

public interface MemberDAO {
    void addMember(Member member);
    void updateMember(Member member);
    void deleteMember(int member_id);
    List<Member> getAllMembers();
    Member getMemberById(int member_id);
}