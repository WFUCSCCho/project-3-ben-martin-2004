/*******************************************************************************
 * @file: Proj3.java
 * @description: Implements and evaluates several sorting algorithms
 *               (bubble, merge, quick, heap, transposition) on different
 *               dataset orderings.
 * @author: Ben Martin
 * @date: December 15, 2025
 ******************************************************************************/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Proj3
{
    // ------------------------------------------------------------
    // Merge Sort
    // ------------------------------------------------------------

    public static <T extends Comparable<? super T>> void mergeSort(ArrayList<T> a, int left, int right)
    {
        if (a == null || left >= right)
        {
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSort(a, left, mid);
        mergeSort(a, mid + 1, right);
        merge(a, left, mid, right);
    }

    public static <T extends Comparable<? super T>> void merge(ArrayList<T> a, int left, int mid, int right)
    {
        ArrayList<T> temp = new ArrayList<>(right - left + 1);

        int i = left;
        int j = mid + 1;

        while (i <= mid && j <= right)
        {
            if (a.get(i).compareTo(a.get(j)) <= 0)
            {
                temp.add(a.get(i++));
            }
            else
            {
                temp.add(a.get(j++));
            }
        }

        while (i <= mid)
        {
            temp.add(a.get(i++));
        }

        while (j <= right)
        {
            temp.add(a.get(j++));
        }

        for (int k = 0; k < temp.size(); k++)
        {
            a.set(left + k, temp.get(k));
        }
    }

    // ------------------------------------------------------------
    // Quick Sort (Median-of-Three)
    // ------------------------------------------------------------

    public static <T extends Comparable<? super T>> void quickSort(ArrayList<T> a, int left, int right)
    {
        if (a == null || left >= right)
        {
            return;
        }

        if (right - left < 2)
        {
            if (a.get(left).compareTo(a.get(right)) > 0)
            {
                swap(a, left, right);
            }
            return;
        }

        int pivotIndex = partition(a, left, right);
        quickSort(a, left, pivotIndex - 1);
        quickSort(a, pivotIndex + 1, right);
    }

    public static <T extends Comparable<? super T>> int partition(ArrayList<T> a, int left, int right)
    {
        int mid = left + (right - left) / 2;

        if (a.get(left).compareTo(a.get(mid)) > 0)
        {
            swap(a, left, mid);
        }

        if (a.get(left).compareTo(a.get(right)) > 0)
        {
            swap(a, left, right);
        }

        if (a.get(mid).compareTo(a.get(right)) > 0)
        {
            swap(a, mid, right);
        }

        swap(a, mid, right - 1);
        T pivot = a.get(right - 1);

        int i = left;
        int j = right - 1;

        while (true)
        {
            while (a.get(++i).compareTo(pivot) < 0) { }
            while (a.get(--j).compareTo(pivot) > 0) { }

            if (i >= j)
            {
                break;
            }

            swap(a, i, j);
        }

        swap(a, i, right - 1);
        return i;
    }

    static <T> void swap(ArrayList<T> a, int i, int j)
    {
        T temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }

    // ------------------------------------------------------------
    // Heap Sort (max-heap)
    // ------------------------------------------------------------

    public static <T extends Comparable<? super T>> void heapSort(ArrayList<T> a, int left, int right)
    {
        if (a == null || left >= right)
        {
            return;
        }

        heapify(a, left, right);

        for (int end = right; end > left; end--)
        {
            swap(a, left, end);
            percolateDown(a, left, left, end - 1);
        }
    }

    public static <T extends Comparable<? super T>> void heapify(ArrayList<T> a, int left, int right)
    {
        int lastParent = left + ((right - left) - 1) / 2;

        for (int i = lastParent; i >= left; i--)
        {
            percolateDown(a, i, left, right);
        }
    }

    private static <T extends Comparable<? super T>> void percolateDown(
            ArrayList<T> a, int index, int leftBound, int rightBound)
    {
        while (true)
        {
            int leftChild = leftBound + 2 * (index - leftBound) + 1;
            int rightChild = leftChild + 1;
            int largest = index;

            if (leftChild <= rightBound &&
                    a.get(leftChild).compareTo(a.get(largest)) > 0)
            {
                largest = leftChild;
            }

            if (rightChild <= rightBound &&
                    a.get(rightChild).compareTo(a.get(largest)) > 0)
            {
                largest = rightChild;
            }

            if (largest == index)
            {
                break;
            }

            swap(a, index, largest);
            index = largest;
        }
    }

    // ------------------------------------------------------------
    // Bubble Sort
    // ------------------------------------------------------------

    public static <T extends Comparable<? super T>> int bubbleSort(ArrayList<T> a, int size)
    {
        int comparisons = 0;
        boolean swapped = true;

        for (int pass = 0; pass < size - 1 && swapped; pass++)
        {
            swapped = false;

            for (int i = 0; i < size - 1 - pass; i++)
            {
                comparisons++;

                if (a.get(i).compareTo(a.get(i + 1)) > 0)
                {
                    swap(a, i, i + 1);
                    swapped = true;
                }
            }
        }

        return comparisons;
    }

    // ------------------------------------------------------------
    // Odd-Even Transposition Sort
    // ------------------------------------------------------------

    public static <T extends Comparable<? super T>> int transpositionSort(ArrayList<T> a, int size)
    {
        int comparisons = 0;
        boolean sorted = false;

        while (!sorted)
        {
            sorted = true;

            if (size > 2)
            {
                comparisons++;
            }

            for (int i = 1; i < size - 1; i += 2)
            {
                if (a.get(i).compareTo(a.get(i + 1)) > 0)
                {
                    swap(a, i, i + 1);
                    sorted = false;
                }
            }

            if (size > 1)
            {
                comparisons++;
            }

            for (int i = 0; i < size - 1; i += 2)
            {
                if (a.get(i).compareTo(a.get(i + 1)) > 0)
                {
                    swap(a, i, i + 1);
                    sorted = false;
                }
            }
        }

        return comparisons;
    }

    // ------------------------------------------------------------
    // Main + I/O
    // ------------------------------------------------------------

    public static void main(String[] args) throws IOException
    {
        if (args.length != 3)
        {
            System.out.println("Usage: java Proj3 <file> <algorithm> <lines>");
            return;
        }

        String file = args[0];
        String algo = args[1].toLowerCase();
        int n = Integer.parseInt(args[2]);

        ArrayList<String> data = readLines(file, n);

        ArrayList<String> sorted = new ArrayList<>(data);
        Collections.sort(sorted);

        ArrayList<String> shuffled = new ArrayList<>(sorted);
        Collections.shuffle(shuffled);

        ArrayList<String> reversed = new ArrayList<>(sorted);
        Collections.sort(reversed, Collections.reverseOrder());

        Result r1 = run(algo, "sorted", sorted);
        Result r2 = run(algo, "shuffled", shuffled);
        Result r3 = run(algo, "reversed", reversed);

        print(r1);
        print(r2);
        print(r3);

        appendCSV("analysis.txt", algo, n, r1);
        appendCSV("analysis.txt", algo, n, r2);
        appendCSV("analysis.txt", algo, n, r3);

        writeList("sorted.txt", reversed);
    }

    private static ArrayList<String> readLines(String file, int n) throws IOException
    {
        ArrayList<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            while (list.size() < n)
            {
                String line = br.readLine();
                if (line == null) break;
                list.add(line);
            }
        }

        return list;
    }

    private static void writeList(String file, List<String> list) throws IOException
    {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, false)))
        {
            for (String s : list)
            {
                pw.println(s);
            }
        }
    }

    private static Result run(String algo, String name, ArrayList<String> list)
    {
        Result r = new Result(name);

        if (algo.equals("merge"))
        {
            long t = System.nanoTime();
            mergeSort(list, 0, list.size() - 1);
            r.seconds = (System.nanoTime() - t) / 1e9;
        }
        else if (algo.equals("quick"))
        {
            long t = System.nanoTime();
            quickSort(list, 0, list.size() - 1);
            r.seconds = (System.nanoTime() - t) / 1e9;
        }
        else if (algo.equals("heap"))
        {
            long t = System.nanoTime();
            heapSort(list, 0, list.size() - 1);
            r.seconds = (System.nanoTime() - t) / 1e9;
        }
        else if (algo.equals("bubble"))
        {
            long t = System.nanoTime();
            r.comparisons = bubbleSort(list, list.size());
            r.seconds = (System.nanoTime() - t) / 1e9;
        }
        else if (algo.equals("transposition"))
        {
            r.comparisons = transpositionSort(list, list.size());
        }
        else
        {
            throw new IllegalArgumentException("Unknown algorithm");
        }

        return r;
    }

    private static void print(Result r)
    {
        System.out.println(r.caseName + " -> time: " + r.seconds + " sec, comps: " + r.comparisons);
    }

    private static void appendCSV(String file, String algo, int n, Result r) throws IOException
    {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true)))
        {
            String sec = (r.seconds < 0) ? "" : Double.toString(r.seconds);
            String comp = (r.comparisons < 0) ? "" : Integer.toString(r.comparisons);
            pw.println(algo + "," + n + "," + r.caseName + "," + sec + "," + comp);
        }
    }

    private static class Result
    {
        String caseName;
        double seconds = -1;
        int comparisons = -1;

        Result(String name)
        {
            caseName = name;
        }
    }
}
