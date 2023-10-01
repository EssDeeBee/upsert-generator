package hckrn.upsertgenerator.service;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;


@Service
@RequiredArgsConstructor
public class CustomRepository {

    private final Pattern camelCasePattern = Pattern.compile("(?<=[a-z])[A-Z]");

    private final NamedParameterJdbcTemplate template;

    public <T> void upsertAll(List<T> values) {
        if (!CollectionUtils.isEmpty(values)) {
            Class<?> aClass = values.get(0).getClass();
            template.batchUpdate(generateUpsertQuery(aClass), values.stream().map(BeanPropertySqlParameterSource::new)
                    .toArray(BeanPropertySqlParameterSource[]::new));
        }
    }

    public String generateUpsertQuery(Class<?> aClass) {
        Table tableAnnotation = extractTableAnnotation(aClass);
        EntityFieldsDescription fieldsDescription = extractEntityInfo(aClass);
        String tablePath = extractTablePath(tableAnnotation);

        return generateUpsertQuery(fieldsDescription, tablePath);
    }

    private String generateUpsertQuery(EntityFieldsDescription fieldsDescription, String tableSchema) {
        return "INSERT INTO " +
                tableSchema +
                " (" +
                String.join(",", fieldsDescription.columns()) +
                ") VALUES (" +
                String.join(",", fieldsDescription.fields().stream().map(s -> ":" + s).toList()) +
                ") ON CONFLICT (" +
                fieldsDescription.id() +
                ") DO UPDATE SET " +
                String.join(",", fieldsDescription.columns().stream().map(s -> s + " = EXCLUDED." + s).toList()) +
                ";";
    }

    private Table extractTableAnnotation(Class<?> aClass) {
        Table tableAnnotation = aClass.getAnnotation(Table.class);
        Entity entityAnnotation = aClass.getAnnotation(Entity.class);
        if (isNull(entityAnnotation)) {
            throw new UnsupportedOperationException("Query can be generated for not Entities");
        }

        if (isNull(tableAnnotation)) {
            throw new UnsupportedOperationException("Query can be generated for Entities that are not annotated with @Table");
        }
        return tableAnnotation;
    }

    private String extractTablePath(Table tableAnnotation) {
        var table = tableAnnotation.name();
        if (StringUtils.hasText(tableAnnotation.schema())) {
            return tableAnnotation.schema() + "." + table;
        }
        return table;
    }

    private EntityFieldsDescription extractEntityInfo(Class<?> aClass) {
        Field[] declaredFields = aClass.getDeclaredFields();
        List<String> columns = new LinkedList<>();
        List<String> fields = new LinkedList<>();
        var id = "id";
        for (Field field : declaredFields) {
            String name = field.getName();
            String column = Optional.ofNullable(field.getAnnotation(Column.class))
                    .map(Column::name)
                    .orElseGet(() -> toLowerUnderscoreCase(name));

            columns.add(column);
            fields.add(name);

            if (Objects.nonNull(field.getAnnotation(Id.class))) {
                id = column;
            }
        }

        return new EntityFieldsDescription(columns, fields, id);
    }

    private String toLowerUnderscoreCase(String camelCaseText) {
        return camelCasePattern.matcher(camelCaseText).replaceAll(match -> "_" + match.group())
                .toLowerCase();
    }

    private record EntityFieldsDescription(List<String> columns, List<String> fields, String id) {
    }
}
