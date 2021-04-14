package me.batizhao.dp.domain;

import lombok.Data;

import java.util.List;

/**
 * @author batizhao
 * @date 2021/4/12
 */
@Data
public class RadioAndCheckboxOptions extends Options {

    private boolean inline;
    private boolean showLabel;
    private List<Props> options;
    private boolean remote;
    private String remoteType;
    private List<String> remoteOptions;
    private Props props;

    public class Option {

        public Option(String value) {
            this.value = value;
        }

        private String value;

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
