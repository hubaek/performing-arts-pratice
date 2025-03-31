package com.culture.perfomingarts.domain.user.entity;

import com.culture.perfomingarts.common.entity.Timestamped;
import com.culture.perfomingarts.domain.user.enums.MemberStatus;
import com.culture.perfomingarts.domain.user.enums.Position;
import com.culture.perfomingarts.domain.user.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate birthDate;

    private String gender;

    private String phone;

    private String uniqueCode;

    private String email;

    private String password;

    private String department;

    private String region;

    private String cell;

    private Integer joinYear;

    private String major;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberstatus;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String comment;

    private Long teamId;

}
