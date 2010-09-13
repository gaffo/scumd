package com.asolutions.scmsshd.ldap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asolutions.scmsshd.authenticators.LDAPUsernameResolver;
import com.asolutions.scmsshd.authorizors.AuthorizationLevel;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;
import com.asolutions.scmsshd.sshd.UnparsableProjectException;

public class LDAPProjectAuthorizer implements IProjectAuthorizer {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	private String groupBaseDN;
	private String groupSuffix;
	private AuthorizationLevel authorizationLevel;
	private LDAPBindingProvider binding;
	private LDAPUsernameResolver resolver;

	public LDAPProjectAuthorizer(String groupBaseDN, 
								 String groupSuffix,
								 AuthorizationLevel authorizationLevel,
								 LDAPBindingProvider binding,
								 LDAPUsernameResolver resolver)
			throws NamingException {
		this.groupBaseDN = groupBaseDN;
		this.groupSuffix = groupSuffix;
		this.authorizationLevel = authorizationLevel;
		this.binding = binding;
		this.resolver = resolver;
	}

	public AuthorizationLevel userIsAuthorizedForProject(String username, String group)
			throws UnparsableProjectException {
		try {
			username = resolver.resolveUserName(username);
			group = getGroupDN(group);
			Attributes attrs = binding.getBinding().getAttributes(group);
			NamingEnumeration<?> e = attrs.get("member").getAll();
			while (e.hasMoreElements())
			{
				String value = e.nextElement().toString().toLowerCase();
				if (username.toLowerCase().equals(value)){
					return authorizationLevel;
				}
			}
			return null;
		} catch (NamingException e) {
			log.error("Error running impl" , e);
			return null;
		}
	}

	private String getGroupDN(String group) {
		if (groupSuffix == null)
		{
			return "cn=" + group + "," + groupBaseDN;
		}
		else{
			return "cn=" + group + "-" + groupSuffix + "," + groupBaseDN;
		}
	}
}
