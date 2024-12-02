package com.amrit.jwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_user_role_map")
public class AppUserRoleMap extends AbstractEntity{
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AppUser appUser;

    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Roles roles;
}
