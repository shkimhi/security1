package io.security.basicsecurity.Service;

import io.security.basicsecurity.Model.Account;
import io.security.basicsecurity.Model.ERole;
import io.security.basicsecurity.Model.Role;
import io.security.basicsecurity.Repository.AccountRepository;
import io.security.basicsecurity.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

@Service(value="userServiceImpl")
public class UserServiceImpl implements UserService{

    private AccountRepository accountRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Account getUserByEmail(String email) throws Exception {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Account getUserByUsername(String username) throws Exception {
        return accountRepository.findByUsername(username);
    }

    @Override
    public Account setUser(Account user) throws Exception {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);

        Role userRole = null;

        if(user.getUsername().equals("admin")){
            userRole = roleRepository.findByRole(ERole.ADMIN.getValue());
        }else if(user.getUsername().equals("user")){
            userRole = roleRepository.findByRole(ERole.MANAGER.getValue());
        }else{
            userRole = roleRepository.findByRole(ERole.GUEST.getValue());
        }

        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        return accountRepository.save(user);

    }
}
