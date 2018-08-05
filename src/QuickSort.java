/**
 * Created by fujianbo on 2018/8/4.
 *
 * @author fujianbo
 * @date 2018/08/04
 */
public class QuickSort extends AbstractLargeDataSort {
    @Override
    public void trySort(int dataSize) {
        sort(0, dataSize - 1);
    }

    private void sort(int leftIdx, int rightIdx) {
        if (leftIdx < rightIdx) {
            int pivot = this.data[leftIdx];
            int lowIdx = leftIdx;
            int highIdx = rightIdx;
            while (lowIdx < highIdx) {
                while(lowIdx < highIdx && this.data[highIdx] >= pivot) {
                    highIdx--;
                }

                if (lowIdx >= highIdx) {
                    break;
                }

                this.data[lowIdx] = this.data[highIdx];
                lowIdx++;

                while (lowIdx < highIdx && this.data[lowIdx] <= pivot) {
                    lowIdx++;
                }

                if (highIdx <= lowIdx) {
                    break;
                }

                this.data[highIdx] = this.data[lowIdx];
                highIdx--;
            }

            this.data[lowIdx] = pivot;
            sort(leftIdx, lowIdx - 1);
            sort(lowIdx + 1, rightIdx);
        }
    }
}
