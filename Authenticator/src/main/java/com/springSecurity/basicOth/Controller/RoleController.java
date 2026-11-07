package com.springSecurity.basicOth.Controller;

import com.springSecurity.basicOth.Entity.Role;
import com.springSecurity.basicOth.Service.RoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class RoleController {
    public RoleService roleService;
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping("/role")
    public boolean addRole(@RequestBody Role role){
        return roleService.addRole(role);
    }

}
