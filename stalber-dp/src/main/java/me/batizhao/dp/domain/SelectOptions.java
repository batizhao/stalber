package me.batizhao.dp.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author batizhao
 * @date 2021/4/12
 */
@Data
public class SelectOptions extends Options {

    private boolean multiple = false;
    private boolean clearable = false;
    private boolean showLabel = false;
    private List<Option> options = asList(new Option("Option 1"), new Option("Option 1"), new Option("Option 1"));
    private boolean remote = false;
    private String remoteType = "option";
    private boolean filterable = false;
    private List<String> remoteOptions = new ArrayList<>();
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
