package org.acme.rest.json;

import javax.inject.Inject;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import org.apache.camel.builder.RouteBuilder;

public class DBRoutes extends RouteBuilder {
    @Inject
    @DataSource("camel-ds")
    AgroalDataSource dataSource;

    @Override
    public void configure() throws Exception {

        from("direct:getAllUsers")
                .id("getAllUsers")
                .to("sql:SELECT * FROM users");

        from("direct:addUser")
                .id("addUser")
                .process(exchange -> {
                    User user = (User) exchange.getIn().getBody();
                    exchange.getIn().setHeader("name", user.getName());
                    exchange.getIn().setHeader("email", user.getEmail());
                })
                .to("sql:INSERT INTO users (id,name, email) VALUES (NEXTVAL('user_id_seq'),:#name, :#email)");

    }
}
