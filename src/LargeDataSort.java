/**
 * Created by fujianbo on 2018/8/4.
 *
 * @author fujianbo
 * @date 2018/08/04
 */
public interface LargeDataSort {
    /**
     * ���ļ��ж�ȡ��������
     * @return
     */
    boolean loadDataFromFile();

    /**
     * ����
     */
    void sort();

    /**
     * �鲢������
     */
    void merge();

    /**
     * ���ɴ����������
     * @param numberCount
     */
    void generateNumbers(int numberCount);
}
