package me.batizhao.admin.security;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import me.batizhao.common.constant.ResultEnum;
import me.batizhao.common.util.R;

import java.io.IOException;

/**
 * @author batizhao
 * @since 2020-03-02
 **/
public class MyOAuth2ExceptionSerializer extends StdSerializer<MyOAuth2Exception> {

    public MyOAuth2ExceptionSerializer() {
        super(MyOAuth2Exception.class);
    }

    @Override
    public void serialize(MyOAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("code", ResultEnum.OAUTH2_TOKEN_ERROR.getCode());
        jsonGenerator.writeStringField("message", new R<String>(ResultEnum.OAUTH2_TOKEN_ERROR.getCode()).getMessage());
        jsonGenerator.writeStringField("data", e.getMessage());
        jsonGenerator.writeEndObject();
    }
}
