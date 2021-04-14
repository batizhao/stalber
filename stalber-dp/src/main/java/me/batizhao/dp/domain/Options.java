package me.batizhao.dp.domain;

/**
 * input/switch/textarea
 *
 * @author batizhao
 * @date 2021/4/12
 */
public class Options {

    private String width = "100%";
    private String defaultValue = "";
    private boolean required = false;
    private String requiredMessage = "";
    private String dataType = "";
    private boolean dataTypeCheck = false;
    private String dataTypeMessage = "";
    private String pattern = "";
    private boolean patternCheck = false;
    private String patternMessage = "";
    private String placeholder = "";
    private String customClass = "";
    private boolean disabled = false;
    private int labelWidth = 100;
    private boolean isLabelWidth = false;
    private boolean hidden = false;
    private boolean dataBind = true;
    private boolean showPassword = false;
    private String remoteFunc = "";
    private String remoteOption = "";

    public Options() {
    }

    public Options(boolean hidden) {
        this.hidden = hidden;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getRequiredMessage() {
        return requiredMessage;
    }

    public void setRequiredMessage(String requiredMessage) {
        this.requiredMessage = requiredMessage;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isDataTypeCheck() {
        return dataTypeCheck;
    }

    public void setDataTypeCheck(boolean dataTypeCheck) {
        this.dataTypeCheck = dataTypeCheck;
    }

    public String getDataTypeMessage() {
        return dataTypeMessage;
    }

    public void setDataTypeMessage(String dataTypeMessage) {
        this.dataTypeMessage = dataTypeMessage;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean isPatternCheck() {
        return patternCheck;
    }

    public void setPatternCheck(boolean patternCheck) {
        this.patternCheck = patternCheck;
    }

    public String getPatternMessage() {
        return patternMessage;
    }

    public void setPatternMessage(String patternMessage) {
        this.patternMessage = patternMessage;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getCustomClass() {
        return customClass;
    }

    public void setCustomClass(String customClass) {
        this.customClass = customClass;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(int labelWidth) {
        this.labelWidth = labelWidth;
    }

    public boolean isLabelWidth() {
        return isLabelWidth;
    }

    public void setLabelWidth(boolean labelWidth) {
        isLabelWidth = labelWidth;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isDataBind() {
        return dataBind;
    }

    public void setDataBind(boolean dataBind) {
        this.dataBind = dataBind;
    }

    public boolean isShowPassword() {
        return showPassword;
    }

    public void setShowPassword(boolean showPassword) {
        this.showPassword = showPassword;
    }

    public String getRemoteFunc() {
        return remoteFunc;
    }

    public void setRemoteFunc(String remoteFunc) {
        this.remoteFunc = remoteFunc;
    }

    public String getRemoteOption() {
        return remoteOption;
    }

    public void setRemoteOption(String remoteOption) {
        this.remoteOption = remoteOption;
    }
}
