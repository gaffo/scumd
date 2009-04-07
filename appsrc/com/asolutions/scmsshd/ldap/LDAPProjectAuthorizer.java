package com.asolutions.scmsshd.ldap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asolutions.scmsshd.authorizors.AuthorizationLevel;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;
import com.asolutions.scmsshd.sshd.UnparsableProjectException;

public class LDAPProjectAuthorizer implements IProjectAuthorizer {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	private String lookupUserDN;
	private String lookupUserPassword;
	private InitialDirContext binding;
	private String groupBaseDN;
	private String userBaseDN;
	private String groupSuffix;
	private AuthorizationLevel authorizationLevel;

	public LDAPProjectAuthorizer(String lookupUserDN,
								 String lookupUserPassword, 
								 String groupBaseDN, 
								 String userBaseDN,
								 String groupSuffix,
								 String url, 
								 boolean promiscuous, 
								 AuthorizationLevel authorizationLevel)
			throws NamingException {
		this(lookupUserDN, lookupUserPassword, groupBaseDN,userBaseDN, groupSuffix,
				new JavaxNamingProvider(url, promiscuous), authorizationLevel);
	}

	public LDAPProjectAuthorizer(String lookupUserDN,
								 String lookupUserPassword, 
								 String groupBaseDN,
								 String userBaseDN,
								 String groupSuffix,
								 IJavaxNamingProvider provider, 
								 AuthorizationLevel authorizationLevel) throws NamingException {
		this.lookupUserDN = lookupUserDN;
		this.lookupUserPassword = lookupUserPassword;
		this.groupBaseDN = groupBaseDN;
		this.userBaseDN = userBaseDN;
		this.groupSuffix = groupSuffix;
		this.binding = provider.getBinding(this.lookupUserDN,
										   this.lookupUserPassword);
		this.authorizationLevel = authorizationLevel;
	}

	public AuthorizationLevel userIsAuthorizedForProject(String username, String group)
			throws UnparsableProjectException {
		username = getUserDN(username);
		group = getGroupDN(group);
		try {
			Attributes attrs = binding.getAttributes(group);
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

	private String getUserDN(String username) {
		return "cn=" + username + "," + userBaseDN;
	}

}
