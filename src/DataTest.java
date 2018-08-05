import java.util.Random;

/**
 * Created by fujianbo on 2018/8/3.
 *
 * @author fujianbo
 * @date 2018/08/03
 */
public class DataTest {
    public static void quickSort(int[] srcData, int leftIdx, int rightIdx) {
        if (leftIdx < rightIdx) {
            int pivot = srcData[leftIdx];
            int lowIdx = leftIdx;
            int highIdx = rightIdx;
            while (lowIdx < highIdx) {
                while(lowIdx < highIdx && srcData[highIdx] >= pivot) {
                    highIdx--;
                }

                if (lowIdx >= highIdx) {
                    break;
                }

                srcData[lowIdx] = srcData[highIdx];
                lowIdx++;

                while (lowIdx < highIdx && srcData[lowIdx] <= pivot) {
                    lowIdx++;
                }

                if (highIdx <= lowIdx) {
                    break;
                }

                srcData[highIdx] = srcData[lowIdx];
                highIdx--;
            }

            srcData[lowIdx] = pivot;
            quickSort(srcData, leftIdx, lowIdx - 1);
            quickSort(srcData, lowIdx + 1, rightIdx);
        }
    }

    public static void merge(int[] srcDataA, int[] srcDataB, int[] resultData) {
        int idxA = 0;
        int idxB = 0;
        int insertPos = 0;
        int dataASize = srcDataA.length;
        int dataBSize = srcDataB.length;
        while (idxA < dataASize && idxB < dataBSize) {
            if (srcDataA[idxA] < srcDataB[idxB]) {
                resultData[insertPos++] = srcDataA[idxA++];
            } else {
                resultData[insertPos++] = srcDataB[idxB++];
            }
        }

        while (idxA < dataASize) {
            resultData[insertPos++] = srcDataA[idxA++];
        }

        while (idxB < dataBSize) {
            resultData[insertPos++] = srcDataB[idxB++];
        }
    }

    public static int[] generateTestData(int dataSize) {
        if (dataSize == 0) {
            return null;
        }

        int[] result = new int[dataSize];
        Random random = new Random();
        for (int idx = 0; idx < dataSize; idx++) {
            result[idx] = random.nextInt();
        }

        return result;
    }

    public static void main(String[] args) {
        int[] srcData = generateTestData(100000000);
        quickSort(srcData, 0, srcData.length - 1);
    }
}
