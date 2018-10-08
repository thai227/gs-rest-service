package com.thai.infra.jpa.customConfig;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.*;

public class JsonSerializer {
    private static final List<JsonNodeType> leafNodes =
            Arrays.asList(JsonNodeType.BOOLEAN, JsonNodeType.NULL, JsonNodeType.NUMBER);

    private static final ObjectMapper mapper;
    private static final ObjectMapper prettyPrintMapper;

    // Set the default view so that all properties annotated with @JsonView are explictly ignored
    static {
        mapper = initMapper();

        prettyPrintMapper = initMapper();
        prettyPrintMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private static ObjectMapper initMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.setConfig(mapper.getSerializationConfig().withView(Object.class));

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

    public static <T> T json2Object(String json, Class<T> tClass) {
        try {
            T obj = null;
            if (!Strings.isNullOrEmpty(json)) {
                obj = mapper.readValue(json, tClass);
            }

            return obj;
        } catch (IOException ex) {
            throw new RuntimeException("Unable to parse: " + json, ex);
        }
    }

    public static <T> List<T> json2List(String json, Class<T> classType) {
        try {
            List<T> listObj = new ArrayList<>();
            if (!Strings.isNullOrEmpty(json)) {
                listObj = mapper.readValue(json,
                        TypeFactory.defaultInstance()
                                .constructCollectionType(ArrayList.class, classType)
                );
            }

            return listObj;
        } catch (IOException ex) {
            throw new RuntimeException("Unable to parse: " + json, ex);
        }
    }

    public static <T> Map<String, T> jsonToMap(String json, Class<T> classType) {
        try {
            Map<String, T> result = new HashMap<>();
            if (!Strings.isNullOrEmpty(json)) {
                return mapper.readValue(json, TypeFactory.defaultInstance()
                        .constructParametricType(HashMap.class,
                                String.class,
                                classType));
            }

            return result;
        } catch (IOException ex) {
            throw new RuntimeException("Unable to parse: " + json, ex);
        }
    }


    public static String object2Json(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Unable to serialize " + obj.getClass().getName() + ": " + obj.toString(), ex);
        }
    }

    public static String prettyPrintObject2Json(Object obj) {
        try {
            return prettyPrintMapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Unable to serialize " + obj.getClass().getName() + ": " + obj.toString(), ex);
        }
    }

    public static JsonNode object2JsonNode(Object value) {
        return mapper.valueToTree(value);
    }

    public static <T> T convertValue(Object copySource, Class<T> destinationClass) {
        return mapper.convertValue(copySource, destinationClass);
    }

    public static <T> T deepClone(Object copySource, Class<T> destinationClass) {
        String json = object2Json(copySource);
        return json2Object(json, destinationClass);
    }

    public static JsonNode standardizeJson(JsonNode jsonNode) {
        if (leafNodes.contains(jsonNode.getNodeType())) {
            return jsonNode;
        }

        if (jsonNode.getNodeType() == JsonNodeType.ARRAY) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                arrayNode.set(i, standardizeJson(arrayNode.get(i)));
            }
            return arrayNode;
        }

        if (jsonNode.getNodeType() == JsonNodeType.OBJECT) {
            ObjectNode objectNode = (ObjectNode) jsonNode;

            for (String fieldName : Lists.newArrayList(objectNode.fieldNames())) {
                objectNode.replace(fieldName, standardizeJson(objectNode.get(fieldName)));
            }
            return objectNode;
        }

        if (jsonNode.getNodeType() == JsonNodeType.STRING) {
            String value = jsonNode.asText();
            try {
                JsonNode node = mapper.readTree(value);
                node = standardizeJson(node);
                return node;
            } catch (JsonProcessingException e) {
                return jsonNode;
            } catch (IOException e) {
                return jsonNode;
            }
        }

        return jsonNode;
    }
}
