package me.batizhao.dp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author batizhao
 * @date 2021/7/9
 */
@Data
@Accessors(chain = true)
public class Fields {

    @JsonInclude(Include.NON_EMPTY)
    private String type;

    @JsonInclude(Include.NON_EMPTY)
    private Autosize autosize;

    @JsonProperty("__config__")
    private Config config;

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("__slot__")
    private Slot slot;

    @JsonProperty("__vModel__")
    private String vModel;

    private String placeholder;

    private Style style;

    /**
     * for input
     */
    @JsonInclude(Include.NON_NULL)
    private Boolean clearable;

    /**
     * for input
     */
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("prefix-icon")
    private String prefixIcon;

    /**
     * for input
     */
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("suffix-icon")
    private String suffixIcon;

    /**
     * for input or textarea
     */
    @JsonInclude(Include.NON_NULL)
    private Integer maxlength;

    /**
     * for input or textarea
     */
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("show-word-limit")
    private Boolean showWordLimit;

    private boolean disabled = false;

    /**
     * for input and textarea
     */
    @JsonInclude(Include.NON_EMPTY)
    private Boolean readonly;

    /**
     * for select and checkbox
     */
    @JsonInclude(Include.NON_EMPTY)
    private Boolean filterable;

    /**
     * for select and checkbox
     */
    @JsonInclude(Include.NON_EMPTY)
    private Boolean multiple;

    /**
     * for radio
     */
    @JsonInclude(Include.NON_EMPTY)
    private String size;

    /**
     * for switch
     */
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("active-text")
    private String activeText;

    /**
     * for switch
     */
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("inactive-text")
    private String inactiveText;

    /**
     * for switch
     */
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("active-color")
    private String activeColor;

    /**
     * for switch
     */
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("inactive-color")
    private String inactiveColor;

    /**
     * for switch
     */
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("active-value")
    private String activeValue;

    /**
     * for switch
     */
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("inactive-value")
    private String inactiveValue;

    /**
     * for slider
     */
    @JsonInclude(Include.NON_EMPTY)
    private Integer min;

    /**
     * for slider
     */
    @JsonInclude(Include.NON_EMPTY)
    private Integer max;

    /**
     * for slider
     */
    @JsonInclude(Include.NON_EMPTY)
    private Integer step;

    /**
     * for slider
     */
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("show-stops")
    private Boolean showStops;

    /**
     * for slider
     */
    @JsonInclude(Include.NON_EMPTY)
    private Boolean range;

}
