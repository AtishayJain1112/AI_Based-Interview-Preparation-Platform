package com.springSecurity.basicOth.Service;

import com.springSecurity.basicOth.Entity.Role;
import com.springSecurity.basicOth.Repository.RoleRepo;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo){
        this.roleRepo = roleRepo;
    }

    public boolean addRole(Role role){
        if(roleRepo.findByRoleName(role.getRoleName()).isPresent()){
            return false;
        }
        roleRepo.save(role);
        return true;
    }
}
