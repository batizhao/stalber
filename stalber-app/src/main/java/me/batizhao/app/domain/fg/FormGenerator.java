package me.batizhao.app.domain.fg;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.List;

/**
 * Form generator JSON序列化类
 * @See <a href="https://mrhj.gitee.io/form-generator/">FormGenerator</a>
 *
 * @author batizhao
 * @date 2021/7/9
 */
@Data
@JsonTypeName("formData")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public class FormGenerator {

    private List<Fields> fields;

    private String formRef = "elForm";

    private String formModel = "formData";

    private String size = "medium";

    private String labelPosition = "right";

    private Integer labelWidth = 100;

    private String formRules = "rules";

    private Integer gutter = 15;

    private boolean disabled = false;

    private Integer span = 24;

    private boolean formBtns = true;

}
