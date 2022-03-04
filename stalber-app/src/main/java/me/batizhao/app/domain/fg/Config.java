package me.batizhao.app.domain.fg;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.List;

/**
 * @author batizhao
 * @date 2021/7/9
 */
public class Config {

    public Config() {
    }

    public Config(String label, String tag, String tagIcon, boolean required, Integer formId, String renderKey) {
        this.label = label;
        this.tag = tag;
        this.tagIcon = tagIcon;
        this.required = required;
        this.formId = formId;
        this.renderKey = renderKey;
    }

    public Config(String label, String tag, String tagIcon, boolean required, Integer formId, String renderKey, String optionType, Boolean border) {
        this.label = label;
        this.tag = tag;
        this.tagIcon = tagIcon;
        this.required = required;
        this.formId = formId;
        this.renderKey = renderKey;
        this.optionType = optionType;
        this.border = border;
    }

    private String label;

    private String labelWidth;

    private boolean showLabel = true;

    private boolean changeTag = true;

    private String tag;

    private String tagIcon;

    private boolean required = false;

    private String layout = "colFormItem";

    private Integer span = 24;

    private boolean show = true;

    private String document;

    private List<RegList> regList = new ArrayList<>();

    private Integer formId;

    private String renderKey;

    /**
     * for radio
     */
    @JsonInclude(Include.NON_EMPTY)
    private String optionType;

    /**
     * for radio
     */
    @JsonInclude(Include.NON_EMPTY)
    private Boolean border;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(String labelWidth) {
        this.labelWidth = labelWidth;
    }

    public boolean isShowLabel() {
        return showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    public boolean isChangeTag() {
        return changeTag;
    }

    public void setChangeTag(boolean changeTag) {
        this.changeTag = changeTag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTagIcon() {
        return tagIcon;
    }

    public void setTagIcon(String tagIcon) {
        this.tagIcon = tagIcon;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Integer getSpan() {
        return span;
    }

    public void setSpan(Integer span) {
        this.span = span;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public List<RegList> getRegList() {
        return regList;
    }

    public void setRegList(List<RegList> regList) {
        this.regList = regList;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public String getRenderKey() {
        return renderKey;
    }

    public void setRenderKey(String renderKey) {
        this.renderKey = renderKey;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public Boolean getBorder() {
        return border;
    }

    public void setBorder(Boolean border) {
        this.border = border;
    }
}
