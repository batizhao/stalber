package me.batizhao.app.domain.fg;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author batizhao
 * @date 2021/7/9
 */
@Data
@Accessors(chain = true)
public class SlotList implements Slot {

    private List<Options> options = asList(new Options("选项一", 1), new Options("选项二", 2));

    public static class Options {

        public Options() {
        }

        public Options(String label, Integer value) {
            this.label = label;
            this.value = value;
        }

        private String label;

        private Integer value;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

}
