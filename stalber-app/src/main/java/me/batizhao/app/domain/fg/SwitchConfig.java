package me.batizhao.app.domain.fg;

/**
 * @author batizhao
 * @date 2021/7/13
 */
public class SwitchConfig extends Config {

    public SwitchConfig(String label, String tag, String tagIcon, boolean required, Integer formId, String renderKey, String optionType, Boolean border, String defaultValue) {
        super(label, tag, tagIcon, required, formId, renderKey, optionType, border);
        this.defaultValue = defaultValue;
    }

    private String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
