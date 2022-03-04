package me.batizhao.app.domain.fg;

import java.util.List;

/**
 * @author batizhao
 * @date 2021/7/13
 */
public class CheckboxConfig extends Config {

    public CheckboxConfig(String label, String tag, String tagIcon, boolean required, Integer formId, String renderKey, String optionType, Boolean border, List<String> defaultValue) {
        super(label, tag, tagIcon, required, formId, renderKey, optionType, border);
        this.defaultValue = defaultValue;
    }

    private List<String> defaultValue;

    public List<String> getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(List<String> defaultValue) {
        this.defaultValue = defaultValue;
    }
}
