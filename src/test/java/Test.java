import cn.hutool.core.util.NumberUtil;
import io.yx.encrypt.ConsoleProgressBar;

/**
 * @author YX
 * @date 2022/8/1 10:47
 */
public class Test {
    public static void main(String[] args) {
        double v = ((double) 427162624 / 889192448) * 100;
        System.out.println(v);
        ConsoleProgressBar cpb = new ConsoleProgressBar(1, 100, 10);
        cpb.show(v);
    }
}
