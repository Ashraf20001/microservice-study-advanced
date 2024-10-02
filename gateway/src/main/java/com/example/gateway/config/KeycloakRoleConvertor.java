package com.example.gateway.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRoleConvertor implements Converter<Jwt, Collection<GrantedAuthority>> {

    /**
     * @param source
     * @return
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String,Object> realmAccess = (Map<String,Object>) source.getClaims().get("realm_access");
         List<String> realmRoles = ( List<String> ) realmAccess.get("roles");
         return realmRoles.stream().map(el-> "ROLE_"+el)
                 .map(SimpleGrantedAuthority::new)
                 .collect(Collectors.toList());

    }
}
