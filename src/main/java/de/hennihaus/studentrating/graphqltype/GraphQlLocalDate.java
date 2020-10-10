package de.hennihaus.studentrating.graphqltype;

import graphql.language.StringValue;
import graphql.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class GraphQlLocalDate extends GraphQLScalarType {

    public GraphQlLocalDate() {
        super("LocalDate", "Local Date Type", new Coercing<LocalDate, String>() {
            @Override
            public String serialize(Object o) throws CoercingSerializeException {
                if (o instanceof LocalDate) {
                    return ((LocalDate) o).toString();
                } else {
                    log.error("LocalDate parsing to String was not sucessfull");
                    return null;
                }
            }

            @Override
            public LocalDate parseValue(Object o) throws CoercingParseValueException {
                if (o instanceof String) {
                    return LocalDate.parse((String) o);
                } else {
                    log.error("String parsing to LocalDate was not successfull");
                    return null;
                }
            }

            @Override
            public LocalDate parseLiteral(Object o) throws CoercingParseLiteralException {
                if (o instanceof StringValue) {
                    return LocalDate.parse(((StringValue) o).getValue());
                } else {
                    log.error("String parsing to LocalDate was not successfull");
                    return null;
                }
            }
        });
    }
}
