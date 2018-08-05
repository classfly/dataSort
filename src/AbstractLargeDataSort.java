import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by fujianbo on 2018/8/4.
 *
 * @author fujianbo
 * @date 2018/08/04
 */
public abstract class AbstractLargeDataSort implements LargeDataSort {
    protected BufferedReader reader;
    protected int[] data;
    /** 单个文件允许占内存的大小(KB) */
    private int blockSize = 1024;
    private int blockNumberCount = blockSize / Integer.BYTES;
    private String srcDataFilePath = "data.txt";
    private int blockedFileCount = 0;
    private String sortedFilePrefix = "sorted-";
    private String baseFilePrefix = "data-";
    private String mergedFilePrefix = "merged-";
    private Map<String, Integer> fileNameAndLineNumbers = new HashMap<>();
    private List<String> sortedFileNames = new ArrayList<>();
    private List<String> mergedFileNames = new ArrayList<>();

    @Override
    public void generateNumbers(int numberCount) {
        Random random = new Random();
        File dataFile = new File(this.srcDataFilePath);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dataFile));
            for (int idx = 0; idx < numberCount ; idx++) {
                int number = random.nextInt();
                bufferedWriter.write((number > 0 ? number : -number) + System.getProperty("line.separator"));
            }

            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Failed to generate data file!");
            return;
        }
    }

    @Override
    public boolean loadDataFromFile() {
        if (null == this.srcDataFilePath || this.srcDataFilePath.isEmpty()) {
            return false;
        }

        try {
            this.reader = new BufferedReader(new FileReader(this.srcDataFilePath));
            int lineNumber = 0;
            int fileIdx = 0;
            String data = null;
            BufferedWriter bufferedWriter = null;
            String fileName = null;
            while (null != (data = this.reader.readLine())) {
                if (0 == lineNumber % this.blockNumberCount) {
                    if (null != bufferedWriter) {
                        bufferedWriter.close();
                    }

                    fileName = this.baseFilePrefix + (fileIdx++) + ".txt";
                    File newFile = new File(fileName);
                    bufferedWriter = new BufferedWriter(new FileWriter(newFile));
                    this.fileNameAndLineNumbers.put(fileName, this.blockNumberCount);
                    lineNumber = 0;
                }

                bufferedWriter.write(data + System.getProperty("line.separator"));
                ++lineNumber;
            }

            this.blockedFileCount = fileIdx;
            this.reader.close();
            if (null != bufferedWriter) {
                this.fileNameAndLineNumbers.put(fileName, lineNumber);
                bufferedWriter.close();
            }

            return true;
        } catch (IOException e) {
            System.out.println("Failed to load file! " + this.srcDataFilePath);
            return false;
        }
    }



    @Override
    public void sort() {
        this.fileNameAndLineNumbers.forEach((fileName, dataSize) -> {
            // 从文件中加载待排序数据
            loadData(fileName, dataSize);
            // 调用子类排序算法
            trySort(dataSize);
            // 将结果输出到文件
            writeSortResultsToFile(fileName, dataSize);
        });
    }

    /**
     * 返回本次处理的文件名
     * @return
     */
    abstract protected void trySort(int dataSize);

    private void writeSortResultsToFile(String fileName, int dataSize) {
        try {
            String sortedFileName = this.sortedFilePrefix + fileName;
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sortedFileName));
            int idx = 0;
            while (idx < dataSize) {
                bufferedWriter.write(this.data[idx++] + System.getProperty("line.separator"));
            }

            bufferedWriter.close();
            this.sortedFileNames.add(sortedFileName);
        } catch (IOException e) {
            System.out.println("Failed to open file " + fileName);
        }
    }

    protected void loadData(String fileName, int dataSize) {
        if (0 == this.blockedFileCount) {
            return;
        }

        if (null == this.data) {
            this.data = new int[dataSize];
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String numberStr = null;
            int idx = 0;
            while (null != (numberStr = bufferedReader.readLine())) {
                this.data[idx++] = Integer.valueOf(numberStr);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open file " + fileName);
        } catch (IOException e) {
            System.out.println("Failed to read line!");
        }
    }

    @Override
    public void merge() {
        this.mergedFileNames.addAll(this.sortedFileNames);
        int mergedRoundIdx = 0;
        while (1 != this.mergedFileNames.size()) {
            tryMerge(this.mergedFileNames, mergedRoundIdx++);
        }
    }

    private void tryMerge(List<String> sortedFileNames, int mergedRoundIdx) {
        if (null == sortedFileNames || 0 == sortedFileNames.size()) {
            return;
        }

        List<String> fileNames = new ArrayList<>();
        fileNames.addAll(sortedFileNames);
        ListIterator<String> fileNameIterator = fileNames.listIterator();
        this.mergedFileNames.clear();
        int mergedFileIdx = 0;
        while (fileNameIterator.hasNext()) {
            String fileAName = fileNameIterator.next();

            // 只剩下一个文件待合并，无需再执行while
            if (!fileNameIterator.hasNext()) {
                this.mergedFileNames.add(fileAName);
                break;
            }
            String fileBName = fileNameIterator.next();
            if (null == fileAName || null == fileBName) {
                return;
            }

            try {
                BufferedReader fileAReader = new BufferedReader(new FileReader(fileAName));
                BufferedReader fileBReader = new BufferedReader(new FileReader(fileBName));
                String dataA = fileAReader.readLine();
                String dataB = fileBReader.readLine();
                String mergedFileName = mergedFilePrefix + mergedRoundIdx + "-" + (mergedFileIdx++) + this.baseFilePrefix + ".txt";
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(mergedFileName));
                while (null != dataA || null != dataB) {
                    if (null != dataA && null != dataB) {
                        if (Long.valueOf(dataA) <= Long.valueOf(dataB)) {
                            bufferedWriter.write(dataA + System.getProperty("line.separator"));
                            dataA = fileAReader.readLine();
                        } else {
                            bufferedWriter.write(dataB + System.getProperty("line.separator"));
                            dataB = fileBReader.readLine();
                        }
                    }

                    if (null == dataA && null != dataB) {
                        bufferedWriter.write(dataB + System.getProperty("line.separator"));
                        dataB = fileBReader.readLine();
                    }

                    if (null == dataB && null != dataA) {
                        bufferedWriter.write(dataA + System.getProperty("line.separator"));
                        dataA = fileAReader.readLine();
                    }
                }

                fileAReader.close();
                fileBReader.close();
                bufferedWriter.close();

                this.mergedFileNames.add(mergedFileName);
            } catch (FileNotFoundException e) {
                System.out.println("Failed to open source file!");
            } catch (IOException e) {
                System.out.println("Failed to read data!");
            }
        }


    }
}
