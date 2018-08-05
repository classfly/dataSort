import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by fujianbo on 2018/8/3.
 *
 * @author fujianbo
 * @date 2018/08/03
 */
public class Test {
    public static void main(String[] args) {
        //int[] result = generateTestData(100);
        //quicksort(result, 0, result.length-1);
        QuickSort quickSort = new QuickSort();
        //quickSort.generateNumbers(1000);
        quickSort.loadDataFromFile();
        quickSort.sort();
        quickSort.merge();
    }
}
