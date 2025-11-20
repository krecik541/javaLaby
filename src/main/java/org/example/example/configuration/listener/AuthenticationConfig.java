package org.example.example.configuration.listener;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@ApplicationScoped
@BasicAuthenticationMechanismDefinition(realmName = "FoodBlog")
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/food-blog",
        callerQuery = "select password from users where login = ?",
        groupsQuery = "select role from user_roles where user_id = (select id from users where login = ?)",
        hashAlgorithm = Pbkdf2PasswordHash.class
)
public class AuthenticationConfig {
}
