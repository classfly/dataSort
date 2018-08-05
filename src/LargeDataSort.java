/**
 * Created by fujianbo on 2018/8/4.
 *
 * @author fujianbo
 * @date 2018/08/04
 */
public interface LargeDataSort {
    /**
     * 从文件中读取排序数据
     * @return
     */
    boolean loadDataFromFile();

    /**
     * 排序
     */
    void sort();

    /**
     * 归并排序结果
     */
    void merge();

    /**
     * 生成待排序随机数
     * @param numberCount
     */
    void generateNumbers(int numberCount);
}
