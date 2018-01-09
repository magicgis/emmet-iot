package com.emmet.iot.account.manager;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountUserDetailsService implements UserDetailsService {
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if ("admin".equals(username)) {
			return new UserDetails() {
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("serial")
				@Override
				public Collection<? extends GrantedAuthority> getAuthorities() {
					ArrayList<GrantedAuthority> authorities = new ArrayList<>();
					authorities.add(new GrantedAuthority() {
						@Override
						public String getAuthority() {
							return ROLE_ADMIN;
						}
					});
					return authorities;
				}

				@Override
				public String getPassword() {
					return "admin";
				}

				@Override
				public String getUsername() {
					return "admin";
				}

				@Override
				public boolean isAccountNonExpired() {
					return true;
				}

				@Override
				public boolean isAccountNonLocked() {
					return true;
				}

				@Override
				public boolean isCredentialsNonExpired() {
					return true;
				}

				@Override
				public boolean isEnabled() {
					return true;
				}
			};
		}

		throw new UsernameNotFoundException("User not found.");
	}

}
