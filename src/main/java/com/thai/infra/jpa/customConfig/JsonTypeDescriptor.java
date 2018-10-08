package com.thai.infra.jpa.customConfig;

import com.hoiio.x.contract.serializer.JsonSerializer;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.MutableMutabilityPlan;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

public class JsonTypeDescriptor
        extends AbstractTypeDescriptor<Object>
        implements DynamicParameterizedType {

    private Class<?> jsonObjectClass;

    @Override
    public void setParameterValues(Properties parameters) {
        jsonObjectClass = ( (ParameterType) parameters.get( PARAMETER_TYPE ) )
                .getReturnedClass();

    }

    public JsonTypeDescriptor() {
        super( Object.class, new MutableMutabilityPlan<Object>() {
            @Override
            protected Object deepCopyNotNull(Object value) {
                try {
                    return JsonSerializer.deepClone(value, value.getClass());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
                //return JacksonUtil.clone(value);
            }
        });
    }

    @Override
    public boolean areEqual(Object one, Object another) {
        if ( one == another ) {
            return true;
        }
        if ( one == null || another == null ) {
            return false;
        }

        return JsonSerializer.object2JsonNode(JsonSerializer.object2Json(one)).equals(
                JsonSerializer.object2JsonNode(JsonSerializer.object2Json(another)));
    }

    @Override
    public String toString(Object value) {
        return JsonSerializer.object2Json(value);
    }

    @Override
    public Object fromString(String string) {
        return JsonSerializer.json2Object(string, jsonObjectClass);
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public <X> X unwrap(Object value, Class<X> type, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }
        if ( String.class.isAssignableFrom( type ) ) {
            return (X) toString(value);
        }
        if ( Object.class.isAssignableFrom( type ) ) {
            return JsonSerializer.json2Object(toString(value), type);
        }
        throw unknownUnwrap( type );
    }

    @Override
    public <X> Object wrap(X value, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }
        return fromString(value.toString());
    }

}