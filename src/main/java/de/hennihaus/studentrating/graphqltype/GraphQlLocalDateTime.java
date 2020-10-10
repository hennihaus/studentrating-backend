package de.hennihaus.studentrating.graphqltype;

import graphql.language.StringValue;
import graphql.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class GraphQlLocalDateTime extends GraphQLScalarType {

    public GraphQlLocalDateTime() {
        super("LocalDateTime", "LocalDateTime Type", new Coercing<LocalDateTime, String>() {
            @Override
            public String serialize(Object o) throws CoercingSerializeException {
                if (o instanceof LocalDateTime) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    return ((LocalDateTime) o).format(formatter);
                } else {
                    log.error("LocalDateTime parsing to String was not sucessfull");
                    return null;
                }
            }

            @Override
            public LocalDateTime parseValue(Object o) throws CoercingParseValueException {
                if (o instanceof String) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    return LocalDateTime.parse((String) o, formatter);
                } else {
                    log.error("String parsing to LocalDateTime was not successfull");
                    return null;
                }
            }

            @Override
            public LocalDateTime parseLiteral(Object o) throws CoercingParseLiteralException {
                if (o instanceof StringValue) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    return LocalDateTime.parse(((StringValue) o).getValue(), formatter);
                } else {
                    log.error("String parsing to LocalDateTime was not successfull");
                    return null;
                }
            }
        });
    }
}
